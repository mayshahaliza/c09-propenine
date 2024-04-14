package com.propenine.siku.dtostok.request;

import com.propenine.siku.model.katalog.Kategori;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequestDTO {
    private String namaProduct;
    private Kategori kategori;
    private Integer stok;
    private Integer harga;
    private Boolean status;
    private String deskripsi;
    private String image;
}
