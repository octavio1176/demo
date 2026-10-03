package BancoMaster.Bank.dto.signup;
import java.time.LocalDateTime;

public record VerificationCode (String code ) {

    static LocalDateTime EXPIRATION_TIME=LocalDateTime.now().plusMinutes(10);

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(EXPIRATION_TIME);
    }


}
