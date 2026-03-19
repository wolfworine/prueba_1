package pe.com.scotiabank.infrastructure.adapter.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.scotiabank.application.port.input.UserServicePort;
import pe.com.scotiabank.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.UpdateRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.input.UserRequest;
import pe.com.scotiabank.infrastructure.adapter.input.rest.model.output.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/api")
public class UserController {

    private final UserServicePort servicePort;
    private final UserRestMapper restMapper;

    @GetMapping("")
    public Flux<UserResponse> findAll() {
        return servicePort.findAll()
                .map(restMapper::toUserResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id) {
        return servicePort.findById(id)
                .map(user -> ResponseEntity.ok()
                        .body(restMapper.toUserResponse(user)));
    }

    @PostMapping("")
    public Mono<ResponseEntity<UserResponse>> register(@RequestBody UserRequest request) {
        return servicePort.save(restMapper.toUser(request))
                .map(account -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(restMapper.toUserResponse(account)));
    }


    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id,@RequestBody UpdateRequest request) {
        return servicePort.update(id, restMapper.toUser(request))
                .map(updatedUser -> ResponseEntity.ok()
                        .body(restMapper.toUserResponse(updatedUser)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return servicePort.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}