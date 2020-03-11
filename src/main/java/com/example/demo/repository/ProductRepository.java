package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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

    @Transactional
    @Modifying
    @Query("UPDATE Product p set p.numberOfViews = p.numberOfViews + 1 WHERE p.id = :id")
    int updateNumberOfViews(@Param("id") long id);

}
