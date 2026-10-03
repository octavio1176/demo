package BancoMaster.Bank.exception.UserException;

public class CodenotFoundException extends  RuntimeException{
    public CodenotFoundException(){
        super("code not found ");
    }
}
