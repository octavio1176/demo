package BancoMaster.Bank.dto.signup;

    public record ConfirmationRequest(
            String email,
            String code
    ) {}

