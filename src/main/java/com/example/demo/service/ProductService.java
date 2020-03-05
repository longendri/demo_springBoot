package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product addProduct(Product product, long seconds){
        if (product.getAbbreviation() == null || product.getAbbreviation().isEmpty()){
            product.setAbbreviation(generateAbbreviation(product.getName()));
        }
        String format = "00:00:00";
        if (product.getType().hasLength()){
            int hours = (int) (seconds / 3600);
            int minutes = (int)((seconds - hours*3600) / 60);
            int sec = (int)(seconds - hours*3600 - minutes *60);
            format = String.format("%02d:%02d:%02d", hours, minutes, sec);
        }
        product.setFormat(format);
        //Check category
        Optional<Category> optCategory = categoryRepository.findById(product.getType().getName());
        if(optCategory.isPresent()){
            Category category = optCategory.get();
            category.setHasLength(product.getType().hasLength());
            product.setType(category);
        }
      return productRepository.save(product);
    }

    public Optional<Product> updateProduct(long id, Product newProduct){
        return productRepository.findById(id)
                .map(product -> {
                    if (newProduct.getAbbreviation() == null || newProduct.getAbbreviation().isEmpty()){
                        product.setAbbreviation(generateAbbreviation(newProduct.getName()));
                    }
                    product.setName(newProduct.getName());
                    product.setType(newProduct.getType());
                    product.setNumberOfViews(newProduct.getNumberOfViews());
                    product.setReleaseDate(newProduct.getReleaseDate());

                    return productRepository.save(product);
                });

    }

    public List<Product> getProductByNumberOfViews(int number){
        return productRepository.findByNumberOfViews(number);
    }

    private String generateAbbreviation(String name){
        StringTokenizer tokenizer = new StringTokenizer(name, " ");
        StringBuilder sb = new StringBuilder();
        if(tokenizer.countTokens() >=3){
            while (tokenizer.hasMoreElements()){
                sb.append(tokenizer.nextToken().charAt(0));
            }
        }else if(tokenizer.countTokens() == 2){
            char[] letters = new char[3];
            int cursor=0;
            String token = tokenizer.nextToken();
            for(int i=0; i<token.length() && cursor <2; i++) {
                letters[cursor++] = token.charAt(i);
            }
            token = tokenizer.nextToken();
            for(int i=0;i<token.length() && cursor <3; i++){
                letters[cursor++] = token.charAt(i);
            }
            sb.append(letters[0]);
            sb.append(letters[1]);
            sb.append(letters[2]);
        }else{
            String token = tokenizer.nextToken();
            sb.append(token.substring(0,Math.min(3, token.length())));
        }
        return sb.toString().toUpperCase();
    }

    public Optional<Product>  getProductById(long id){
        return productRepository.findById(id);
    }

    public void deleteProductById(long id){
        productRepository.deleteById(id);
    }

    public Iterable<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public List<Product> findByReleaseDate(String date) throws ParseException {
        return productRepository.findByReleaseDate(dateFormatter.parse(date));
    }

    public List<Product> findByNumberOfViews(int number){
        return productRepository.findByNumberOfViews(number);
    }

    public List<Product> getProductsByOrderByNumberOfViews(String direction) throws Exception {
        if (direction.toUpperCase().equals("ASC")){
            return productRepository.findByOrderByNumberOfViewsAsc();
        }else if(direction.toUpperCase().equals("DESC")){
            return productRepository.findByOrderByNumberOfViewsDesc();
        }
        throw new Exception("Unknown direction: " + direction);
    }

    public List<Product> getProductsByOrderByReleaseDate(String direction) throws Exception {
        if (direction.toUpperCase().equals("ASC")){
            return productRepository.findByOrderByReleaseDateAsc();
        }else if(direction.toUpperCase().equals("DESC")){
            return productRepository.findByOrderByReleaseDateDesc();
        }
        throw new Exception("Unknown direction: " + direction);
    }
}
