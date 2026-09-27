package com.indra.retail.orders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indra.retail.orders.service.OrderNotFoundException;
import com.indra.retail.orders.service.OrderService;
import com.indra.retail.orders.web.OrderController;
import com.indra.retail.orders.web.dto.request.CreateOrderItemRequest;
import com.indra.retail.orders.web.dto.request.CreateOrderRequest;
import com.indra.retail.orders.web.dto.response.OrderResponse;
import com.indra.retail.orders.web.exception.GlobalExceptionHandler;

@WebMvcTest(OrderController.class)
@Import(GlobalExceptionHandler.class)
class OrderServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void createOrder_shouldReturn201_whenRequestIsValid() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(
                "customer-001",
                List.of(new CreateOrderItemRequest("SKU-001", 2, 19.99)),
                "Calle 123 # 45-67");

        OrderResponse response = new OrderResponse("ORD-001", "NEW", 39.98, "2026-09-30");

        when(orderService.create(any(CreateOrderRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value("ORD-001"))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.totalAmount").value(39.98));
    }

    @Test
    void createOrder_shouldReturn400_whenRequestIsInvalid() throws Exception {
        String invalidJson = """
                {
                  "customerId": "",
                  "items": [
                    {
                      "sku": "",
                      "quantity": 0,
                      "unitPrice": -1
                    }
                  ],
                  "deliveryAddress": "abc"
                }
                """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void getOrder_shouldReturn200_whenOrderExists() throws Exception {
        OrderResponse response = new OrderResponse("ORD-001", "NEW", 39.98, "2026-09-30");

        when(orderService.findById("ORD-001")).thenReturn(response);

        mockMvc.perform(get("/api/orders/ORD-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("ORD-001"))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void getOrder_shouldReturn404_whenOrderDoesNotExist() throws Exception {
        when(orderService.findById("missing-order"))
                .thenThrow(new OrderNotFoundException("missing-order"));

        mockMvc.perform(get("/api/orders/missing-order"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errors[0]").value("Pedido no encontrado: missing-order"));
    }
}
