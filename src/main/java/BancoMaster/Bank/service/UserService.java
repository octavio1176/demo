package BancoMaster.Bank.service;
import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.entity.UserStatus;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.dto.signIn.LoginRequest;
import BancoMaster.Bank.dto.signIn.LoginResponse;
import BancoMaster.Bank.dto.signup.UserRequest;
import BancoMaster.Bank.dto.signup.VerificationCode;
import BancoMaster.Bank.dto.signup.signupResponse;
import BancoMaster.Bank.security.JwtService;
import BancoMaster.Bank.util.Email;
import BancoMaster.Bank.util.RandomString;
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

    public UserService(Email email, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, UserDetailsService userDetailsService,  AuthenticationManager authenticationManager) {
        this.email = email;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;

    }

    public void register(@NonNull UserRequest userRequest){


        if (userRepository.findByEmail(userRequest.email()).isPresent()){
            throw new RuntimeException("duplicated email");
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
            throw new RuntimeException("user not found");
        }

        if (saved.isExpired()){
            verificationCodes.remove(email);
            pendingUsers.remove(email);
            throw new RuntimeException("code expired ask for another one");
        }

        if (!saved.code().equals(code)){
            throw new RuntimeException("invalid code");
        }

        UserRequest userRequest = pendingUsers.get(email);


        User user = new User();

        user.setFullName(userRequest.name());

        user.setEmail(userRequest.name());

        user.setPassword(passwordEncoder.encode(userRequest.password()));

        user.setUserStatus(UserStatus.USER);

        System.out.println("PHONE NUMBER IS " +userRequest.phoneNumber() );

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

}
