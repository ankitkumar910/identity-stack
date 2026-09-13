package dev.ankitkumar.identitystack.controller;

import dev.ankitkumar.identitystack.dto.request.UserPasswordUpdate;
import dev.ankitkumar.identitystack.dto.request.UserRegisterRequestDto;
import dev.ankitkumar.identitystack.dto.request.UserUpdateRequestDto;
import dev.ankitkumar.identitystack.dto.response.UserData;
import dev.ankitkumar.identitystack.dto.response.UserPasswordUpdateResponse;
import dev.ankitkumar.identitystack.dto.response.UserResponseDto;
import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.security.CustomUserDetails;
import dev.ankitkumar.identitystack.security.jwt.JwtService;
import dev.ankitkumar.identitystack.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ankit");
        user.setPassword("Password@123");

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, List.of());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void createUserTest() throws Exception {
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setFirstName("Ankit");
        request.setLastName("Kumar");
        request.setPhone("9876543210");
        request.setEmail("ankit@example.com");
        request.setUsername("ankit123");
        request.setPassword("Password@123");

        UserResponseDto responseDto = new UserResponseDto();
        UserData data = new UserData();
        data.setId(1L);
        responseDto.setData(data);

        when(userService.createUser(any(UserRegisterRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    public void findMyProfileTest() throws Exception {
        UserData userData = new UserData();
        userData.setId(1L);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setData(userData);

        when(userService.findUserByUsername("ankit")).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    public void updateUserTest() throws Exception {
        UserUpdateRequestDto request = new UserUpdateRequestDto();
        request.setFirstName("Ankit");
        request.setLastName("Kumar");
        request.setPhone("9876543210");
        request.setEmail("ankit@example.com");
        request.setUsername("ankit");
        request.setProfilePicture("https://example.com/new-avatar.png");

        UserData userData = new UserData();
        userData.setId(1L);
        userData.setFirstName("Ankit");
        userData.setLastName("Kumar");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setData(userData);

        when(userService.updateUser(any(UserUpdateRequestDto.class), eq(1L))).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        //this will not work: .content(request.toString())
                        .content(objectMapper.writeValueAsString(request))

                )

                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    public void updateUserPasswordTest() throws Exception {
        UserPasswordUpdate request = new UserPasswordUpdate();
        request.setOldPassword("OldPassword@123");
        request.setNewPassword("NewPassword@456");

        UserPasswordUpdateResponse response = new UserPasswordUpdateResponse();
        response.setMessage("Password updated successfully");

        when(userService.updateUserPassword(any(UserPasswordUpdate.class),eq(1L))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/users/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) //This line , I asked about this


                )

                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Password updated successfully"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        when(userService.removeUserById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/users/me"))
                .andExpect(status().isNoContent());
    }
}




