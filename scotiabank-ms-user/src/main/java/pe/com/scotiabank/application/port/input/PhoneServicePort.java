package pe.com.scotiabank.application.port.input;

import pe.com.scotiabank.domain.model.Phone;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhoneServicePort {

    Mono<Phone> findById(String id);
    Flux<Phone> findAll();
    Mono<Phone> save(Phone phone);
    Mono<Phone> update(String id, Phone phone);
    Mono<Void> deleteById(String id);

}