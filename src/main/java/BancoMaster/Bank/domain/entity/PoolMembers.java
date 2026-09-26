package BancoMaster.Bank.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    private PoolMemberRole poolMemberRole;
}
