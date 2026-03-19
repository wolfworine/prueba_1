package pe.com.scotiabank.application.port.input;

import pe.com.scotiabank.domain.model.Login;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.LoginRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.RegisterRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.AuthResponse;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.UserResponse;
import reactor.core.publisher.Mono;

public interface AuthServicePort {
    Mono<UserResponse> login(LoginRequest request);
    Mono<UserResponse> register(RegisterRequest request);
    Mono<String> logout(Login login);
    Mono<String> recoverPassword(Login login);
}
