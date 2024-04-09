package com.propenine.siku.repository;

import com.propenine.siku.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface KaryawanRepository extends JpaRepository<User, Long> {

    // Mencari pengguna berdasarkan nama yang mengandung string yang diberikan (ignore case)
    @Query("SELECT u FROM User u WHERE CONCAT(u.nama_depan, ' ', u.nama_belakang) LIKE %:name%")
    List<User> findByNamaContainingIgnoreCase(@Param("name") String name);
    
    // Query untuk mendapatkan semua pengguna yang diurutkan berdasarkan penggabungan nama depan dan belakang mereka dalam huruf kecil
    @Query("SELECT u FROM User u ORDER BY LOWER(CONCAT(u.nama_depan, ' ', u.nama_belakang))")
    List<User> allKaryawanSorted();

    List<User> findByRoleContainingIgnoreCase(String role);
}

