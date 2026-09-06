package BancoMaster.Bank.domain.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(length = 9)
    private String phone;

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Pool> pools;

    private String token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @Override
    public @NullMarked Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.userStatus== UserStatus.ADMIN){
            return List.of(new SimpleGrantedAuthority("Role_User"), new SimpleGrantedAuthority("Role_Admin"));
        }
        return List.of(new SimpleGrantedAuthority("Role_User"));
    }

    @Override
    public @NullMarked  String getUsername() {
        return email;
    }

    @Override
    public @NullMarked boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public @NullMarked  boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public @NullMarked boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public @NullMarked boolean isEnabled() {
        return true;
    }
}
