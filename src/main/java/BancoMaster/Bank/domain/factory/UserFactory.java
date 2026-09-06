package BancoMaster.Bank.domain.factory;

import BancoMaster.Bank.domain.entity.User;
import BancoMaster.Bank.domain.entity.UserStatus;
import BancoMaster.Bank.dto.signup.UserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder){
        this.passwordEncoder=passwordEncoder;
    }

    public User create(UserRequest userRequest){
        return User.builder()
                .fullName(userRequest.name())
                .email(userRequest.email())
                .phone(userRequest.phoneNumber())
                .userStatus(UserStatus.USER)
                .password(passwordEncoder.encode(userRequest.password()))
                .build();
    }
}
