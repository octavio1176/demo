package BancoMaster.Bank.domain.repository;

import BancoMaster.Bank.domain.entity.Account;
import BancoMaster.Bank.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account , Long> {
    User findByUser(User user);
}
