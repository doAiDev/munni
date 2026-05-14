package com.munni.repository;

import com.munni.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryOrderByCreatedAtDesc(Product.Category category);
    List<Product> findAllByOrderByCreatedAtDesc();
}
