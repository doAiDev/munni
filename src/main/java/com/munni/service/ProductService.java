package com.munni.service;

import com.munni.model.Product;
import com.munni.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repo;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public List<Product> findByCategory(Product.Category category) {
        return repo.findByCategoryOrderByCreatedAtDesc(category);
    }

    public Product save(Product product, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path dir = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(dir);
            Files.copy(image.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            product.setImagePath("/uploads/" + filename);
        }
        return repo.save(product);
    }

    public void delete(Long id) {
        repo.findById(id).ifPresent(p -> {
            if (p.getImagePath() != null) {
                try {
                    Path file = Paths.get(uploadDir).toAbsolutePath()
                            .resolve(Paths.get(p.getImagePath()).getFileName());
                    Files.deleteIfExists(file);
                } catch (IOException ignored) {}
            }
            repo.delete(p);
        });
    }

    public Product findById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
