package pe.com.scotiabank.infrastructure.adapter.input.rest.model.output;

import pe.com.scotiabank.domain.model.User;

public record AuthResponse (User user) {
}