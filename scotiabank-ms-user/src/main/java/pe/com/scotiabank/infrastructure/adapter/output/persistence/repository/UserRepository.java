package pe.com.scotiabank.infrastructure.adapter.output.persistence.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.entity.UserEntity;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

    Mono<UserEntity> findByEmail(String email);
}
