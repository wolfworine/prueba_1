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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.scotiabank.application.port.input.UserServicePort;
import pe.com.scotiabank.application.service.JwtService;
import pe.com.scotiabank.domain.exception.UserNotFoundException;
import pe.com.scotiabank.domain.model.Phone;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.config.ApplicationConfig;
import pe.com.scotiabank.infrastructure.adapter.config.SecurityConfig;
import pe.com.scotiabank.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.enums.RoleEnum;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.UpdateRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.UserResponse;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.repository.PhoneRepository;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.repository.UserRepository;
import pe.com.scotiabank.infrastructure.adapter.security.JwtAuthenticationManager;
import pe.com.scotiabank.infrastructure.adapter.security.JwtServerAuthenticationConverter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient(timeout = "30000")
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = UserController.class)
@Import({SecurityConfig.class, ApplicationConfig.class, JwtAuthenticationManager.class, JwtService.class, JwtServerAuthenticationConverter.class, JwtTokenizer.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserServicePort userService;

    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private PhoneRepository phoneRepository;

    @MockitoBean
    private UserRestMapper userMapper;

    @Test
    @WithMockUser(username = "johndoe", roles = {"USER"})
    @Order(1)
    void findAll_returnsListOfUsers() {
        // Configuración de datos de prueba
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

        UserResponse userResponse = new UserResponse(
                "1",
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhones(),
                user.getCreated(),
                user.getModified(),
                user.getLastLogin(),
                user.getToken()
        );

        when(userService.findAll()).thenReturn(Flux.just(user));
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Ejecución de la solicitud y verificación
        webTestClient.get()
                .uri("/users/api")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .contains(userResponse);
    }

    @Test
    @WithMockUser(username = "johndoe", roles = {"USER"})
    @Order(2)
    void findById_returnsUser() {

        Phone phone = Phone.builder()
                .number("945749")
                .cityCode("1")
                .countryCode("57")
                .build();

        User user = User.builder()
                .id("1")
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
        UserResponse userResponse = userMapper.toUserResponse(user);

        when(userService.findById("1")).thenReturn(Mono.just((user)));
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get()
                .uri("/users/api/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    @Test
    @WithMockUser(username = "johndoe", roles = {"USER"})
    @Order(3)
    void update_returnsUpdatedUser() {
        Phone phone = Phone.builder()
                .number("945749")
                .cityCode("1")
                .countryCode("57")
                .build();

        User user = User.builder()
                .id("1")
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

        User userUpdate = User.builder()
                .id("1")
                .name("John Doe")
                .email("john.doe@example.com")
                .password("$2a$10$Q54YOM3J4e2LLDZsfNmAqu1L/yaHgdsl5So.QA8LhoEKLNQEgLOqS")
                .phones(List.of(phone))
                .created(LocalDateTime.parse("2025-01-31T10:21:18"))
                .modified(LocalDateTime.parse("2025-02-04T03:14:37"))
                .lastLogin(LocalDateTime.parse("2025-01-31T10:21:18"))
                .token("eyJhbGciOiJIUzI1NiJ6...")
                .isActive(true)
                .role(RoleEnum.USER)
                .build();

        UserResponse userResponse = userMapper.toUserResponse(user);
        UpdateRequest updateRequest = new UpdateRequest(null, null, null, null,"eyJhbGciOiJIUzI1NiJ6...");
        when(userService.update(anyString(), any(User.class))).thenReturn(Mono.just(userUpdate));
        when(userMapper.toUser(any(UpdateRequest.class))).thenReturn(userUpdate);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.put()
                .uri("/users/api/46086503")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    @Test
    @WithMockUser(username = "johndoe", roles = {"USER"})
    @Order(4)
    void delete_deletesUser() {
        when(userService.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/users/api/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithMockUser(username = "johndoe", roles = {"USER"})
    @Order(5)
    void findAll_returnsEmptyListWhenNoUsers() {
        when(userService.findAll()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/users/api")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(0);
    }

    @Test
    @WithMockUser(username = "johndoe", roles = {"USER"})
    @Order(6)
    void findById_returnsNotFoundWhenUserDoesNotExist() {
        when(userService.findById("1")).thenReturn(Mono.error(new UserNotFoundException("User Not Found.")));

        webTestClient.get()
                .uri("/users/api/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithMockUser(username = "johndoe", roles = {"USER"})
    @Order(7)
    void update_returnsNotFoundWhenUserDoesNotExist() {
        UpdateRequest updateRequest = new UpdateRequest(null, null, null, null,"eyJhbGciOiJIUzI1NiJ6...");

        // Simulaciones
        when(userService.findById("1")).thenReturn(Mono.empty());
        when(userService.update(eq("1"), any(User.class)))
                .thenReturn(Mono.error(new UserNotFoundException("User Not Found.")));
        when(userMapper.toUser(any(UpdateRequest.class))).thenReturn(new User());

        // Ejecución de la prueba
        webTestClient.put()
                .uri("/users/api/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

}