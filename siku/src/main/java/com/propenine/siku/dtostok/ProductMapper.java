package com.propenine.siku.dtostok;
import com.propenine.siku.dtostok.request.CreateProductRequestDTO;
import com.propenine.siku.dtostok.request.UpdateProductRequestDTO;
import com.propenine.siku.modelstok.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product createProductRequestDTOToProduct(CreateProductRequestDTO createProductRequestDTO);
    Product updateProductRequestDTOToProduct(UpdateProductRequestDTO updateProductRequestDTO);
    UpdateProductRequestDTO productToUpdateProductRequestDTO(Product product);
}
