package dev.ankitkumar.identitystack.service;

import dev.ankitkumar.identitystack.dto.request.UserPasswordUpdate;
import dev.ankitkumar.identitystack.dto.request.UserRegisterRequestDto;
import dev.ankitkumar.identitystack.dto.request.UserUpdateRequestDto;
import dev.ankitkumar.identitystack.dto.response.ListUserResponseDto;
import dev.ankitkumar.identitystack.dto.response.RoleUpdateDto;
import dev.ankitkumar.identitystack.dto.response.UserPasswordUpdateResponse;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.exception.BadCredentialsExceptions;
import dev.ankitkumar.identitystack.exception.ConflictException;
import dev.ankitkumar.identitystack.exception.ParameterNotFoundException;
import dev.ankitkumar.identitystack.exception.ResourceNotFoundException;
import dev.ankitkumar.identitystack.mapper.UserMapper;
import dev.ankitkumar.identitystack.repository.UserRepository;
import dev.ankitkumar.identitystack.security.SecurityUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private UserMapper userMapper;
    private UserRepository userRepository;
    private final Set<String> whiteListField = Set.of("firstName", "lastName", "phone", "email");
    private final PasswordEncoder encoder;

    @Transactional
    public UserResponseDto createUser(UserRegisterRequestDto userRequestDto) {


        log.info("Creating new user: username = {} ", userRequestDto.getUsername());

        if (userRequestDto.getEmail() != null && userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ConflictException("Email already present.");
        }
        if (userRequestDto.getPhone() != null && userRepository.existsByPhone(userRequestDto.getPhone())) {

            throw new ConflictException("Phone number already present.");
        }

        User user = userMapper.toUser(userRequestDto);


        if (user == null) throw new ConflictException("User is null.");

        User userResponse = userRepository.save(user);

        log.info("User registered successfully:username = {} ,user_id = {}", userResponse.getUsername(), userResponse.getId());

        return userMapper.toUserResponseDto(userResponse, HttpStatus.CREATED, "User registered successfully.");
    }

    public ListUserResponseDto findAllUsers(String search, String sortedBy, String dir, int page, int pageSize) {

        List<User> userList;
        Sort sort = Sort.unsorted();

        log.info("Searching users: search = {}, sortBy = {} , dir = {} , page = {} , pageSize = {}",
                search,
                sortedBy,
                dir,
                page,
                pageSize);


        boolean isValid = isSortedParameterValid(sortedBy);


        if (!isValid)
            throw new ParameterNotFoundException("Sorting with parameter " + sortedBy + " is not supported.", whiteListField);


        if (sortedBy != null && !sortedBy.isBlank()) {

            if (dir == null || dir.isBlank() || dir.equals("asc")) {
                sort = Sort.by(Sort.Direction.ASC, sortedBy);

            } else if (dir.equals("desc")) {
                sort = Sort.by(Sort.Direction.DESC, sortedBy);

            } else {
                throw new IllegalArgumentException("Invalid sorted direction. : " + dir);
            }

        }

        if (pageSize <= 0) throw new IllegalArgumentException("Page size must be positive. ");
        if (page < 0) throw new IllegalArgumentException("Page number can't be less than 0. ");


        Pageable pageable = PageRequest.of(page, pageSize, sort);


        if (search != null && !search.isBlank()) {

            userList = userRepository.searchAll(search, pageable).getContent();

        } else {
            userList = userRepository.findAll(pageable).getContent();
        }



        int userCount = userList.size();
        String message = String.format("%d results found.", userCount);

        log.info("User search completed: results={}", userCount);

        return userMapper.toListUserResponseDto(userList, message, HttpStatus.OK);
    }

    public boolean isSortedParameterValid(String sortedBy) {

        if (sortedBy == null) return true;
        if (sortedBy.isBlank()) return true;


        return whiteListField.contains(sortedBy);
    }

    public UserResponseDto findUserById(Long id) {

        log.info("Finding user: user_id = {}", id);
        if (id < 0) throw new IllegalArgumentException("user_id can't be negative.");

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No User found with id " + id));
        log.info("User found: user_id = {} ,username = {}", user.getId(), user.getUsername());

        return userMapper.toUserResponseDto(user, HttpStatus.OK, "User found.");
    }


    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto requestDto, Long id) {

        log.info("Updating user: user_id = {}",id);

        if (id < 0) throw new IllegalArgumentException("user_id can't be negative.");

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user found with id " + id));

        boolean updated = false;


        if (shouldUpdateField(user.getFirstName(), requestDto.getFirstName())) {
            user.setFirstName(requestDto.getFirstName());
            updated = true;
        }

        if (shouldUpdateField(user.getLastName(), requestDto.getLastName())) {
            user.setLastName(requestDto.getLastName());
            updated = true;
        }

        if (shouldUpdateField(user.getEmail(), requestDto.getEmail())) {

            if (userRepository.existsByEmail(requestDto.getEmail()))
                throw new ConflictException("Email already present.");
            user.setEmail(requestDto.getEmail());
            updated = true;
        }


        if (shouldUpdateField(user.getPhone(), requestDto.getPhone())) {

            if (userRepository.existsByPhone(requestDto.getPhone()))
                throw new ConflictException("Phone is already present.");

            user.setPhone(requestDto.getPhone());
            updated = true;
        }

        if (shouldUpdateField(user.getProfilePicture(), requestDto.getProfilePicture())) {
            user.setProfilePicture(requestDto.getProfilePicture());
            updated = true;

        }

        if (shouldUpdateField(user.getUsername(), requestDto.getUsername())) {

            if (!userRepository.existsByUsername(requestDto.getUsername())) user.setUsername(requestDto.getUsername());
            else throw new ConflictException("Username already taken.");
            updated = true;

        }

        if (updated) {
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            log.info("User updated successfully: user_id= {}",user.getId());
        }

        return userMapper.toUserResponseDto(user, HttpStatus.OK, "User updated successfully.");
    }

    @Transactional
    public boolean removeUserById(Long id) {
        log.info("Removing user: user_id = {}", id);
        if (id < 0) throw new IllegalArgumentException("user_id can't be negative.");

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            SecurityContextHolder.getContext().setAuthentication(null);

            log.info("User Removed: user_id = {}", id);
            return true;
        } else {
            throw new ResourceNotFoundException("No user found with id " + id);
        }

    }

    public UserResponseDto findUserByUsername(String username) {

        log.info("Finding user: username = {}", username);
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username is invalid.");

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        log.info("User found: user_id = {} ,username = {}", user.getId(), user.getUsername());

        return userMapper.toUserResponseDto(user, HttpStatus.OK, "Success");

    }

    public boolean shouldUpdateField(String prevValue, String newValue) {

        return newValue != null && !newValue.isBlank() && !newValue.equals(prevValue);
    }

    @Transactional
    public UserPasswordUpdateResponse updateUserPassword(@Valid UserPasswordUpdate requestDto, long userId) {

        log.info("Updating user password: user_id = {}",userId);

        if (userId < 0) throw new IllegalArgumentException("user_id can't be negative.");
        if (requestDto.getNewPassword() == null || requestDto.getNewPassword().isBlank())
            throw new IllegalArgumentException("Provide a valid updated password.");

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (encoder.matches(requestDto.getOldPassword(), user.getPassword())) {

            user.setPassword(encoder.encode(requestDto.getNewPassword()));
            UserPasswordUpdateResponse passwordUpdateResponse = new UserPasswordUpdateResponse();
            passwordUpdateResponse.setMessage("Password updated.");
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);


            log.info("Password updated successfully: user_id = {}",user.getId());
            return passwordUpdateResponse;

        }

        throw new BadCredentialsExceptions("Password does not matched with older one.");
    }

    @Transactional
    public RoleUpdateDto updateRole(Long id, boolean demotion) {

        log.info("Updating Role: user_id = {} ,demotion = {}",id,demotion);

        if (id < 0) throw new IllegalArgumentException("user_id can't be negative.");

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id));

        Set<Role> roles = user.getRoles();
        long myId = SecurityUtil.getUserId();

        if (myId == id)
            throw new AuthorizationDeniedException("You are not allowed to change your own role.");


        if (demotion) {

            if (roles.contains(Role.ADMIN)) {
                roles.remove(Role.ADMIN);
            } else {
                throw new ResourceNotFoundException("Admin not found with id " + id + ".");
            }

        } else {
            roles.add(Role.ADMIN);
        }
        user.setTokenVersion(user.getTokenVersion() + 1);
        log.info("Role updated successfully: user_id = {}",user.getId());
        return new RoleUpdateDto("Role updated successfully.");
    }
}
