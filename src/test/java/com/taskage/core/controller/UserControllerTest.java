package com.taskage.core.controller;

import com.taskage.core.TaskageCoreApplication;
import com.taskage.core.config.TestContainersConfig;
import com.taskage.core.dto.user.UserLoginRequestDto;
import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.dto.user.UserUpdateRequestDto;
import com.taskage.core.enitity.JobTitle;
import com.taskage.core.enitity.Team;
import com.taskage.core.enitity.User;
import com.taskage.core.repository.JobTitleRepository;
import com.taskage.core.repository.TeamRepository;
import com.taskage.core.repository.UserRepository;
import com.taskage.core.service.UserService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpHeaders.ORIGIN;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TaskageCoreApplication.class, TestContainersConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private JobTitleRepository jobTitleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private String csrfToken = "ValidToken";

    private String basicToken;
    private String adminToken;
    private String managerToken;


    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
    }

    @BeforeEach
    public void init() {
        RestAssured.port = port;

        JobTitle jobTitle = JobTitle.builder()
                .name("Software Developer")
                .build();

        jobTitleRepository.save(jobTitle);

        User basicUser = User.builder()
                .username("basictestuser")
                .password(passwordEncoder.encode("password"))
                .firstName("John")
                .lastName("Doe")
                .authRole("ROLE_BASIC")
                .jobTitle(jobTitle)
                .build();
        userRepository.save(basicUser);

        User adminUser = User.builder()
                .username("admintestuser")
                .password(passwordEncoder.encode("password"))
                .firstName("John")
                .lastName("Doe")
                .authRole("ROLE_ADMIN")
                .jobTitle(jobTitle)
                .build();

        userRepository.save(adminUser);

        User managerUser = User.builder()
                .username("managertestuser")
                .password(passwordEncoder.encode("password"))
                .firstName("John")
                .lastName("Doe")
                .authRole("ROLE_MANAGER")
                .jobTitle(jobTitle)
                .build();

        userRepository.save(managerUser);


        Team team = Team.builder()
                .name("Test Team")
                .build();
        teamRepository.save(team);

        managerUser.setTeam(team);
        userRepository.save(managerUser);

        basicToken = getToken("basictestuser");
        adminToken = getToken("admintestuser");
        managerToken = getToken("managertestuser");
    }

    @Test
    public void testPublicEndpoint() {
        given()
                .when()
                .get("/core/users/checkLocalCredentials")
                .then()
                .statusCode(403);
    }

    @Test
    public void testAutomaticLogin() {
        given()
                .header("Authorization", "Bearer " + basicToken)
                .header("x-xsrf-token", csrfToken)
                .header(ORIGIN, "http://localhost:3000")
                .when()
                .get("/core/users/checkLocalCredentials")
                .then()
                .statusCode(200);
    }

    @Test
    public void testLogin() {
        UserLoginRequestDto loginRequest = new UserLoginRequestDto("basictestuser", "password");

        Response response = given()
                .contentType("application/json")
                .header(ORIGIN, "http://localhost:3000")
                .header("x-xsrf-token", csrfToken)
                .body(loginRequest)
                .when()
                .post("/core/users/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String token = response.path("token");
        String username = response.path("user.username");

        assertNotNull(token);
        assertEquals("basictestuser", username);
    }

    @Test
    void testRegisterWithExistingUsername() throws Exception {
        JobTitle jobTitle = jobTitleRepository.findByName("Software Developer");
        UserRegisterRequestDto
                registerRequest =
                new UserRegisterRequestDto("basictestuser", "password", "First", "Last", "ROLE_BASIC", jobTitle, null);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .header("x-xsrf-token", csrfToken)
                .header(ORIGIN, "http://localhost:3000")
                .body(registerRequest)
                .when()
                .post("/core/users/register")
                .then()
                .statusCode(409);
    }

    @Test
    void testRegisterWithExistingJobTitle() throws Exception {
        JobTitle jobTitle = jobTitleRepository.findByName("Software Developer");
        UserRegisterRequestDto
                registerRequest =
                new UserRegisterRequestDto("newuser", "password", "First", "Last", "ROLE_BASIC", jobTitle, null);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .header("x-xsrf-token", csrfToken)
                .header(ORIGIN, "http://localhost:3000")
                .body(registerRequest)
                .when()
                .post("/core/users/register")
                .then()
                .statusCode(200)
                .body("username", equalTo("newuser"));
    }

    @Test
    void testRegisterWithExistingJobTitleName() throws Exception {
        JobTitle jobTitle = JobTitle.builder()
                .name("Software Developer")
                .build();
        UserRegisterRequestDto
                registerRequest =
                new UserRegisterRequestDto("newuser", "password", "First", "Last", "ROLE_BASIC", jobTitle, null);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .header("x-xsrf-token", csrfToken)
                .header(ORIGIN, "http://localhost:3000")
                .body(registerRequest)
                .when()
                .post("/core/users/register")
                .then()
                .statusCode(200)
                .body("username", equalTo("newuser"));
    }

    @Test
    void testRegisterWithNewJobTitle() throws Exception {
        JobTitle jobTitle = JobTitle.builder()
                .name("New Title")
                .build();
        UserRegisterRequestDto
                registerRequest =
                new UserRegisterRequestDto("newuser", "password", "First", "Last", "ROLE_BASIC", jobTitle, null);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .header("x-xsrf-token", csrfToken)
                .header(ORIGIN, "http://localhost:3000")
                .body(registerRequest)
                .when()
                .post("/core/users/register")
                .then()
                .statusCode(200)
                .body("username", equalTo("newuser"));
    }

    @Test
    void testUpdateUser() throws Exception {
        JobTitle jobTitle = JobTitle.builder()
                .name("New Title")
                .build();
        Team team = teamRepository.findByName("Test Team");

        User user = userRepository.findByUsername("basictestuser").get();
        UserUpdateRequestDto
                registerRequest =
                new UserUpdateRequestDto(user.getId(), "newuser", "newpassword", "First", "Last", "ROLE_BASIC",
                        jobTitle,
                        team.getId());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .header("x-xsrf-token", csrfToken)
                .header(ORIGIN, "http://localhost:3000")
                .body(registerRequest)
                .when()
                .post("/core/users/update")
                .then()
                .statusCode(200)
                .body("username", equalTo("newuser"))
                .body("authRole", equalTo("ROLE_BASIC"))
                .body("jobTitle.name", equalTo("New Title"))
                .body("team.name", equalTo("Test Team"))
                .body("team.id", equalTo(team.getId()));
    }

    @Test
    void testDeleteUser() throws Exception {
        User user = userRepository.findByUsername("basictestuser").get();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .header("x-xsrf-token", csrfToken)
                .header(ORIGIN, "http://localhost:3000")
                .when()
                .delete("/core/users/delete/" + user.getId())
                .then()
                .statusCode(200);

        assertEquals(2, userRepository.findAll().size());
    }

    private String getToken(String username) {
        UserLoginRequestDto loginRequest = new UserLoginRequestDto(username, "password");

        return userService.authenticate(loginRequest).token();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        jobTitleRepository.deleteAll();
        teamRepository.deleteAll();
    }
}