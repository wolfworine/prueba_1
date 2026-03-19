package pe.com.scotiabank.infrastructure.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.com.scotiabank.application.port.output.PhonePersistencePort;
import pe.com.scotiabank.domain.exception.NotFoundException;
import pe.com.scotiabank.domain.model.Phone;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.entity.PhoneEntity;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.mapper.PhonePersistenceMapper;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.repository.PhoneRepository;
import pe.com.scotiabank.utils.Constants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static pe.com.scotiabank.utils.Constants.USER;

@Component
@Slf4j
@RequiredArgsConstructor
public class PhoneRestAdapter implements PhonePersistencePort {

    private final PhoneRepository phoneRepository;
    private final PhonePersistenceMapper phoneMapper;


    @Override
    public Flux<Phone> findByUserId(String userId) {
        return phoneRepository.findByUserId(userId)
                .map(phoneMapper::toPhone)
                .doOnError(error -> log.error("Error fetching all users: {}", error.getMessage(), error));
    }

    @Override
    public Mono<Phone> save(Phone phone) {
        return Mono.defer(() -> {
            PhoneEntity entity = createPhone(phone);
            return phoneRepository.save(entity)
                    .map(phoneMapper::toPhone)
                    .doOnError(error -> log.error(Constants.ERROR_SAVING, USER, error.getMessage(), error));
        });
    }

    private PhoneEntity createPhone(Phone phone) {
        PhoneEntity entity = phoneMapper.toPhoneEntity(phone);
        entity.setNewEntry(true);
        return entity;
    }

    @Override
    public Mono<Phone> update(String id, Phone phone) {
        return Mono.defer(() -> phoneRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + id)))
                .flatMap(existingPhone -> {
                    updatePhone(existingPhone, phone);
                    return phoneRepository.save(existingPhone);
                })
                .map(phoneMapper::toPhone)
                .doOnError(error -> log.error(Constants.ERROR_SAVING, Constants.USER, error.getMessage(), error))
        );
    }

    private void  updatePhone(PhoneEntity existingPhone, Phone phone) {
        Optional.ofNullable(phone.getNumber()).ifPresent(existingPhone::setNumber);
        Optional.ofNullable(phone.getCityCode()).ifPresent(existingPhone::setCityCode);
        Optional.ofNullable(phone.getCountryCode()).ifPresent(existingPhone::setCountryCode);
        existingPhone.setNewEntry(false);
    }

    @Override
    public Mono<Void> delete(String id) {
        return phoneRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + id))) // Manejo de error
                .flatMap(phone -> {
                    phone.setNewEntry(false);
                    return phoneRepository.delete(phone);
                })
                .then()
                .doOnError(error -> log.error("Error disabling user with id {}: {}", id, error.getMessage(), error));
    }

}