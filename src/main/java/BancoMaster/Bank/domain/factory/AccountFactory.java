package BancoMaster.Bank.domain.factory;

import BancoMaster.Bank.domain.entity.Account;
import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.exception.UserException.UsernotfoundException;
import BancoMaster.Bank.util.RandomString;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountFactory {

    private final UserRepository userRepository;

    public AccountFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Account createAccount(Authentication authentication){
        User user =userRepository.findByEmail(authentication.getName()).
                orElseThrow(UsernotfoundException::new);

        return Account.builder()
                .balance(BigDecimal.ZERO)
                .accountNumber(RandomString.accountNumberGenerator())
                .user(user)
                .build();
    }

}
