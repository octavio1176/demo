package BancoMaster.Bank.exception.UserException;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(){
        super("email already exist ");
    }
}
