package com.propenine.siku.dtostok.request;

import com.propenine.siku.model.katalog.Kategori;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStokRequestDTO {
    private String namaKatalog;
    private Kategori kategori;
    private Integer stok;
    private Integer harga;
    private Boolean status;
}
