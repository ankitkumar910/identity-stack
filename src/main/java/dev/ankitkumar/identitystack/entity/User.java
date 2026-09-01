package dev.ankitkumar.identitystack.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,
            name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @Column(length = 10, unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @Column(name = "profile_picture",length = 1000)
    private String profilePicture;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    private String password;

    @Column(nullable = false,unique = true)
    private String username;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Role> roles = new HashSet<>();

    private int tokenVersion = 0;



}
