package pe.com.scotiabank.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.scotiabank.application.port.input.UserServicePort;
import pe.com.scotiabank.application.port.output.PhonePersistencePort;
import pe.com.scotiabank.application.port.output.UserPersistencePort;
import pe.com.scotiabank.domain.exception.DuplicateUserException;
import pe.com.scotiabank.domain.exception.InvalidCredentialException;
import pe.com.scotiabank.domain.exception.InvalidFormatException;
import pe.com.scotiabank.domain.exception.UserNotFoundException;
import pe.com.scotiabank.domain.model.Phone;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.config.ValidationProperties;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.enums.RoleEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static pe.com.scotiabank.utils.ErrorCatalog.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserServicePort {

    @Autowired
    private ValidationProperties validationProperties;
    private final UserPersistencePort userPersistencePort;
    private final PhonePersistencePort phonePersistencePort;

    @Override
    public Mono<User> findById(String id) {
        return userPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND.getTitle())))
                .flatMap(user ->
                        phonePersistencePort.findByUserId(user.getId())
                                .collectList()
                                .map(phones -> {
                                    user.setPhones(phones);
                                    return user;
                                })
                );
    }

    @Override
    public Flux<User> findAll() {
        return this.userPersistencePort.findAll()
                .flatMap(user ->
                        phonePersistencePort.findByUserId(user.getId())
                                .collectList()
                                .map(phones -> {
                                    user.setPhones(phones);
                                    return user;
                                })
                );
    }

    @Override
    public Mono<User> save(User user) {
        return validateUser(user)
                .flatMap(userValid -> userPersistencePort.findByEmail(userValid.getEmail())
                .hasElement())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DuplicateUserException("User with email already exists"));
                    }
                    return Mono.just(buildUser(user))
                            .flatMap(userPersistencePort::save)
                            .flatMap(this::savePhones);
                });
    }

    private Mono<User> validateUser(User user) {
        return Mono.fromCallable(() -> {
            validatePasswordFormat(user.getPassword());
            return user;
        });
    }

    private Mono<User> savePhones(User user) {
        if (user.getPhones() == null || user.getPhones().isEmpty()) {
            return Mono.just(user);
        }

        return Flux.fromIterable(user.getPhones())
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

    private User buildUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setRole(RoleEnum.USER);
        return user;
    }

    @Override
    public Mono<User> update(String id, User user) {
        return userPersistencePort.update(id, user)
                .flatMap(updatedUser -> processPhones(updatedUser, user.getPhones()));
    }

    private Mono<User> processPhones(User user, List<Phone> phones) {

        if (isEmpty(phones)) {
            return loadExistingPhones(user);
        }

        return saveOrUpdatePhones(user, phones);
    }

    private Mono<User> loadExistingPhones(User user) {
        return phonePersistencePort.findByUserId(user.getId())
                .collectList()
                .map(existingPhones -> {
                    user.setPhones(existingPhones);
                    return user;
                });
    }

    private Mono<Phone> persistPhone(String userId, Phone phone) {

        phone.setUserId(userId);

        if (phone.getId() != null && !phone.getId().isEmpty()) {
            return phonePersistencePort.update(phone.getId(), phone);
        }

        phone.setId(UUID.randomUUID().toString());
        return phonePersistencePort.save(phone);
    }

    private Mono<User> saveOrUpdatePhones(User user, List<Phone> phones) {
        return Flux.fromIterable(phones)
                .flatMap(phone -> persistPhone(user.getId(), phone))
                .collectList()
                .map(savedPhones -> {
                    user.setPhones(savedPhones);
                    return user;
                });
    }

    private boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userPersistencePort.delete(id)
                .thenMany(phonePersistencePort.findByUserId(id))
                .flatMap(phone -> phonePersistencePort.delete(phone.getId()))
                .then()
                .doOnError(error -> log.error("Error deleting user with id {}: {}", id, error.getMessage(), error));

    }

    private void validatePasswordFormat(String password) {
        if (password == null || !password.matches(validationProperties.getPasswordRegex())) {
            throw new InvalidFormatException(
                    "La clave debe tener al menos 8 caracteres, incluir letras y números"
            );
        }
    }
}
