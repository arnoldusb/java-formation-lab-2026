package com.indra.catalog.products.web;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.indra.catalog.products.application.ProductService;
import com.indra.catalog.products.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "EDITOR")
class ProductControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    // El precio debe ser > 0
    @Test
    void rejectNonPositivePrice() throws Exception {
        assertValidationError(
                """
                        {"name":"producto1","price":0,"stock":4}
                        """,
                "price must be greater than 0");
    }

    private void assertValidationError(String requestBody, String expectedError) throws Exception {
        ResultActions result = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors").value(org.hamcrest.Matchers.hasItem(expectedError)));

        verifyNoInteractions(productService);
    }
}
