package BancoMaster.Bank.domain.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PoolMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Pool pool;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private User invitedBy;

    @Enumerated(EnumType.STRING)
    private PoolMemberRole poolMemberRole;

    @PrePersist
    public void createdAt(){
        LocalDateTime joinedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
