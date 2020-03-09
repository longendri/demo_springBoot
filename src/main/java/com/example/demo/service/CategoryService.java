package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category addCategory(Category category){
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(String id){
        return categoryRepository.findById(id);
    }

    public void deleteCategoryById(String id) throws Exception {
        Optional<Category> cat = categoryRepository.findById(id);
        if(cat.isPresent()){
            Category category = cat.get();
            categoryRepository.delete(category);
        }else{
            throw new Exception(String.format("Category %s does not exist", id));
        }

    }

    public Iterable<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
