package com.example.demo;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.controller.ProductController;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import static org.hamcrest.CoreMatchers.is;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;


    @Test
    public void testGetAllProducts() throws Exception {
        //given
        Category category = new Category("Trailer TV", false);
        Product product1 = new Product("Product 1", "pr1", category, new Date(), 3 );
        Product product2 = new Product("Product 2", "pr2", category, new Date(), 5 );

        List<Product> products = Arrays.asList(product1, product2);
        //when
        when(productService.getAllProduct()).thenReturn(products);

        //then
        mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(product1.getName())));
    }

    @Test
    public void testAddProduct() throws Exception {
        //given
        Category category = new Category("Trailer TV", true);
        Product product1 = new Product("Product 1", "pr1", category, new Date(), 3 );

        //when
        when(categoryService.findById(category.getName())).thenReturn(Optional.of(category));
        when(productService.addProduct(category.getName(), product1, 0)).thenReturn(product1);

        //then
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/products")
                .param("type", "Trailer TV")
                .param("length", "3600")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateProduct() throws Exception{
        //given
        Category category = new Category("Trailer TV", false);
        Product product1 = new Product("Product 1", "pr1", category, new Date(), 3 );
        product1.setId((long)1);

        //when
        when(productService.updateProduct(product1.getId(), product1)).thenReturn(Optional.of(product1));

        //then
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                .put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk());
    }


    public void testDeleteProduct() throws Exception {
        //given
        Category category = new Category("Trailer TV", false);
        Product product1 = new Product("Product 1", "pr1", category, new Date(), 3 );
        product1.setId((long)1);


        //then
        mockMvc.perform( MockMvcRequestBuilders.delete("/products/{id}", 1) )
                .andExpect(status().isAccepted());
    }

}
