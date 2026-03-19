package pe.com.scotiabank.infrastructure.adapter.input.rest.model.input;

import pe.com.scotiabank.domain.model.Phone;

import java.util.List;

public record UserRequest(String name, String email, String password, List<Phone> phones) {
}
