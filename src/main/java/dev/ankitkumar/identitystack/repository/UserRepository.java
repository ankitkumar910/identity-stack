package dev.ankitkumar.identitystack.repository;

import dev.ankitkumar.identitystack.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("from User u where u.email like %:search% or u.firstName like %:search% or u.lastName like %:search%")
    Page<User> searchAll(String search, Pageable pageable);

    boolean existsByEmail(@Email(message = "Please enter a valid email.") String email);

    boolean existsByPhone(@Pattern(regexp = "[0-9]{10}$",message = "Please enter a valid phone number.") String phone);

    Optional<User> findByUsername(String username);
}
