package pe.com.scotiabank.infrastructure.adapter.input.rest;

import io.jsonwebtoken.impl.JwtTokenizer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.scotiabank.application.port.input.AuthServicePort;
import pe.com.scotiabank.application.service.JwtService;
import pe.com.scotiabank.domain.exception.DuplicateUserException;
import pe.com.scotiabank.domain.exception.UserNotFoundException;
import pe.com.scotiabank.domain.model.Phone;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.config.ApplicationConfig;
import pe.com.scotiabank.infrastructure.adapter.config.SecurityConfig;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.enums.RoleEnum;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.LoginRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.RegisterRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.AuthResponse;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.UserResponse;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.repository.UserRepository;
import pe.com.scotiabank.infrastructure.adapter.security.JwtAuthenticationManager;
import pe.com.scotiabank.infrastructure.adapter.security.JwtServerAuthenticationConverter;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@AutoConfigureWebTestClient(timeout = "30000")
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AuthController.class)
@Import({SecurityConfig.class, ApplicationConfig.class, JwtAuthenticationManager.class, JwtService.class, JwtServerAuthenticationConverter.class, JwtTokenizer.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AuthServicePort authService;

    @MockitoBean
    private UserRepository userRepository;

    private String jsonRequest;
/*
    @Test
    @Order(1)
    void loginReturnsOkWhenCredentialsAreValid() {
        jsonRequest = "{\"username\":\"johndoe\",\"password\":\"securePassword123\"}";
        Phone phone = Phone.builder()
                .number("945749")
                .cityCode("1")
                .countryCode("57")
                .build();

        User user = User.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("$2a$10$Q54YOM3J4e2LLDZsfNmAqu1L/yaHgdsl5So.QA8LhoEKLNQEgLOqS")
                .phones(List.of(phone))
                .created(LocalDateTime.parse("2025-01-31T10:21:18"))
                .modified(LocalDateTime.parse("2025-02-04T03:14:37"))
                .lastLogin(LocalDateTime.parse("2025-01-31T10:21:18"))
                .token("eyJhbGciOiJIUzI1NiJ9...")
                .isActive(true)
                .role(RoleEnum.USER)
                .build();

        AuthResponse response = new AuthResponse("eyJhbGciOiJIUzI1NiJ9...", user);

        when(authService.login(any(LoginRequest.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/auth/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(AuthResponse.class)
                .isEqualTo(response);
    }

    @Test
    @Order(2)
    void loginReturnsErrorWhenCredentialsAreInvalid() {
        jsonRequest = "{\"username\":\"johndoe\",\"password\":\"securePassword123\"}";
        when(authService.login(any(LoginRequest.class))).thenReturn(Mono.error(new UserNotFoundException("Invalid credentials.")));

        webTestClient.post()
                .uri("/auth/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isNotFound();
    }
*/
    @Test
    @Order(3)
    void registerReturnsCreatedWhenUserIsNew() {
        jsonRequest = """
                {
                  "name": "John Doe",
                  "email": "john.doe@example.com",
                  "password": "securePassword123",
                  "phones": [
                    {
                      "number": "945749",
                      "cityCode": "1",
                      "countryCode": "57"
                    }
                  ]
                } \s""";
        Phone phone = Phone.builder()
                .number("945749")
                .cityCode("1")
                .countryCode("57")
                .build();

        User user = User.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("$2a$10$Q54YOM3J4e2LLDZsfNmAqu1L/yaHgdsl5So.QA8LhoEKLNQEgLOqS")
                .phones(List.of(phone))
                .created(LocalDateTime.parse("2025-01-31T10:21:18"))
                .modified(LocalDateTime.parse("2025-02-04T03:14:37"))
                .lastLogin(LocalDateTime.parse("2025-01-31T10:21:18"))
                .token("eyJhbGciOiJIUzI1NiJ9...")
                .isActive(true)
                .role(RoleEnum.USER)
                .build();

        //User response = new User(user);
        when(authService.register(any(RegisterRequest.class))).thenReturn(Mono.just(null));

        webTestClient.post()
                .uri("/auth/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .isEqualTo(user);
    }

    @Test
    @Order(4)
    void registerReturnsErrorWhenUsernameExists() {
        jsonRequest = """
                {
                  "name": "John Doe",
                  "email": "john.doe@example.com",
                  "password": "securePassword123",
                  "phones": [
                    {
                      "number": "945749",
                      "cityCode": "1",
                      "countryCode": "57"
                    }
                  ]
                } \s""";
        when(authService.register(any(RegisterRequest.class))).thenReturn(Mono.error(new DuplicateUserException("Username already exists.")));

        webTestClient.post()
                .uri("/auth/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}