package BancoMaster.Bank.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "o nome e obrigatorio")
        String name,
        @Email
        @NotBlank(message = "o email e obrigatorio")
        String email,
        @NotBlank(message = "a senha deve ter no minimo 6 carctares ")
        @Size(max = 9)
        String phoneNumber,
        @NotBlank(message = "o numero de celular e obrigatorio")
        String password

) {
}
