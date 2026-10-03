package BancoMaster.Bank.domain.factory;
import BancoMaster.Bank.domain.entity.Account;
import BancoMaster.Bank.domain.entity.AccountStatus;
import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.dto.AccountRequest.AccountRequest;
import BancoMaster.Bank.exception.UserException.UsernotfoundException;
import BancoMaster.Bank.util.RandomString;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class AccountFactory {

    private final UserRepository userRepository;

    public AccountFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Account createAccount(String email) {
        User user = userRepository.findByEmail(email).
                orElseThrow(UsernotfoundException::new);

        return Account.builder()
                .balance(BigDecimal.ZERO)
                .accountNumber(RandomString.accountNumberGenerator())
                .user(user)
                .name(user.getFullName())
                .accountStatus(AccountStatus.ACTIVE)
                .build();
    }

    public Account createAccount(String email, AccountRequest accountRequest){
        User user =userRepository.findByEmail(email).
                orElseThrow(UsernotfoundException::new);

        return Account.builder()
                .balance(BigDecimal.ZERO)
                .accountNumber(RandomString.accountNumberGenerator())
                .user(user)
                .name(accountRequest.name())
                .accountStatus(AccountStatus.ACTIVE)
                .build();
    }



}
