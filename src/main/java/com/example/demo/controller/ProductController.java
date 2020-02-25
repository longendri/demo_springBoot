package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/product")
@RestController
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product, @RequestParam(name = "length", defaultValue = "0") long length){
        return productService.addProduct(product, length);
    }

    @DeleteMapping(path = "{id}")
    public void deleteProduct(@PathVariable("id") long id){
        productService.deleteProductById(id);
    }

    @PutMapping(path = "{id}")
    public Product updateProduct(@PathVariable("id") long id, @RequestBody Product product){
        return productService.updateProduct(id, product).orElse( null);
    }

    @GetMapping(path = "{id}")
    public Product getProductById(@PathVariable("id") long id){
        return productService.getProductById(id).orElse(null);
    }

    @GetMapping
    public Iterable<Product> getAllProduct(){
        return productService.getAllProduct();
    }

}
