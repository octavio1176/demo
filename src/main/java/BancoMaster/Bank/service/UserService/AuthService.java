package BancoMaster.Bank.service.UserService;
import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.dto.signIn.LoginRequest;
import BancoMaster.Bank.dto.signIn.LoginResponse;
import BancoMaster.Bank.security.JwtService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
   private  final AuthenticationManager authenticationManager;
   private final JwtService jwtService;
   private final UserRepository userRepository;


    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;

    }

    public LoginResponse login(@NonNull LoginRequest loginRequest) {

        if (loginRequest.email().equalsIgnoreCase("octavionobeza@gmail.com")){
            throw new RuntimeException("nao pode fazer login com essa conta");
        }

        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));


        User user = (User) authentication.getPrincipal();

        assert user != null;

        String token = jwtService.generateToken(user);

        userRepository.save(user);

        return new LoginResponse(token);
    }

}
