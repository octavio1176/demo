package BancoMaster.Bank.service.UserService;

import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.dto.ForgotPassword.ForgotPasswordRequest;
import BancoMaster.Bank.dto.ForgotPassword.ResetPasswordRequest;
import BancoMaster.Bank.dto.signup.VerificationCode;
import BancoMaster.Bank.exception.UserException.CodeExpiredException;
import BancoMaster.Bank.exception.UserException.InvalidCodeException;
import BancoMaster.Bank.exception.UserException.UsernotfoundException;
import BancoMaster.Bank.security.JwtService;
import BancoMaster.Bank.util.Email;
import BancoMaster.Bank.util.RandomString;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {
    private final UserRepository userRepository;
    private final Map<String , VerificationCode> passwordResetCodes = new ConcurrentHashMap<>();
    private final Email email;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public PasswordResetService(UserRepository userRepository, Email email, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.email = email;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void forgotPassword(@NonNull ForgotPasswordRequest request) {

        User user =userRepository.findByEmail(request.email())
                .orElseThrow(UsernotfoundException::new);

        String code = RandomString.codeGenerator();

        passwordResetCodes.put(request.email(), new VerificationCode(code ));


        email.sendCode(request.email(), user.getFullName(), code);

    }

    public void resetPassword(@NonNull ResetPasswordRequest request)
    {

        VerificationCode verificationCode = passwordResetCodes.get(request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(UsernotfoundException::new);


        if (verificationCode==null)
        {
            throw new RuntimeException("null");
        }

        if (verificationCode.isExpired())
        {
            throw new CodeExpiredException();
        }


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

}
