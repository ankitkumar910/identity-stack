package dev.ankitkumar.identitystack.repository;

import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User u where u.email like %:search% or u.firstName like %:search% or u.lastName like %:search%")
    Page<User> searchAll(String search, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByUsername(String username);

    boolean existsByRoles(Role role);

    boolean existsByUsername(String username);

}
