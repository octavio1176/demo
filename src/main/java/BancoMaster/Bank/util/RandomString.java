package BancoMaster.Bank.util;
import java.security.SecureRandom;

public class RandomString {
    public static String codeGenerator(){
        SecureRandom secureRandom= new SecureRandom();
        int number = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(number);
    }

    public static Long accountNumberGenerator(){
        SecureRandom secureRandom = new SecureRandom();
        return 1000 + secureRandom.nextLong(9000);
    }


}
