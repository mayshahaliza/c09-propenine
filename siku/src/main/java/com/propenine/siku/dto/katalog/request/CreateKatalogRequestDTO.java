package com.propenine.siku.dto.katalog.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.propenine.siku.model.katalog.Kategori;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateKatalogRequestDTO {
    private UUID id ;

    @NotBlank(message = "Nama katalog tidak boleh kosong")
    private String namaKatalog;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    private String deskripsi;

    @NotNull(message = "Harga tidak boleh kosong")
    @Min(value = 0, message = "Harga harus lebih besar dari atau sama dengan 0")
    private Integer harga;

    private String image;
    
    @NotNull(message = "Harap masukkan kategori")
    private Kategori kategori;
}
