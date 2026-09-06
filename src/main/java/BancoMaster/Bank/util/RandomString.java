package BancoMaster.Bank.util;
import java.security.SecureRandom;

public class RandomString {
    public static String codeGenerator(){
        SecureRandom secureRandom= new SecureRandom();
        int number = 1000 + secureRandom.nextInt(101,999);
        return String.valueOf(number);
    }

    public static int accountNumberGenerator(){
        SecureRandom secureRandom = new SecureRandom();
        return 1000 + secureRandom.nextInt(100,999);
    }
}
