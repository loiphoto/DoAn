package com.example.doan.Service;

import com.example.doan.Entity.Category;
import com.example.doan.Repository.CategoryRepository;
import com.example.doan.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void deleteById(Long id) throws CategoryNotFoundException {
        Long count = categoryRepository.countById(id);
        if (count == null || count == 0) {
            throw new CategoryNotFoundException("Could not find any categories with ID" + id);
        }

        categoryRepository.deleteById(id);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category getCategoryById(Long id) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return category.get();
        }
        throw new CategoryNotFoundException("Could not find any users with ID" + id);
    }

    public Optional<Category> findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }
}
