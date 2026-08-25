package dev.ankitkumar.identitystack.service;

import dev.ankitkumar.identitystack.dto.request.UserRegisterRequestDto;
import dev.ankitkumar.identitystack.dto.request.UserUpdateRequestDto;
import dev.ankitkumar.identitystack.dto.response.ListUserResponseDto;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.exception.ConflictException;
import dev.ankitkumar.identitystack.exception.ParameterNotFoundException;
import dev.ankitkumar.identitystack.exception.ResourceNotFoundException;
import dev.ankitkumar.identitystack.mapper.UserMapper;
import dev.ankitkumar.identitystack.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private UserMapper userMapper;
    private UserRepository userRepository;
    private final Set<String> whiteListField = Set.of("firstName", "lastName", "phone", "email");


    @Transactional
    public UserResponseDto createUser(UserRegisterRequestDto userRequestDto) {

        if (userRequestDto.getEmail() != null && userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ConflictException("Email already present.");
        }
        if (userRequestDto.getPhone() != null && userRepository.existsByPhone(userRequestDto.getPhone())) {
            throw new ConflictException("Phone number already present.");
        }
        User user = userMapper.toUser(userRequestDto);
        User userResponse = userRepository.save(user);

        return userMapper.toUserResponseDto(userResponse, HttpStatus.CREATED, "User registered successfully.");
    }

    public ListUserResponseDto findAllUsers(String search, String sortedBy, String dir, int page, int pageSize) {

        List<User> userList;
        Sort sort = Sort.unsorted();


        boolean isValid = isSortedParameterValid(sortedBy);


        if (!isValid)
            throw new ParameterNotFoundException("Sorting with parameter " + sortedBy + " is not supported.", whiteListField);

        if (pageSize < 0) throw new ConflictException("Page size can't be less than 0. ");


        if (sortedBy != null && !sortedBy.isBlank()) {
            if (dir == null || dir.isBlank() || dir.equals("asc")) {
                sort = Sort.by(Sort.Direction.ASC, sortedBy);
            } else if (dir.equals("desc")) {
                sort = Sort.by(Sort.Direction.DESC, sortedBy);
            }

        }

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        // System.out.println("SORT = " + sort);
        //System.out.println("PAGEABLE = " + pageable);

        if (search != null && !search.isBlank()) {
            userList = userRepository.searchAll(search, pageable).getContent();
        } else {
            userList = userRepository.findAll(pageable).getContent();

        }

        int userCount = userList.size();
        String message = String.format("%d results found.", userCount);


        return userMapper.toListUserResponseDto(userList, message, HttpStatus.OK);
    }

    private boolean isSortedParameterValid(String sortedBy) {

        // System.out.println("Called for : " + sortedBy);

        if (sortedBy == null) return true;
        if (sortedBy.isBlank()) return true;


        return whiteListField.contains(sortedBy);
    }

    public UserResponseDto findUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No User found with id " + id));

        return userMapper.toUserResponseDto(user, HttpStatus.OK, "User found.");
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto requestDto, Long id) {

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

        }

        if (updated) {
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

        }

        return userMapper.toUserResponseDto(user, HttpStatus.OK, "User updated successfully.");
    }

    @Transactional
    public void removeUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            throw new ResourceNotFoundException("No user found with id " + id);
        }

    }

    public UserResponseDto findUserByUsername(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.toUserResponseDto(user, HttpStatus.OK, "Success");

    }


    boolean shouldUpdateField(String prevValue, String newValue) {

        return newValue != null && !newValue.isBlank() && !newValue.equals(prevValue);
    }


}
