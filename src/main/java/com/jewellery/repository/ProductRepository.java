package com.jewellery.repository;
import com.jewellery.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface ProductRepository extends MongoRepository<Product,String> {
    List<Product> findByNewCollectionTrue();
    List<Product> findByTrendingTrue();
    List<Product> findTop6ByOrderBySalesCountDesc();
    List<Product> findByCategoryIgnoreCase(String category);
}