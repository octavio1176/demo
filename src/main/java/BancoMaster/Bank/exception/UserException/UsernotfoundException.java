package BancoMaster.Bank.exception.UserException;

public class UsernotfoundException extends RuntimeException{
    public UsernotfoundException(){
        super("user not found");
    }
}
