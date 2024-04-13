package com.propenine.siku.service;

import java.util.List;

import com.propenine.siku.model.User;


public interface KaryawanService {
    
    public List<User> getAllKaryawan();

    public User getUserById(Long id);

    public void editKaryawan(User user);

    public List<User> findByRoleContainingIgnoreCase(String role);

    public List<User> searchByName(String name);

    public void deleteKaryawan(User karyawan);
}
