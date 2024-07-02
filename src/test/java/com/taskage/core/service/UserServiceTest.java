package com.taskage.core.service;

import com.taskage.core.TaskageCoreApplication;
import com.taskage.core.config.security.JwtProvider;
import com.taskage.core.dto.user.*;
import com.taskage.core.enitity.JobTitle;
import com.taskage.core.enitity.Team;
import com.taskage.core.enitity.User;
import com.taskage.core.exception.conflict.UsernameConflictException;
import com.taskage.core.exception.notFound.NotFoundException;
import com.taskage.core.exception.security.AuthenticationFailedException;
import com.taskage.core.mapper.UserMapper;
import com.taskage.core.repository.JobTitleRepository;
import com.taskage.core.repository.TeamRepository;
import com.taskage.core.repository.UserRepository;
import com.taskage.core.utils.UserActivityLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {TaskageCoreApplication.class})
public class

UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private UserActivityLogger userActivityLogger;
    @MockBean
    private JobTitleRepository jobTitleRepository;
    @MockBean
    private TeamRepository teamRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        User user =
                User.builder().id(1).username("testuser").password("encodedpassword").firstName("John").lastName("Doe")
                        .authRole(
                                "ROLE_USER").build();
        User newUser =
                User.builder().id(2).username("newtestuser").password("encodedpassword").firstName("John").lastName(
                        "Doe").authRole(
                        "ROLE_USER").build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correctpassword", "encodedpassword")).thenReturn(true);
        when(passwordEncoder.matches("wrongpassword", "encodedpassword")).thenReturn(false);
        when(jwtProvider.generateToken(eq("1"), any(), eq("ROLE_USER"))).thenReturn("mockedtoken");
        when(userMapper.mapUserToUserResponseDto(user)).thenReturn(
                new UserResponseDto(1, "testuser", "Test", "User", "ROLE_USER", null, null));
        when(userRepository.existsByUsername("newtestuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.mapUserCreateEditDtoToUser(any(UserRegisterRequestDto.class),
                eq("encodedPassword"))).thenReturn(newUser);
        when(userMapper.mapUserToUserResponseDto(newUser)).thenReturn(new UserResponseDto(2, "newtestuser", "John",
                "Doe", "ROLE_USER", null, null));
        doAnswer((Answer<User>) invocation -> {
            User newUser1 = invocation.getArgument(0);
            newUser1.setId(2);
            return newUser1;
        }).when(userRepository).save(newUser);
        when(userRepository.findById(2)).thenReturn(Optional.of(newUser));
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);
    }

    @Test
    void test_successful_authentication_with_correct_username_and_password() {
        UserLoginRequestDto requestDto = new UserLoginRequestDto("testuser", "correctpassword");

        UserLoginResponseDto responseDto = userService.authenticate(requestDto);

        assertNotNull(responseDto);
        assertEquals("mockedtoken", responseDto.token());
        assertEquals("testuser", responseDto.user().username());
    }

    @Test
    void test_authentication_with_incorrect_password() {
        UserLoginRequestDto requestDto = new UserLoginRequestDto("testuser", "wrongpassword");

        assertThrows(AuthenticationFailedException.class, () -> {
            userService.authenticate(requestDto);
        });

        verify(userActivityLogger).logUserActivity("Failed login attempt. Incorrect password.", "ERROR");
    }

    @Test
    public void test_create_user_successfully() {
        UserRegisterRequestDto
                userRegisterRequestDto = new UserRegisterRequestDto("newtestuser", "password", "John", "Doe",
                "ROLE_USER",
                new JobTitle(), null);

        UserResponseDto response = userService.create(userRegisterRequestDto);

        assertNotNull(response);
        assertEquals("newtestuser", response.username());
    }

    @Test
    void test_create_user_username_conflict() {
        UserRegisterRequestDto userRegisterRequestDto =
                new UserRegisterRequestDto("existinguser", "password", "John", "Doe", "USER", new JobTitle(), null);

        assertThrows(UsernameConflictException.class, () -> {
            userService.create(userRegisterRequestDto);
        });
    }

    @Test
    void test_assign_existing_job_title_to_user_by_job_title_id() {
        User user = new User();
        user.setId(1);
        JobTitle jobTitle = new JobTitle();
        jobTitle.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(jobTitleRepository.getById(1)).thenReturn(jobTitle);

        userService.assignJobTitleToUser(1, jobTitle);

        verify(userRepository).save(user);
        assertEquals(jobTitle, user.getJobTitle());
    }

    @Test
    void test_user_id_does_not_exist_in_repository() {
        JobTitle jobTitle = new JobTitle();
        jobTitle.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.assignJobTitleToUser(1, jobTitle);
        });
    }

    @Test
    void test_assign_existing_team_to_existing_user() {
        User user = new User();
        user.setId(1);
        Team team = new Team();
        team.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        userService.assignTeamToUser(1, 1);

        verify(userRepository).save(user);
        assertEquals(team, user.getTeam());
    }

    @Test
    void test_throw_not_found_exception_when_user_id_does_not_exist() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.assignTeamToUser(1, 1);
        });
    }

    @Test
    void test_throws_not_found_exception_when_user_does_not_exist() {
        UserUpdateRequestDto userUpdateRequestDto =
                new UserUpdateRequestDto(1, "newUsername", "newPassword", "newFirstName", "newLastName", "newAuthRole",
                        new JobTitle(), 1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.update(userUpdateRequestDto);
        });
    }
}
