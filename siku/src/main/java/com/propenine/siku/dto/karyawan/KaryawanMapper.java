package com.propenine.siku.dto.karyawan;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.propenine.siku.dto.karyawan.request.EditKaryawanReqDTO;
import com.propenine.siku.dto.karyawan.response.ReadKaryawanResponseDTO;
import com.propenine.siku.model.User;

@Mapper(componentModel = "spring")
public interface KaryawanMapper {

    User editKaryawanRequestDTOToKaryawan(EditKaryawanReqDTO editKaryawanReqDTO);

    EditKaryawanReqDTO karyawanToEditKaryawanRequestDTO(User user);

    ReadKaryawanResponseDTO karyawanToReadKaryawanResponseDTO(User user);
    
}
