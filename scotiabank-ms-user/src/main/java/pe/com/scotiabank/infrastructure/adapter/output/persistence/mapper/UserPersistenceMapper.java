package pe.com.scotiabank.infrastructure.adapter.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    UserEntity toUserEntity(User user);
    User toUser(UserEntity user);

}