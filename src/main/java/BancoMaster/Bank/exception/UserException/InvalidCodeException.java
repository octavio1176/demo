package BancoMaster.Bank.exception.UserException;

public class InvalidCodeException extends RuntimeException{
    public InvalidCodeException(){
        super("invalid code");
    }
}
