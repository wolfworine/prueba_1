    package pe.com.scotiabank.infrastructure.adapter.output.persistence.entity;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.data.annotation.Id;
    import org.springframework.data.annotation.Transient;
    import org.springframework.data.domain.Persistable;
    import org.springframework.data.relational.core.mapping.Column;
    import org.springframework.data.relational.core.mapping.Table;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import pe.com.scotiabank.infrastructure.adapter.input.rest.model.enums.RoleEnum;

    import java.time.LocalDateTime;
    import java.util.Collection;
    import java.util.List;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Table("tb_user")
    public class UserEntity implements Persistable<String>, UserDetails {

        @Id
        private String id;
        private String name;
        private String email;
        private String password;
        @Transient
        private List<PhoneEntity> phones;
        private LocalDateTime created;
        private LocalDateTime modified;
        private LocalDateTime lastLogin;
        private String token;
        @Column("is_active")
        private Boolean isActive;
        private RoleEnum role;

        @Transient
        @Builder.Default
        private boolean isNewEntry = true;

        @Override
        public boolean isNew() {
            return isNewEntry;
        }
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(("ROLE_" + role.name())));
        }

        @Override
        public String getUsername() {
            return email;
        }
    }