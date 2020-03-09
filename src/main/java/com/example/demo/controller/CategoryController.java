package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public Category addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @GetMapping("categories/{id}")
    public Category getProductById(@PathVariable("id") String id){
        return categoryService.findById(id).orElse(null);
    }

    @GetMapping("/categories")
    public Iterable<Category> getAllProduct(){
        return categoryService.getAllCategories();
    }

    @DeleteMapping(path = "/categories/{id}")
    public void deleteCategory(@PathVariable("id") String id) throws Exception {
        categoryService.deleteCategoryById(id);
    }
}
