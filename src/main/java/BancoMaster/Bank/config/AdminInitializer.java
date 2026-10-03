package BancoMaster.Bank.config;
import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.entity.UserStatus;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.security.JwtService;
import jakarta.validation.constraints.Email;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer  implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.admin.email}")
    @Email
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.name}")
    private String adminName;

    @Value("${app.admin.phone}")
    private String adminPhoneNumber;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public void run(String @NonNull ... args) {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .fullName(adminName)
                    .email(adminEmail)
                    .phone(adminPhoneNumber)
                    .userStatus(UserStatus.ADMIN)
                    .password(passwordEncoder.encode(adminPassword))
                    .build();

            String token = jwtService.generateToken(admin);
            admin.setToken(token);
            userRepository.save(admin);

        }
    }
}



