package BancoMaster.Bank.domain.repository;
import BancoMaster.Bank.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account , Long> {
    Optional<Account> findByAccountNumber (Long accountNumber);




}
