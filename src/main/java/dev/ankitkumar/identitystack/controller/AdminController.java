package dev.ankitkumar.identitystack.controller;

import dev.ankitkumar.identitystack.dto.request.UserUpdateRequestDto;
import dev.ankitkumar.identitystack.dto.response.ListUserResponseDto;
import dev.ankitkumar.identitystack.dto.response.RoleUpdateDto;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.exception.ConflictException;
import dev.ankitkumar.identitystack.security.SecurityUtil;
import dev.ankitkumar.identitystack.service.UserService;
import io.jsonwebtoken.lang.Arrays;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminController<role> {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;

        System.out.println("AdminController initialized with UserService: " + userService);
    }

    @GetMapping("")
    public ResponseEntity<ListUserResponseDto> findUser(@RequestParam(name = "search", required = false) String search
            , @RequestParam(name = "sort", required = false) String sortedBy,
                                                         @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir,
                                                         @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                         @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {


        ListUserResponseDto userResponseDto = userService.findAllUsers(search, sortedBy,dir, page, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.findUserById(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody @Valid UserUpdateRequestDto requestDto, @PathVariable Long id) {

        UserResponseDto userResponseDto = userService.updateUser(requestDto, id);

        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity.BodyBuilder deleteUser(@PathVariable Long id) {

        userService.removeUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/admin")
    public ResponseEntity<RoleUpdateDto> addAdmin(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateRole(id,false));
    }

    @DeleteMapping("/{id}/admin")
    public ResponseEntity<?> removeAdmin(@PathVariable Long id){
        userService.updateRole(id,true);
        return ResponseEntity.noContent().build();
    }

}
