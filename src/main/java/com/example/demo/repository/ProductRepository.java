package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByNumberOfViews(int number);
    List<Product> findByOrderByNumberOfViewsAsc();
    List<Product> findByOrderByNumberOfViewsDesc();

    List<Product> findByReleaseDate(Date date);
    List<Product> findByOrderByReleaseDateAsc();
    List<Product> findByOrderByReleaseDateDesc();

}
