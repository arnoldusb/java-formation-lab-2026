package com.indra.retail.orders.web.mapper;

import java.util.List;

import com.indra.retail.orders.model.OrderItem;
import com.indra.retail.orders.web.dto.request.CreateOrderItemRequest;
import org.springframework.stereotype.Component;


@Component
public class OrderItemMapper {

    public List<OrderItem> toOrderItem(List<CreateOrderItemRequest> createOrderItemRequest) {
        return createOrderItemRequest.stream().map(item -> new OrderItem(item.sku(), item.quantity(), item.unitPrice()))
                .toList();
    }

    public OrderItem toOrderItem(CreateOrderItemRequest createOrderItemRequest) {
        return new OrderItem(createOrderItemRequest.sku(), createOrderItemRequest.quantity(),
                createOrderItemRequest.unitPrice());
    }

}
