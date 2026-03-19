package pe.com.scotiabank.infrastructure.adapter.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.com.scotiabank.domain.model.Phone;
import pe.com.scotiabank.domain.model.User;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.entity.PhoneEntity;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface PhonePersistenceMapper {
    PhoneEntity toPhoneEntity(Phone phone);
    Phone toPhone(PhoneEntity phone);

}