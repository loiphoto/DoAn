package com.example.doan.Repository;

import com.example.doan.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);

    Long countById(Long id);

    Optional<Category> findCategoryByName(String name);

}
