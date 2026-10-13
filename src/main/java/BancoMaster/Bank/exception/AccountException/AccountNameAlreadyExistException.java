package BancoMaster.Bank.exception.AccountException;

public class AccountNameAlreadyExistException extends  RuntimeException {
    public AccountNameAlreadyExistException(){
        super("ja existe uma conta com esse nome");
    }
}
