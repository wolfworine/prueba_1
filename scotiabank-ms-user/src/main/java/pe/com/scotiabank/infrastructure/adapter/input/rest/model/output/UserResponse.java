package pe.com.scotiabank.infrastructure.adapter.input.rest.model.output;

import pe.com.scotiabank.domain.model.Phone;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(String id, String name, String email, String password, List<Phone> phones,
                           LocalDateTime created, LocalDateTime modified, LocalDateTime lastLogin,
                           String token) {

}
