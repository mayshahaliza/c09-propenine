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

    @NotBlank(message = "First name must be filled.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "First name must be letter only.")
    @Size(min = 1, max = 255, message = "Length first name must be between 1--255 character.")
    @Column(name = "nama_depan", nullable = false)
    private String nama_depan;

    @NotBlank(message = "Last name must be filled.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name must be letter only.")
    @Size(min = 1, max = 255, message = "Length last name must be between 1--255 character.")
    @Column(name = "nama_belakang", nullable = false)
    private String nama_belakang;

    @NotBlank(message = "Email must be filled.")
    @Email(message = "Email format must be valid.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @NotNull(message = "Phone number must be filled")
    @Pattern(regexp = "\\d+", message = "Phone number must be number only.")
    @Column(name = "nomor_hp", nullable = false)
    private String nomor_hp;

    @NotBlank(message = "Address must be filled.")
    @Size(min = 1, max = 255, message = "Length address must be between 1--255 character.")
    @Column(name = "alamat", nullable = false)
    private String alamat;

    @NotNull(message = "Birthdate must be filled.")
    @Past(message = "Birthdate must be less than today.")
    @Column(name = "tanggal_lahir", nullable = false)
    private LocalDate tanggal_lahir;

    @NotBlank(message = "Birth town must be filled.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Birth town must be filled with letters only.")
    @Size(min = 1, max = 255, message = "Length birth town must be between 1--255 character.")
    @Column(name = "tempat_lahir", nullable = false)
    private String tempat_lahir;

    @NotBlank(message = "Role must be filled.")
    @Column(name = "role", nullable = false)
    private String role;

    @NotNull(message = "Employee status must be filled.")
    @Column(name = "status_karyawan", nullable = false)
    private Boolean status_karyawan;

    @NotBlank(message = "Username must be filled.")
    @Size(min = 1, max = 255, message = "Length username must be between 1--255 character.")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password must be filled.")
    @Size(min = 8, message = "Password minimal 8 character.")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;
}
