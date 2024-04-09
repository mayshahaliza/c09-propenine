package com.propenine.siku.dto.karyawan.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadKaryawanResponseDTO {
    private Long id;
    private String namaDepan;
    private String namaBelakang;
    private String email;
    private String nomorHp;
    private String alamat;
    private LocalDate tanggalLahir;
    private String tempatLahir;
    private String role;
    private Boolean statusKaryawan;
    private String username;
}
