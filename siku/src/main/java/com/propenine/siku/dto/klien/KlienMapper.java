package com.propenine.siku.dto.klien;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.propenine.siku.model.Klien;



@Mapper(componentModel = "spring")
public interface KlienMapper {
    @Mapping(target = "id", ignore = true)
    Klien createKlienRequestDTOToKlien(CreateKlienRequestDTO createKlienRequestDTO);

    @Mapping(target = "id", ignore = true)
    UpdateKlienRequestDTO klienToUpdateKlienRequestDTO(Klien klien);

    // @Mapping(target = "id", ignore = true)
    // Katalog updateKatalogRequestDTOToCatalog(UpdateKatalogRequestDTO updateKatalogRequestDTO);
}
