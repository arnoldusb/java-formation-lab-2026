package com.indra.retail.orders.web.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.indra.retail.orders.model.OrderItem;
import com.indra.retail.orders.web.dto.request.CreateOrderItemRequest;

class OrderItemMapperTest {

    private final OrderItemMapper mapper = new OrderItemMapper();

    @Test
    void toOrderItem_shouldMapSingleRequest() {
        CreateOrderItemRequest request = new CreateOrderItemRequest("SKU-001", 2, 19.99);

        OrderItem item = mapper.toOrderItem(request);

        assertNotNull(item);
        assertEquals("SKU-001", item.getSku());
        assertEquals(2, item.getQuantity());
        assertEquals(19.99, item.getUnitPrice());
    }

    @Test
    void toOrderItem_shouldMapListOfRequests() {
        List<CreateOrderItemRequest> requests = List.of(
                new CreateOrderItemRequest("SKU-001", 2, 19.99),
                new CreateOrderItemRequest("SKU-002", 1, 12.5)
        );

        List<OrderItem> items = mapper.toOrderItem(requests);

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("SKU-001", items.get(0).getSku());
        assertEquals(2, items.get(0).getQuantity());
        assertEquals(19.99, items.get(0).getUnitPrice());
        assertEquals("SKU-002", items.get(1).getSku());
        assertEquals(1, items.get(1).getQuantity());
        assertEquals(12.5, items.get(1).getUnitPrice());
    }
}
