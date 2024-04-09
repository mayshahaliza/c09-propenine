package com.propenine.siku.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.propenine.siku.model.User;
import com.propenine.siku.repository.KaryawanRepository;

@Service
public class KaryawanServiceImpl implements KaryawanService{
    
    @Autowired
    private KaryawanRepository karyawanRepository;

    @Override
    public List<User> getAllKaryawan() {
        return karyawanRepository.allKaryawanSorted();
    }

    @Override
    public User getUserById(Long id) {
    return karyawanRepository.findById(id).orElse(null);
    }

    @Override
    public User editKaryawan(User user) {
        return karyawanRepository.save(user);
    }

    @Override
    public List<User> findByRoleContainingIgnoreCase(String role) {
        return karyawanRepository.findByRoleContainingIgnoreCase(role);
    }

    // @Override
    // public User updateBuku(User bukuFromDto) {
    //     User karyawan = getBukuById(bukuFromDto.getId());
    //     if (karyawan != null){
    //         karyawan.setHarga(bukuFromDto.getHarga());
    //         karyawan.setJudul(bukuFromDto.getJudul());
    //         bukuDb.save(buku);
    //     }
    //     return buku;
    // }

    @Override
    public List<User> searchKaryawanByNama(String namaDepan, String namaBelakang) {
        if (namaDepan != null && namaBelakang != null) {
            return karyawanRepository.findByNamaDepanContainingIgnoreCaseOrNamaBelakangContainingIgnoreCase(namaDepan, namaBelakang);
        } else if (namaDepan != null) {
            return karyawanRepository.findByNamaDepanContainingIgnoreCase(namaDepan);
        } else if (namaBelakang != null) {
            return karyawanRepository.findByNamaBelakangContainingIgnoreCase(namaBelakang);
        } else {
            return karyawanRepository.findAll();
        }
    }

    // @Override
    // public void deleteKaryawan(User karyawan) {
    //     karyawanRepository.delete(karyawan);
    // }

}

