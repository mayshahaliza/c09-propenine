package com.propenine.siku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.propenine.siku.dtostok.ProductMapper;
import com.propenine.siku.dtostok.request.CreateProductRequestDTO;
import com.propenine.siku.servicestok.ProductService;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import com.propenine.siku.service.katalog.KategoriService;
import com.propenine.siku.model.User;
import com.propenine.siku.model.katalog.Kategori;

@SpringBootApplication
public class SikuApplication implements WebMvcConfigurer {

    @Autowired
    private KategoriService kategoriService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    public static void main(String[] args) {
        SpringApplication.run(SikuApplication.class, args);
    }

    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            // Cek apakah sudah ada kategori di database
            if (kategoriService.countData() == 0) {
                // Kategori manual yang akan disimpan
                String[] manualKategori = {
                        "Surgery",
                        "Urologi",
                        "Anesthesia",
                        "Obstetrics & Gynecology"
                };

                // Simpan setiap kategori ke dalam database
                for (String namaKategori : manualKategori) {
                    Kategori kategori = new Kategori();
                    kategori.setNamaKategori(namaKategori);
                    kategoriService.saveKategori(kategori);
                }
            }
        };
    }
}
