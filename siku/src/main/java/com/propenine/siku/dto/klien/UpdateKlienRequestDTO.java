package com.propenine.siku.dto.klien;
import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Data
public class UpdateKlienRequestDTO extends CreateKlienRequestDTO {
    // @NotNull
    // private UUID id ;
}
