package com.indra.retail.orders.web.mapper;

import org.springframework.stereotype.Component;

import com.indra.retail.orders.model.Order;
import com.indra.retail.orders.web.dto.request.CreateOrderRequest;
import com.indra.retail.orders.web.dto.response.OrderResponse;

@Component
public class OrderMapper {

    private final OrderItemMapper createOrderItemMapper;

    public OrderMapper(OrderItemMapper createOrderItemMapper) {
        this.createOrderItemMapper = createOrderItemMapper;
    }

    public Order toOrder(CreateOrderRequest request) {
        return new Order(request.customerId(), createOrderItemMapper.toOrderItem(request.items()),
                request.deliveryAddress());

    }

    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(order.getId(),order.getStatus().name(), order.getTotalAmount(), order.getEstimatedDelivery().toString());
    }

}
