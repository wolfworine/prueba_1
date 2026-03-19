package pe.com.scotiabank.infrastructure.adapter.output.persistence.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pe.com.scotiabank.domain.model.Phone;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.entity.PhoneEntity;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PhoneRepository extends R2dbcRepository<PhoneEntity, String> {

    Flux<PhoneEntity> findByUserId(String userID);

}
