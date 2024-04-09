package com.propenine.siku.dtostok.request;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.propenine.siku.model.katalog.Kategori;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProductRequestDTO extends CreateProductRequestDTO {
    @NotNull
    private UUID idProduct;
}
