package BancoMaster.Bank.dto.signIn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        @NotBlank(message = "o email e obrigatorio")
        String email,
        @NotBlank(message = "o numero de celular e obrigatorio")
        String password
)  {
}
