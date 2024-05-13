package com.propenine.siku.runner;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.propenine.siku.model.User;
import com.propenine.siku.repository.UserRepository;

@Component
public class AdminRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cek apakah sudah ada admin dalam database
        if (!userRepository.existsByUsername("admin12")) {
            // Buat objek user admin baru
            User admin = new User();
            admin.setNama_depan("admin");
            admin.setNama_belakang("admin");
            admin.setEmail("admin12@gmail.com");
            admin.setNomor_hp("08123456789");
            admin.setAlamat("Jalan Contoh No. 123");
            admin.setTanggal_lahir(LocalDate.of(1990, 1, 1));
            admin.setTempat_lahir("Jakarta");
            admin.setRole("admin");
            admin.setStatus_karyawan(true); // Aktif
            admin.setUsername("admin12");

            // Hash password menggunakan BCryptPasswordEncoder
            admin.setPassword("rahasia12");
            System.out.println("admin generated");

            // Simpan user admin ke dalam database
            userRepository.save(admin);
        }
    }

}
