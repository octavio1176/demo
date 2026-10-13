package BancoMaster.Bank.exception.AccountException;

public class BlankNameException extends RuntimeException {
    public BlankNameException(){
        super("o campo  nome nao pode estar vazio");
    }
}
