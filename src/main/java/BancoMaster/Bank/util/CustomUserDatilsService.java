package BancoMaster.Bank.util;
import BancoMaster.Bank.domain.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDatilsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDatilsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(  @NonNull String email) throws UsernameNotFoundException {
       return userRepository.findByEmail(email)
               .orElseThrow(()->new UsernameNotFoundException("user not found"));
    }
}
