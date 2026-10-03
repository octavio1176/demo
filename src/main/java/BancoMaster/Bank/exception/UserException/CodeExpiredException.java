package BancoMaster.Bank.exception.UserException;

public class CodeExpiredException extends RuntimeException {
    public CodeExpiredException(){
        super("code expired ask for another one ");
    }
}
