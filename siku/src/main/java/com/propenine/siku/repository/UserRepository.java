package com.propenine.siku.repository;

import com.propenine.siku.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.Long;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Untuk Cek saat Edit Profile
    boolean existsByUsernameAndIdNot(String username, Long userId);
    boolean existsByEmailAndIdNot(String email, Long userId);
}
