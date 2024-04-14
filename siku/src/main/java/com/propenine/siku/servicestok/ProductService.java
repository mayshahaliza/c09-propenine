package com.propenine.siku.servicestok;
import com.propenine.siku.modelstok.Product;
import com.propenine.siku.repositorystok.ProductDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    ProductDb productDb;
    
    public List<Product> getAllProduct() {
        return productDb.findAll();
    }

    public Product createProduct(Product product) {
        return productDb.save(product);
    }

    public Product updateProduct(Product product) {
        return productDb.save(product);
    }

    public Product getProductById(UUID idProduct) {
        return productDb.findById(idProduct).get();
    }
    public List<Product> getProductsByCategory(Long kategoriId) {
        return productDb.findByKategoriId(kategoriId);
    }
    
}
