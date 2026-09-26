package BancoMaster.Bank.service.UserService;

import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.repository.UserRepository;
import BancoMaster.Bank.exception.UserException.UsernotfoundException;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class LogoutService {

   private final  UserRepository userRepository;

    public LogoutService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UsernotfoundException::new);

        userRepository.delete(user);

    }
}
