package BancoMaster.Bank.util;
import java.security.SecureRandom;

public class RandomString {
    public static String codeGenerator(){
        SecureRandom secureRandom= new SecureRandom();
        int number = 10000 + secureRandom.nextInt(1001,9999);
        return String.valueOf(number);
    }

    public static Long accountNumberGenerator(){
        SecureRandom secureRandom = new SecureRandom();
        return 4000 + secureRandom.nextLong(100,999);
    }


}
