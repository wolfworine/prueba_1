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

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("tb_phone")
public class PhoneEntity  implements Persistable<String> {

    @Id
    private String id;
    @Column("user_id")
    private String userId;
    private String number;
    @Column("city_code")
    private String cityCode;
    @Column("country_code")
    private String countryCode;


    @Transient
    @Builder.Default
    private boolean isNewEntry = true;

    @Override
    public boolean isNew() {
        return isNewEntry;
    }
}
