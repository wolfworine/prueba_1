package pe.com.scotiabank.infrastructure.adapter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pe.com.scotiabank.infrastructure.adapter.output.persistence.repository.UserRepository;
import reactor.core.publisher.Mono;

import static pe.com.scotiabank.utils.ErrorCatalog.USER_NOT_FOUND;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(USER_NOT_FOUND.getTitle())))
                .map(user -> User.builder()
                        .password(user.getPassword())
                        .username(user.getEmail())
                        .authorities(user.getAuthorities())
                        .build()
                );
    }

}
