package dev.ankitkumar.identitystack.controller;

import dev.ankitkumar.identitystack.dto.request.UserRegisterRequestDto;
import dev.ankitkumar.identitystack.dto.response.ListUserResponseDto;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@AllArgsConstructor
public class AdminController {

    private UserService userService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    private ResponseEntity<ListUserResponseDto> findUser(@RequestParam(name = "search", required = false) String search
            , @RequestParam(name = "sort", required = false) String sortedBy,
                                                         @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir,
                                                         @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                         @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {


        ListUserResponseDto userResponseDto = userService.findAllUsers(search, sortedBy,dir, page, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }


    @GetMapping("/{id}")
    private ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.findUserById(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRegisterRequestDto requestDto, @PathVariable Long id) {

        UserResponseDto userResponseDto = userService.updateUser(requestDto, id);

        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity.BodyBuilder deleteUser(@PathVariable Long id) {

        userService.removeUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }

}
