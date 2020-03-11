package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public Product addProduct(@RequestParam(name = "type") String type, @RequestBody Product product, @RequestParam(name = "length", defaultValue = "0") long length) throws Exception {
        return productService.addProduct(type, product, length);
    }

    @DeleteMapping(path = "/products/{id}")
    public void deleteProduct(@PathVariable("id") long id){
        productService.deleteProductById(id);
    }

    @PutMapping(path = "/products/{id}")
    public Product updateProduct(@PathVariable("id") long id, @RequestBody Product product){
        return productService.updateProduct(id, product).orElse( null);
    }

    @PatchMapping(path = "/products/updateNumberOfViews/{id}")
    public void updateNumberOfViews(@PathVariable("id") long id){
        productService.updateNumberOfViews(id);
    }

    @PatchMapping(path = "/products/resetNumberOfViews/{id}")
    public void resetNumberOfViews(@PathVariable("id") long id){
        productService.resetNumberOfViews(id);
    }

    @GetMapping("products/{id}")
    public Product getProductById(@PathVariable("id") long id){
        return productService.getProductById(id).orElse(null);
    }

    @GetMapping("/products")
    public Iterable<Product> getAllProduct(){
        return productService.getAllProduct();
    }

    @GetMapping("/products/byNumberOfViews")
    public List<Product> getProductsByOrderByNumberOfViews(@RequestParam(name = "direction", defaultValue = "ASC") String direction) throws Exception {
        return productService.getProductsByOrderByNumberOfViews(direction);
    }

    @GetMapping("/products/findByNumberOfViews/{number}")
    public List<Product> getProductsByNumberOfViews(@PathVariable(name = "number") int number)  {
        return productService.findByNumberOfViews(number);
    }

    @GetMapping("/products/byReleaseDate")
    public List<Product> getProductsByOrderByReleaseDate(@RequestParam(name = "direction", defaultValue = "ASC") String direction) throws Exception {
        return productService.getProductsByOrderByReleaseDate(direction);
    }

    @GetMapping("/products/findByReleaseDate/{date}")
    public List<Product> getProductsByNumberOfViews(@PathVariable(name = "date") String date) throws ParseException {
        return productService.findByReleaseDate(date);
    }

}
