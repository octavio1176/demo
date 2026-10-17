package BancoMaster.Bank.controller;
import BancoMaster.Bank.domain.entity.Account;
import BancoMaster.Bank.dto.AccountRequest.AccountRequest;
import BancoMaster.Bank.dto.AccountRequest.DeleteAccountRequest;
import BancoMaster.Bank.dto.AccountRequest.FindAccountByName;
import BancoMaster.Bank.service.AccountService.Account1;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("Account")
@RestController
public class AccountController {

    private  final Account1 account1;

    public AccountController(Account1 account1) {
        this.account1 = account1;
    }

    @PostMapping("/creatAccount")
    public ResponseEntity<Void> creat(@NonNull Authentication authentication, @RequestBody AccountRequest accountRequest) {
        String email = authentication.getName();
        account1.createAccount(email, accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Account>> listAccount(){
        return ResponseEntity.status(HttpStatus.OK).body(account1.listAccount());
    }

    @GetMapping("listMyAccounts")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<Account>> listMyAccounts(@NonNull Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(account1.listMyAccount(email));
    }

    @GetMapping("/findAccount")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Account> account(@RequestBody @NonNull DeleteAccountRequest deleteAccountRequest){
        return ResponseEntity.ok().body(account1.Account(deleteAccountRequest.accountNumber()));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> delete( @RequestBody DeleteAccountRequest deleteAccountRequest){
        account1.deleteAccount(deleteAccountRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByName")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Account> findByName(@RequestBody FindAccountByName findAccountByName){

        return ResponseEntity.status(HttpStatus.OK).body(account1.findByName(findAccountByName));
    }

    @GetMapping("/elemento")
    public ResponseEntity<Boolean> elementos(){
        return ResponseEntity.status(HttpStatus.OK).body(account1.elementos());
    }
}
