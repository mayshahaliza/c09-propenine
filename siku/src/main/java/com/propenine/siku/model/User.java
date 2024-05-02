package com.propenine.siku.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.hibernate.annotations.SQLDelete;
// import org.hibernate.annotations.WhereClause;
import org.hibernate.annotations.Where;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
@SQLDelete(sql = "UPDATE \"user\" SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama depan harus diisi")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Nama depan harus berisi huruf saja")
    @Size(min = 1, max = 255, message = "Panjang nama depan antara 1-255 karakter")
    @Column(name = "nama_depan", nullable = false)
    private String nama_depan;

    @NotBlank(message = "Nama belakang harus diisi")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Nama belakang harus berisi huruf saja")
    @Size(min = 1, max = 255, message = "Panjang nama belakang antara 1-255 karakter")
    @Column(name = "nama_belakang", nullable = false)
    private String nama_belakang;

    @NotBlank(message = "Email harus diisi")
    @Email(message = "Format email tidak valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @NotNull(message = "Nomor hp harus diisi")
    @Pattern(regexp = "\\d+", message = "Nomor hp harus berisi angka saja")
    @Column(name = "nomor_hp", nullable = false)
    private String nomor_hp;

    @NotBlank(message = "Alamat harus diisi")
    @Size(min = 1, max = 255, message = "Panjang alamat antara 1-255 karakter")
    @Column(name = "alamat", nullable = false)
    private String alamat;

    @NotNull(message = "Tanggal lahir harus diisi")
    @Past(message = "Tanggal lahir harus kurang dari hari ini")
    @Column(name = "tanggal_lahir", nullable = false)
    private LocalDate tanggal_lahir;

    @NotBlank(message = "Tempat lahir harus diisi")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Tempat lahir harus berisi huruf dan spasi saja")
    @Size(min = 1, max = 255, message = "Panjang tempat lahir antara 1-255 karakter")
    @Column(name = "tempat_lahir", nullable = false)
    private String tempat_lahir;

    @NotBlank(message = "Role harus diisi")
    @Column(name = "role", nullable = false)
    private String role;

    @NotNull(message = "Status karyawan harus diisi")
    @Column(name = "status_karyawan", nullable = false)
    private Boolean status_karyawan;

    @NotBlank(message = "Username harus diisi")
    @Size(min = 1, max = 255, message = "Panjang username antara 1-255 karakter")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password harus diisi")
    @Size(min = 8, message = "Panjang password minimal 8 karakter")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;
}

