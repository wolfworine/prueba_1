package pe.com.scotiabank.infrastructure.adapter.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.scotiabank.application.port.input.AuthServicePort;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.LoginRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.RegisterRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.AuthResponse;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.UserResponse;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/api")
@Slf4j
public class AuthController {

    private final AuthServicePort authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<UserResponse>> login(@RequestBody LoginRequest request) {
        return authService.login(request)
                .map(login -> ResponseEntity.ok()
                        .body(login));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<UserResponse>> register(@RequestBody RegisterRequest request) {
        return authService.register(request)
                .map(login -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(login));
    }
/*
    @PostMapping("/token-refresh")
    public Mono<ResponseEntity<AuthResponse>> tokenRefresh(@RequestBody LoginRequest request) {
        return null;
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<String>> logout(@RequestBody LoginRequest loginRequest) {
        return null;
    }

    @PostMapping("/recover-password")
    public Mono<ResponseEntity<String>> recoverPassword(@RequestBody LoginRequest loginRequest) {
        return null;
    }*/
}
