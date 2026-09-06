package BancoMaster.Bank.dto.signup;
import java.time.LocalDateTime;

public record VerificationCode (String code , LocalDateTime expireAt) {
    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireAt);
    }
}
