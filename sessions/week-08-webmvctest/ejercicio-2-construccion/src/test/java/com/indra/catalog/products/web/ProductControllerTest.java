package com.indra.catalog.products.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.indra.catalog.products.application.CreateProductCommand;
import com.indra.catalog.products.application.ProductService;
import com.indra.catalog.products.domain.exception.ProductNotFoundException;
import com.indra.catalog.products.domain.model.Product;
import com.indra.catalog.products.infrastructure.security.SecurityConfig;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  // El nombre vacío devuelve 400
  @Test
  void shouldRejectInvalidProductCreation() throws Exception {
    mockMvc.perform(post("/api/products")
        .with(httpBasic("editor", "editor123"))
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "name": "",
              "price": 3500000.00,
              "stock": 10
            }
            """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
        .andExpect(jsonPath("$.errors[0]").value(containsString("name")));
  }

  // Producto inexistente devuelve 404
  @Test
  void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
    when(productService.findById("PRD-9999")).thenThrow(new ProductNotFoundException("PRD-9999"));

    mockMvc.perform(get("/api/products/PRD-9999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0]").value(containsString("PRD-9999")));
  }

  // `Location` apunta al recurso creado
  @Test
  void shouldCreateProduct() throws Exception {
    Product created = new Product("PRD-0001", "Laptop", new BigDecimal("3500000.00"), 10);
    CreateProductCommand command = new CreateProductCommand("Laptop", new BigDecimal("3500000.00"), 10);
    when(productService.create(command)).thenReturn(created);

    mockMvc.perform(post("/api/products")
        .with(httpBasic("editor", "editor123"))
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "name": "Laptop",
              "price": 3500000.00,
              "stock": 10
            }
            """))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/api/products/PRD-0001"))
        .andExpect(jsonPath("$.id").value("PRD-0001"))
        .andExpect(jsonPath("$.name").value("Laptop"));
  }

  // `DELETE` exige rol `ADMIN`
  @Test
  void shouldDeleteProduct() throws Exception {
    doNothing().when(productService).deleteById("PRD-0001");

    mockMvc.perform(delete("/api/products/PRD-0001")
        .with(httpBasic("admin", "admin123")))
        .andExpect(status().isNoContent());
  }

}
