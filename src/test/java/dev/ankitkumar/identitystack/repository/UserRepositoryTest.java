package dev.ankitkumar.identitystack.repository;

import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;


    private User createUser(String firstName, String username) {

        User user = new User();

        user.setFirstName(firstName);
        user.setUsername(username);

        return user;
    }


    @Test
    void shouldReturnTrueWhenEmailExists() {

        User user = createUser("Ankit", "ankit123");
        user.setEmail("ankit@gmail.com");

        // save the user into the test database.
        userRepository.save(user);

        //  data is coming from test db
        boolean result = userRepository.existsByEmail("ankit@gmail.com");

        Assertions.assertTrue(result);
    }


    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {

        boolean result =
                userRepository.existsByEmail("alien100@gmail.com");

        Assertions.assertFalse(result);
    }


    @Test
    void shouldReturnTrueWhenPhoneExists() {

        User user = createUser("Ankit", "ankit123");
        user.setPhone("9876543210");

        userRepository.save(user);

        boolean result =
                userRepository.existsByPhone("9876543210");

        Assertions.assertTrue(result);
    }


    @Test
    void shouldReturnFalseWhenPhoneDoesNotExist() {

        boolean result =
                userRepository.existsByPhone("9999999999");

        Assertions.assertFalse(result);
    }



    @Test
    void shouldFindUserByUsername() {

        User user = createUser("Ankit", "ankit123");

        userRepository.save(user);

        Optional<User> result =
                userRepository.findByUsername("ankit123");


        Assertions.assertTrue(result.isPresent());

        // Check that the returned user is actually the expected one.
        Assertions.assertEquals(
                "Ankit",
                result.get().getFirstName()
        );
    }


    @Test
    void shouldReturnEmptyWhenUsernameDoesNotExist() {

        Optional<User> result =
                userRepository.findByUsername("alien3000");

        Assertions.assertTrue(result.isEmpty());
    }



    @Test
    void shouldReturnTrueWhenUsernameExists() {

        User user = createUser("Ankit", "ankit123");

        userRepository.save(user);

        boolean result =
                userRepository.existsByUsername("ankit123");

        Assertions.assertTrue(result);
    }


    @Test
    void shouldReturnFalseWhenUsernameDoesNotExist() {

        boolean result =
                userRepository.existsByUsername("unknownUser");

        Assertions.assertFalse(result);
    }



    @Test
    void shouldReturnTrueWhenRoleExists() {

        User user = createUser("Ankit", "ankit123");


        user.setRoles(Set.of(Role.USER));

        userRepository.save(user);

        boolean result =
                userRepository.existsByRoles(Role.USER);

        Assertions.assertTrue(result);
    }


    @Test
    void shouldReturnFalseWhenRoleDoesNotExist() {

        User user = createUser("Ankit", "ankit123");

        user.setRoles(Set.of(Role.USER));

        userRepository.save(user);

        boolean result =
                userRepository.existsByRoles(Role.ADMIN);

        Assertions.assertFalse(result);
    }


   //testing for searchAll()

    @Test
    void shouldSearchUserByFirstName() {

        User user = createUser("Ankit", "ankit123");

        userRepository.save(user);

        Page<User> result =
                userRepository.searchAll(
                        "Ankit",
                        PageRequest.of(0, 10)
                );

        Assertions.assertEquals(1, result.getTotalElements());

        Assertions.assertEquals(
                "Ankit",
                result.getContent().getFirst().getFirstName()
        );
    }


    @Test
    void shouldSearchUserByLastName() {

        User user = createUser("Ankit", "ankit123");

        user.setLastName("Kumar");

        userRepository.save(user);

        Page<User> result =
                userRepository.searchAll(
                        "Kumar",
                        PageRequest.of(0, 10)
                );

        Assertions.assertEquals(1, result.getTotalElements());

        Assertions.assertEquals(
                "Kumar",
                result.getContent().getFirst().getLastName()
        );
    }


    @Test
    void shouldReturnEmptyPageWhenSearchDoesNotMatch() {

        User user = createUser("Ankit", "ankit123");

        userRepository.save(user);

        Page<User> result =
                userRepository.searchAll(
                        "Rahul",
                        PageRequest.of(0, 10)
                );

        Assertions.assertEquals(0, result.getTotalElements());
    }


}