package com.example.demo;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productServ;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void testAddProduct(){
        //given
        Category category = new Category("Trailer TV", true);
        Product product = new Product("Five Feed Apart", "", category, new Date(), 3 );

        //when
        Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        Product saved = null;
        try {
            saved = productServ.addProduct(category.getName(), product, 3600);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Then
        assertThat(saved).isNotNull();
        assertEquals("FFA", saved.getAbbreviation());
        assertEquals("01:00:00", saved.getFormat());
    }

    @Test
    public void testUpdateProduct(){
        //given
        Category category = new Category("Trailer TV", false);
        Product product = new Product("Product 1", "pr1", category, new Date(), 3 );
        product.setId((long)1);
        product.setName("Spiderman the invisible men");
        product.setAbbreviation("");

        //when
        Mockito.when(productRepository.findById((long)1)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Optional<Product> saved = productServ.updateProduct(product.getId(), product);

        //then
        assertEquals("STIM", saved.get().getAbbreviation());
    }

}
