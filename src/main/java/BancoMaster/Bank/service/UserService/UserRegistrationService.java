package BancoMaster.Bank.service.UserService;
import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.factory.UserFactory;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.dto.signup.UserRequest;
import BancoMaster.Bank.dto.signup.VerificationCode;
import BancoMaster.Bank.dto.signup.signupResponse;
import BancoMaster.Bank.exception.UserException.CodeExpiredException;
import BancoMaster.Bank.exception.UserException.EmailAlreadyExistException;
import BancoMaster.Bank.exception.UserException.InvalidCodeException;
import BancoMaster.Bank.exception.UserException.UsernotfoundException;
import BancoMaster.Bank.security.JwtService;
import BancoMaster.Bank.util.Email;
import BancoMaster.Bank.util.RandomString;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final Map<String,UserRequest> pendingUsers = new ConcurrentHashMap<>();
    private final Map<String ,  VerificationCode> verificationCodes = new ConcurrentHashMap<>();
    private final Email email;
    private final JwtService jwtService;
    private final UserFactory userFactory;
    private final UserDetailsService userDetailsService;

    public UserRegistrationService(UserRepository userRepository, Email email, JwtService jwtService, UserFactory userFactory, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.email = email;
        this.jwtService = jwtService;
        this.userFactory = userFactory;
        this.userDetailsService = userDetailsService;
    }

    public void register(@NonNull UserRequest userRequest){


        if (userRepository.findByEmail(userRequest.email()).isPresent()){
            throw new EmailAlreadyExistException();
        }

        pendingUsers.put(userRequest.email() , userRequest);

        String code = RandomString.codeGenerator();


        verificationCodes.put(userRequest.email(), new VerificationCode(code));

        email.sendConfirmationCode(userRequest.email() , userRequest.name(), code);

    }

    public signupResponse signUp(String email1 , String code){


        VerificationCode saved = verificationCodes.get(email1);
        if (saved==null){
            throw new UsernotfoundException();
        }

        if (saved.isExpired()){
            verificationCodes.remove(email1);
            pendingUsers.remove(email1);
            throw new CodeExpiredException();
        }

        if (!saved.code().equals(code)){
            throw new InvalidCodeException();
        }

        UserRequest userRequest = pendingUsers.get(email1);

        User user =  userFactory.create(userRequest);

        userRepository.save(user);


        verificationCodes.remove(email1);

        pendingUsers.remove(email1);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email1);

        String token = jwtService.generateToken(userDetails);

        user.setToken(token);

        email.sendWelcomeEmail(userRequest.email(), user.getFullName());

        userRepository.save(user);

        return new signupResponse(token);
    }

}