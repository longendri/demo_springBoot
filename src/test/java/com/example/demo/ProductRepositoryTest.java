package com.example.demo;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testAddProduct(){
        //given
        Category category = new Category("Trailer TV", false);
        Product product = new Product("Product 1", "pr1", category, new Date(), 3 );

        //when
        Product saved = productRepository.save(product);

        //then
        assertNotNull(saved.getId());
        assertNotNull(saved.getInsertDate());
    }

    @Test
    public void testUpdateProduct(){
        //given
        Category category = new Category("Trailer TV", false);
        Product product = new Product("Product 1", "pr1", category, new Date(), 3 );
        entityManager.persistAndFlush(category);
        product = entityManager.persist(product);
        entityManager.flush();

        //when
        product.setNumberOfViews(product.getNumberOfViews() + 1);
        productRepository.save(product);

        //then
        Product saved = entityManager.find(Product.class, product.getId());
        assertEquals(saved.getNumberOfViews(), product.getNumberOfViews());
    }

    @Test
    public void testDeleteProduct(){
        //given
        Category category = new Category("Trailer TV", false);
        Product product1 = new Product("Product 1", "pr1", category, new Date(), 3 );
        Product product2 = new Product("Product 2", "pr2", category, new Date(), 5 );
        entityManager.persistAndFlush(category);
        product1 = entityManager.persist(product1);
        product2 = entityManager.persist(product2);
        entityManager.flush();

        //when
        productRepository.delete(product2);

        //then
        assertThat(entityManager.find(Product.class, product2.getId())).isNull();
    }


    @Test
    public void testFindByNumberOfViews(){
        //given
        Category category = new Category("Trailer TV", false);
        Product product1 = new Product("Product 1", "pr1", category, new Date(), 3 );
        Product product2 = new Product("Product 2", "pr2", category, new Date(), 5 );
        entityManager.persistAndFlush(category);
        product1 = entityManager.persist(product1);
        product2 = entityManager.persist(product2);
        entityManager.flush();


        //when
        int numberOfViews = 3;
        List<Product> found = productRepository.findByNumberOfViews(numberOfViews);

        //then
        assertThat(found).isNotEmpty();
        assertTrue(found.stream().map(Product::getNumberOfViews).allMatch(number -> number.equals(numberOfViews)));
    }

    @Test
    public void testFindByReleaseDate(){
        //given
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Category category = new Category("Trailer TV", false);
        Product product1 = null;
        Product product2 = null;
        Product product3 = null;
        List<Product> products = null;
        try {
            product1 = new Product("Product 1", "pr1", category, df.parse("2020-03-02"), 1 );
            product2 = new Product("Product 2", "pr2", category, df.parse("2020-03-02"), 1 );
            product3 = new Product("Product 3", "pr3", category, df.parse("2019-02-01"), 1 );
            entityManager.persistAndFlush(category);
            entityManager.persist(product1);
            entityManager.persist(product2);
            entityManager.persist(product3);
            entityManager.flush();

            //when
            Date query = df.parse("2020-03-02");
            products = productRepository.findByReleaseDate(query);

            //then
            assertThat(products).isNotNull();
            assertEquals(products.size(), 2);
            assertThat(products.stream().map(Product::getReleaseDate).allMatch(date -> date.equals(query)));
        } catch (ParseException e) {
            e.printStackTrace();
            assertThat(products).isNotNull();
        }

    }

    @Test
    public void testFindByOrderByNumberOfViews(){
        //given
        Category category = new Category("Trailer TV", false);
        Product product1 = new Product("Product 1", "pr1", category, new Date(), 3 );
        Product product2 = new Product("Product 2", "pr2", category, new Date(), 5 );
        entityManager.persistAndFlush(category);
        product1 = entityManager.persist(product1);
        product2 = entityManager.persist(product2);
        entityManager.flush();

        //when
        Direction direction = Direction.DESC;
        List<Product> products;
        if (direction == Direction.ASC){
            products = productRepository.findByOrderByNumberOfViewsAsc();
        }
        else{
            products = productRepository.findByOrderByNumberOfViewsDesc();
        }

        //then
        assertThat(products).isNotEmpty();
        if(direction == Direction.ASC) {
            assertThat(products).containsSequence(product1, product2);
        }
        else {
            assertThat(products).containsSequence(product2, product1);
        }
    }

    @Test
    public void testFindByOrderByReleaseDate(){
        //given
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Category category = new Category("Trailer TV", false);
        Product product1 = null;
        Product product2 = null;
        List<Product> products = null;
        try {
            product1 = new Product("Product 1", "pr1", category, df.parse("2020-03-02"), 1 );
            product2 = new Product("Product 2", "pr2", category, df.parse("2019-03-02"), 1 );
            entityManager.persistAndFlush(category);
            entityManager.persist(product1);
            entityManager.persist(product2);
            entityManager.flush();

            //when
            Direction direction = Direction.DESC;
            if (direction == Direction.ASC){
                products = productRepository.findByOrderByReleaseDateAsc();
            }
            else{
                products = productRepository.findByOrderByReleaseDateDesc();
            }

            //then
            assertThat(products).isNotEmpty();
            if(direction == Direction.ASC) {
                assertThat(products).containsSequence(product2, product1);
            }
            else {
                assertThat(products).containsSequence(product1, product2);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            assertThat(products).isNotNull();
        }
    }

//    @Test
//    public void testUpdateNumberOfViews() throws ExecutionException, InterruptedException {
//        //given
//        Category category = new Category("Trailer TV", false);
//        Product product = new Product("Product 1", "pr1", category, new Date(), 0 );
//        entityManager.persistAndFlush(category);
//        product = entityManager.persistAndFlush(product);
//        entityManager.flush();
//
//        //when
//        int numberOfThreads = 10;
//        ExecutorService executorService  = Executors.newFixedThreadPool(numberOfThreads);
//        List<Future> futureList = new ArrayList<>(numberOfThreads);
//        long id = product.getId();
//        for(int thread=1; thread <= numberOfThreads; thread++){
//            futureList.add(executorService.submit(()-> productRepository.updateNumberOfViews(id)));
//        }
//        for(int thread=0; thread < numberOfThreads; thread++){
//            futureList.get(thread).get();
//        }
//        executorService.shutdown();
//
//        //then
//        product = productRepository.findById(id).get();
//        assertEquals(numberOfThreads, product.getNumberOfViews());
//    }

}
