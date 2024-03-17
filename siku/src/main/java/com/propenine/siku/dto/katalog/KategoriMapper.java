package com.propenine.siku.dto.katalog;
import org.mapstruct.Mapper;

import com.propenine.siku.dto.katalog.request.CreateKategoriRequestDTO;
import com.propenine.siku.model.katalog.Kategori;

@Mapper(componentModel = "spring")
public interface KategoriMapper {
    Kategori createKategoriRequestDTOToKategori(CreateKategoriRequestDTO createKategoriRequestDTO);
}
