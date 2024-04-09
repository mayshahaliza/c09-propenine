package com.propenine.siku.service;

import java.util.List;

import com.propenine.siku.model.User;

import lombok.var;

public interface KaryawanService {
    
    public List<User> getAllKaryawan();

    public User getUserById(Long id);

    public User editKaryawan(User user);

    List<User> findByRoleContainingIgnoreCase(String role);

    public List<User> searchKaryawanByNama(String namaDepan, String namaBelakang) ;

    // public void deleteKaryawan(User karyawan);
}
