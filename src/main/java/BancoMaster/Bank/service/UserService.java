package BancoMaster.Bank.service;
import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.factory.UserFactory;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.dto.ForgotPassword.ForgotPasswordRequest;
import BancoMaster.Bank.dto.ForgotPassword.ResetPasswordRequest;
import BancoMaster.Bank.dto.signIn.LoginRequest;
import BancoMaster.Bank.dto.signIn.LoginResponse;
import BancoMaster.Bank.dto.signup.UserRequest;
import BancoMaster.Bank.dto.signup.VerificationCode;
import BancoMaster.Bank.dto.signup.signupResponse;
import BancoMaster.Bank.exception.UserException.*;
import BancoMaster.Bank.security.JwtService;
import BancoMaster.Bank.util.Email;
import BancoMaster.Bank.util.RandomString;
import jakarta.mail.MessagingException;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final Email email;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final Map<String, UserRequest> pendingUsers = new ConcurrentHashMap<>();
    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, VerificationCode> passwordResetCodes = new ConcurrentHashMap<>();
    private final UserFactory userFactory;

    private UserService(Email email, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, UserFactory userFactory) {
        this.email = email;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;

        this.userFactory = userFactory;
    }

    public void register(@NonNull UserRequest userRequest){


        if (userRepository.findByEmail(userRequest.email()).isPresent()){
            throw new EmailAlreadyExistException();
        }

        pendingUsers.put(userRequest.email() , userRequest);

        String code = RandomString.codeGenerator();

        LocalDateTime expired = LocalDateTime.now().plusMinutes(10);

        verificationCodes.put(userRequest.email(), new VerificationCode(code, expired));

        email.sendConfirmationCode(userRequest.email() , userRequest.name(), code);

    }

    public signupResponse signUp(String email , String code){

        VerificationCode saved = verificationCodes.get(email);
        if (saved==null){
            throw new UsernotfoundException();
        }

        if (saved.isExpired()){
            verificationCodes.remove(email);
            pendingUsers.remove(email);
            throw new CodeExpiredException();
        }

        if (!saved.code().equals(code)){
            throw new InvalidCodeException();
        }

        UserRequest userRequest = pendingUsers.get(email);

        User user =  userFactory.create(userRequest);

        userRepository.save(user);

        verificationCodes.remove(email);

        pendingUsers.remove(email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        String token = jwtService.generateToken(userDetails);

        user.setToken(token);

        userRepository.save(user);

        return new signupResponse(token);
    }

    public LoginResponse login(@NonNull LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        User user = (User) authentication.getPrincipal();

        assert user != null;

        String token =jwtService.generateToken(user);

        user.setToken(token);

        userRepository.save(user);

        return new LoginResponse(token);
    }

    public void forgotPassword(@NonNull ForgotPasswordRequest request) throws MessagingException {

       User user =userRepository.findByEmail(request.email())
               .orElseThrow(UsernotfoundException::new);


        LocalDateTime EXPIRATION_TIME=LocalDateTime.now().plusMinutes(10);

        String code =RandomString.codeGenerator();

        passwordResetCodes.put(request.email(), new VerificationCode(code , EXPIRATION_TIME));
        System.out.println(request.email() + " CODIGOOOOO " +code);

        email.sendCode(request.email(), user.getFullName(), code);

    }

    public void resetPassword(@NonNull ResetPasswordRequest request)
    {
        System.out.println( " nulooooo " + request.code());



        VerificationCode verificationCode = passwordResetCodes.get(request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(UsernotfoundException::new);


        if (verificationCode==null)
        {
        throw new RuntimeException("nuloooo");
        }

        if (verificationCode.isExpired())
        {
            throw new CodeExpiredException();
        }
        System.out.println( " cdigo " + request.code());

        if (!verificationCode.code().equals(request.code()))
        {
            throw new InvalidCodeException();
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        String token = jwtService.generateToken(user);

        user.setToken(token);

        userRepository.save(user);

        passwordResetCodes.remove(request.email());
    }

    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UsernotfoundException::new);

        userRepository.delete(user);

    }

}
