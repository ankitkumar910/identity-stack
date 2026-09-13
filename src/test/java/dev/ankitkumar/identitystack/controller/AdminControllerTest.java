package dev.ankitkumar.identitystack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ankitkumar.identitystack.dto.request.UserUpdateRequestDto;
import dev.ankitkumar.identitystack.dto.response.RoleUpdateDto;
import dev.ankitkumar.identitystack.dto.response.UserData;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.security.CustomUserDetails;
import dev.ankitkumar.identitystack.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class AdminControllerTest {

    @MockitoBean
    private UserService userService;

    User user = new User();
    UserResponseDto userResponseDto = new UserResponseDto();

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {

        user.setId(1L);
        user.setUsername("ankit");
        user.setRoles(Set.of(Role.ADMIN));
        user.setPassword("Password@123");

        UserData userData = new UserData();
        userData.setId(1L);
        userResponseDto.setData(userData);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, List.of());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void findUserById() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(userResponseDto);

        mockMvc.perform(get("/api/v1/admin/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void updateUser() throws Exception {
        UserUpdateRequestDto request = new UserUpdateRequestDto();
        request.setFirstName("Ankit");
        request.setLastName("Kumar");
        request.setPhone("9876543210");
        request.setEmail("ankit@example.com");
        request.setUsername("ankit");

        when(userService.updateUser(any(UserUpdateRequestDto.class), eq(1L))).thenReturn(userResponseDto);

        mockMvc.perform(patch("/api/v1/admin/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void deleteUser() throws Exception {
        when(userService.removeUserById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/admin/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void addAdmin() throws Exception {
        RoleUpdateDto roleUpdateDto = new RoleUpdateDto("Role updated successfully.");
        when(userService.updateRole(1L, false)).thenReturn(roleUpdateDto);

        mockMvc.perform(put("/api/v1/admin/users/{id}/admin", 1L))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Role updated successfully."));
    }

    @Test
    void removeAdmin() throws Exception {
        RoleUpdateDto roleUpdateDto = new RoleUpdateDto("Role updated successfully.");
        when(userService.updateRole(1L, true)).thenReturn(roleUpdateDto);

        mockMvc.perform(delete("/api/v1/admin/users/{id}/admin", 1L))
                .andExpect(status().isNoContent());
    }
}