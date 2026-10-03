package BancoMaster.Bank.exception.AccountException;

public class AccountLimitReachedException extends  RuntimeException{
    public AccountLimitReachedException(){
        super("You have reached the limit for created accounts");
    }

}
