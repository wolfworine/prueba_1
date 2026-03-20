package pe.com.scotiabank.infrastructure.adapter.input.rest.model.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import pe.com.scotiabank.domain.model.Phone;

import java.util.List;

public record UserRequest(String name,
                          @Email(message = "El correo debe tener un formato válido")
                          @Pattern(
                                  regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                                  message = "El correo debe seguir el formato correcto: aaaaaaa@dominio.cl"
                          )
                          String email,


                          String password,

                          List<Phone> phones) {


}
