package com.propenine.siku.dto.karyawan.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class EditKaryawanReqDTO {
    @NotBlank(message = "Nama depan harus diisi")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Nama depan harus berisi huruf saja")
    @Size(min = 1, max = 255, message = "Panjang nama depan antara 1-255 karakter")
    private String namaDepan;

    @NotBlank(message = "Nama belakang harus diisi")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Nama belakang harus berisi huruf saja")
    @Size(min = 1, max = 255, message = "Panjang nama belakang antara 1-255 karakter")
    private String namaBelakang;

    @NotBlank(message = "Email harus diisi")
    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "Nomor hp harus diisi")
    @Pattern(regexp = "\\d+", message = "Nomor hp harus berisi angka saja")
    private String nomorHp;

    @NotBlank(message = "Alamat harus diisi")
    @Size(min = 1, max = 255, message = "Panjang alamat antara 1-255 karakter")
    private String alamat;

    @NotNull(message = "Tanggal lahir harus diisi")
    @Past(message = "Tanggal lahir harus kurang dari hari ini")
    private LocalDate tanggalLahir;

    @NotBlank(message = "Tempat lahir harus diisi")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Tempat lahir harus berisi huruf saja")
    @Size(min = 1, max = 255, message = "Panjang tempat lahir antara 1-255 karakter")
    private String tempatLahir;

    @NotBlank(message = "Role harus diisi")
    private String role;

    @NotNull(message = "Status karyawan harus diisi")
    private Boolean statusKaryawan;

    @NotBlank(message = "Username harus diisi")
    @Size(min = 1, max = 255, message = "Panjang username antara 1-255 karakter")
    private String username;

    @NotBlank(message = "Password harus diisi")
    @Size(min = 8, message = "Panjang password minimal 8 karakter")
    private String password;
    
}
