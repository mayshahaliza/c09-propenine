package com.propenine.siku.dtostok;
import com.propenine.siku.dtostok.request.CreateStokRequestDTO;
import com.propenine.siku.dtostok.request.UpdateStokRequestDTO;
import com.propenine.siku.modelstok.Stok;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StokMapper {
    Stok createStokRequestDTOToStok(CreateStokRequestDTO createStokRequestDTO);
    Stok updateStokRequestDTOToStok(UpdateStokRequestDTO updateStokRequestDTO);
    UpdateStokRequestDTO stokToUpdateStokRequestDTO(Stok stok);
}
