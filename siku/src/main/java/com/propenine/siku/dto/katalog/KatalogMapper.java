package com.propenine.siku.dto.katalog;
import javax.xml.catalog.Catalog;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.propenine.siku.dto.katalog.request.CreateKatalogRequestDTO;
import com.propenine.siku.dto.katalog.request.UpdateKatalogRequestDTO;
import com.propenine.siku.model.katalog.Katalog;


@Mapper(componentModel = "spring")
public interface KatalogMapper {
    @Mapping(target = "id", ignore = true)
    Katalog createKatalogRequestDTOToKatalog(CreateKatalogRequestDTO createKatalogRequestDTO);

    @Mapping(target = "id", ignore = true)
    UpdateKatalogRequestDTO katalogToUpdateKatalogRequestDTO(Katalog katalog);

    @Mapping(target = "id", ignore = true)
    Katalog updateKatalogRequestDTOToCatalog(UpdateKatalogRequestDTO updateKatalogRequestDTO);
}
