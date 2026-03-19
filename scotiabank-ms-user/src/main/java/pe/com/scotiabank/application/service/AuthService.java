package pe.com.scotiabank.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.scotiabank.application.port.input.AuthServicePort;
import pe.com.scotiabank.application.port.output.PhonePersistencePort;
import pe.com.scotiabank.application.port.output.UserPersistencePort;
import pe.com.scotiabank.domain.exception.DuplicateUserException;
import pe.com.scotiabank.domain.exception.InvalidCredentialException;
import pe.com.scotiabank.domain.model.Login;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.enums.RoleEnum;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.LoginRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.RegisterRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.AuthResponse;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.UserResponse;
import pe.com.scotiabank.infrastructure.adapter.security.TokenProvider;
import pe.com.scotiabank.utils.Constants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static pe.com.scotiabank.utils.ErrorCatalog.INVALID_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements AuthServicePort {

    private final UserPersistencePort userPersistencePort;
    private final PhonePersistencePort phonePersistencePort;
    private final UserRestMapper userRestMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public Mono<UserResponse> login(LoginRequest request) {
        return userPersistencePort.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .switchIfEmpty(Mono.error(new InvalidCredentialException(INVALID_USER.getTitle())))
                .flatMap(this::addPhones)
                .map(this::addToken)
                .map(userRestMapper::toUserResponse);
    }

    private Mono<User> addPhones(User user) {
        return phonePersistencePort.findByUserId(user.getId())
                .collectList()
                .map(phones -> {
                    user.setPhones(phones);
                    return user;
                });
    }

    private User addToken(User user) {
        String token = tokenProvider.generateToken(user);
        user.setToken(token);
        //userPersistencePort.updateToken(user, token); // Update user with token asynchronously
        return user;
    }

    @Override
    @Transactional
    public Mono<UserResponse> register(RegisterRequest request) {
        return userPersistencePort.findByEmail(request.email())
                .hasElement()
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DuplicateUserException("User with email already exists"));
                    }

                    return Mono.just(buildUser(request))
                            .flatMap(userPersistencePort::save)
                            .flatMap(user -> savePhones(user, request))
                            .map(userRestMapper::toUserResponse);
                });
    }

    private Mono<User> savePhones(User user, RegisterRequest request) {
        if (request.phones() == null || request.phones().isEmpty()) {
            return Mono.just(user);
        }

        return Flux.fromIterable(request.phones())
                .map(phone -> {
                    phone.setId(UUID.randomUUID().toString());
                    phone.setUserId(user.getId());
                    return phone;
                })
                .flatMap(phonePersistencePort::save)
                .collectList()
                .map(phones -> {
                    user.setPhones(phones);
                    return user;
                });
    }

    private User buildUser(RegisterRequest request) {
        User user = userRestMapper.toUser(request);
        user.setId(UUID.randomUUID().toString());
        user.setRole(RoleEnum.USER);
        return user;
    }

    @Override
    public Mono<String> logout(Login login) {
        // implement logout logic or throw an exception
        throw new UnsupportedOperationException("Logout not implemented");
    }

    @Override
    public Mono<String> recoverPassword(Login login) {
        // implement recover password logic or throw an exception
        throw new UnsupportedOperationException("Recover password not implemented");
    }
/*
    private Mono<User> createUserAndSave(RegisterRequest request) {
        return Mono.defer(() -> {
            //User newUser = createUser(request);
            return userPersistencePort.save(userRestMapper.toUser(request));
        });
    }*/
/*
    private User createUser(RegisterRequest request) {
        User user = userRestMapper.toUser(request);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(encodePassword(request.password()));
        user.setRole(RoleEnum.USER);
        user.setIsActive(Constants.ENABLED);
        user.setCreated(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        user.setLastLogin(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        return user;
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }*/

}
