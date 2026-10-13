package BancoMaster.Bank.dto.AccountRequest;

import jakarta.validation.constraints.Pattern;

public record FindAccountByName (
        @Pattern(
                regexp = "^[A-Z]+( [A-Z]+)*$",
                message = "O nome deve conter apenas letras maiúsculas e sem caracteres especiais"
        )
        String name

) {
}
