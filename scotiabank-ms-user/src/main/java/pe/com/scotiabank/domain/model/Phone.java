package pe.com.scotiabank.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone implements Serializable {

    @Serial
    private static final long serialVersionUID = -3756090898223056681L;

    private String id;
    @JsonIgnore
    @Column("user_id")
    private String userId;
    private String number;
    private String cityCode;
    private String countryCode;


    @Transient
    @JsonIgnore
    private Boolean isNewEntry;
}
