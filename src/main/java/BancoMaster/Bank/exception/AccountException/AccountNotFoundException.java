package BancoMaster.Bank.exception.AccountException;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(){
        super(" Account not found ");
    }
}
