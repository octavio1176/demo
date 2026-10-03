package BancoMaster.Bank.service.AccountService;
import BancoMaster.Bank.domain.entity.Account;
import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.factory.AccountFactory;
import BancoMaster.Bank.domain.repository.AccountRepository;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.dto.AccountRequest.AccountRequest;
import BancoMaster.Bank.dto.AccountRequest.DeleteAccountRequest;
import BancoMaster.Bank.exception.AccountException.AccountLimitReachedException;
import BancoMaster.Bank.exception.AccountException.AccountNotFoundException;
import BancoMaster.Bank.exception.UserException.UsernotfoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Account1 {
    private final AccountFactory accountFactory;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;


    public Account1(AccountFactory accountFactory, AccountRepository accountRepository, UserRepository userRepository) {
        this.accountFactory = accountFactory;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }


    public void createAccount(String email, AccountRequest accountRequest){

        User user = userRepository.findByEmail(email)
                .orElseThrow(UsernotfoundException::new);
        List<Account> accounts = user.getAccounts();

        if (accounts.size()>=3){
            throw new AccountLimitReachedException();
        }

        BancoMaster.Bank.domain.entity.Account account = accountFactory.createAccount(email, accountRequest);
        accountRepository.save(account);

    }

    public void createAccount(String email){
        BancoMaster.Bank.domain.entity.Account account = accountFactory.createAccount(email);
        accountRepository.save(account);

    }

    public void deleteAccount(DeleteAccountRequest deleteAccountRequest){
        Account  account =accountRepository.findByAccountNumber(deleteAccountRequest.accountNumber())
                .orElseThrow(AccountNotFoundException::new);

        accountRepository.delete(account);
    }

    public Account Account(DeleteAccountRequest deleteAccountRequest){
        return accountRepository.findByAccountNumber(deleteAccountRequest.accountNumber())
                .orElseThrow(AccountNotFoundException::new);

    }


    public List<Account>  listAccount(){
        return accountRepository.findAll();
    }




}
