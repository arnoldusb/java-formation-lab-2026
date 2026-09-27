package com.indra.retail.orders.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.indra.retail.orders.model.Order;
import com.indra.retail.orders.web.dto.request.CreateOrderRequest;
import com.indra.retail.orders.web.dto.response.OrderResponse;
import com.indra.retail.orders.web.mapper.OrderMapper;

@Service
public class OrderService {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final OrderMapper orderMapper;
    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public OrderResponse create(CreateOrderRequest createOrder) {
        Order order = orderMapper.toOrder(createOrder);
        orders.put(order.getId(), order);
        
        return orderMapper.toOrderResponse(order);
    }

    public OrderResponse findById(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return orderMapper.toOrderResponse(order);
    }
}
