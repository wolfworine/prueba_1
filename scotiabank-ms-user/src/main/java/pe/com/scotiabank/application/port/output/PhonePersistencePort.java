package pe.com.scotiabank.application.port.output;

import pe.com.scotiabank.domain.model.Phone;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface PhonePersistencePort {
    Flux<Phone> findByUserId(String userId);
    Mono<Phone> update(String id,Phone phone);
    Mono<Phone> save(Phone phone);
    Mono<Void> delete(String id);
}