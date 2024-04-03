package com.propenine.siku.dto.klien;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateKlienRequestDTO {
    private UUID id;
    private String namaKlien;
    private String emailKlien;
    private String noHpKlien;
    private String alamatKlien;
}
