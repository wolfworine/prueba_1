package pe.com.scotiabank.application.port.output;

import pe.com.scotiabank.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface UserPersistencePort {

    Mono<User> findByEmail(String email);
    Mono<User> findById(String document);
    Flux<User> findAll();
    Mono<User> update(String id,User user);
    Mono<User> save(User user);
    Mono<Void> delete(String id);

    Mono<User>  updateToken(User user);
}