package pe.com.scotiabank.infrastructure.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.scotiabank.application.port.output.UserPersistencePort;
import pe.com.scotiabank.domain.exception.NotFoundException;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.entity.UserEntity;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.repository.UserRepository;
import pe.com.scotiabank.infrastructure.adapter.security.TokenProvider;
import pe.com.scotiabank.utils.Constants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static pe.com.scotiabank.utils.Constants.USER;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRestAdapter implements UserPersistencePort {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toUser)
                .doOnError(error -> log.error("Error fetching user by email {}: {}", email, error.getMessage(), error));
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toUser)
                .doOnError(error -> log.error("Error fetching user by id {}: {}", id, error.getMessage(), error));
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll()
                .map(userMapper::toUser)
                .doOnError(error -> log.error("Error fetching all users: {}", error.getMessage(), error));
    }

    @Override
    public Mono<User> save(User user) {
        return Mono.defer(() -> {
            UserEntity entity = createUser(user);
            return userRepository.save(entity)
                    .map(userMapper::toUser)
                    .doOnError(error -> log.error(Constants.ERROR_SAVING, USER, error.getMessage(), error));
        });
    }

    private UserEntity createUser(User user) {
        UserEntity entity = userMapper.toUserEntity(user);
        String token = tokenProvider.generateToken(user);
        entity.setToken(token);
        entity.setPassword(encodePassword(user.getPassword()));
        entity.setIsActive(Constants.ENABLED);
        entity.setCreated(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        entity.setLastLogin(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        entity.setNewEntry(true);
        return entity;
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public Mono<User> update(String id, User user) {
        return Mono.defer(() -> userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + id)))
                .flatMap(existingUser -> {
                    updateUser(existingUser, user);
                    return userRepository.save(existingUser);
                })
                .map(userMapper::toUser)
                .doOnError(error -> log.error(Constants.ERROR_SAVING, Constants.USER, error.getMessage(), error))
        );
    }

    private void  updateUser(UserEntity existingUser, User user) {
        Optional.ofNullable(user.getName()).ifPresent(existingUser::setName);
        Optional.ofNullable(user.getPassword()).ifPresent(existingUser::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(user.getToken()).ifPresent(existingUser::setToken);
        existingUser.setModified(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        existingUser.setNewEntry(false);
    }

    @Override
    public Mono<Void> delete(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + id))) // Manejo de error
                .flatMap(user -> {
                    user.setIsActive(false);
                    user.setNewEntry(false);
                    return userRepository.save(user);
                })
                .then()
                .doOnError(error -> log.error("Error disabling user with id {}: {}", id, error.getMessage(), error));
    }

    @Override
    public Mono<User> updateToken(User user) {
        UserEntity userEntity = userMapper.toUserEntity(user);
        String token = tokenProvider.generateToken(user);
        userEntity.setToken(token);
        userEntity.setModified(Constants.convertToLocalTimeZone(LocalDateTime.now()));
        userEntity.setNewEntry(false);
        return userRepository.save(userEntity)
                .map(userMapper::toUser)
                .doOnError(error -> log.error(Constants.ERROR_SAVING, Constants.USER, error.getMessage(), error));
    }

}