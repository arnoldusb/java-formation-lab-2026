package com.indra.retail.manager;

import com.indra.retail.entities.Customer;
import com.indra.retail.entities.Order;
import com.indra.retail.entities.OrderItem;
import com.indra.retail.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.LazyInitializationException;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LazyLoadingTest {

    @Autowired
    private EntityManagerRecordManager manager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private Long orderId;
    private Long itemId;
    private Long productId;

    @BeforeEach
    void createOrderWithItems() {
        orderId = transactionTemplate.execute(status -> {
            Customer customer = manager.create(new Customer("Carla", UUID.randomUUID() + "@indra.com"));
            Product product = manager.create(new Product("Laptop", new BigDecimal("1500.00"), 4));
            Order order = new Order(customer);
            OrderItem item = new OrderItem(product, 1, product.getPrice());
            order.addItem(item);
            manager.create(order);
            itemId = item.getId();
            productId = product.getId();
            return order.getId();
        });
    }

    @Test
    @DisplayName("Acceder a items fuera de la transacción lanza LazyInitializationException")
    void shouldThrowLazyInitializationExceptionOutsideTransaction() {
        // findById abre y cierra su transacción: la Order sale DETACHED
        Order order = manager.findById(Order.class, orderId).orElseThrow();

        assertThrows(LazyInitializationException.class, () -> order.getItems().size());
    }

    @Test
    @DisplayName("Acceder a items dentro de una transacción activa funciona")
    void shouldLoadItemsInsideTransaction() {
        int itemCount = transactionTemplate.execute(status -> {
            Order order = manager.findById(Order.class, orderId).orElseThrow();
            return order.getItems().size();
        });

        assertEquals(1, itemCount);
    }

    @Test
    @DisplayName("Customer de un Order no se carga hasta que se usa (LAZY)")
    void shouldLoadCustomerLazily() {
        Order detachedOrder = manager.findById(Order.class, orderId).orElseThrow();

        assertThrows(LazyInitializationException.class, () -> detachedOrder.getCustomer().getName());

        String customerName = transactionTemplate.execute(status ->
                manager.findById(Order.class, orderId).orElseThrow().getCustomer().getName());
        assertEquals("Carla", customerName);
    }

    @Test
    @DisplayName("Quitar un ítem de la lista elimina la fila (orphanRemoval)")
    void shouldRemoveOrphanItem() {
        transactionTemplate.executeWithoutResult(status -> {
            Order order = entityManager.find(Order.class, orderId);
            order.removeItem(order.getItems().get(0));
            entityManager.flush();
        });

        assertFalse(manager.findById(OrderItem.class, itemId).isPresent());
    }

    @Test
    @DisplayName("No se puede guardar un OrderItem sin Order")
    void shouldRejectOrphanOrderItem() {
        Product product = manager.findById(Product.class, productId).orElseThrow();

        assertThrows(PropertyValueException.class, () ->
                transactionTemplate.executeWithoutResult(status -> {
                    entityManager.persist(new OrderItem(product, 1, product.getPrice()));
                    entityManager.flush();
                }));
    }

    @Test
    @DisplayName("Borrar un OrderItem no borra el Product")
    void shouldNotDeleteProductWhenDeletingItem() {
        manager.delete(OrderItem.class, itemId);

        assertTrue(manager.findById(Product.class, productId).isPresent());
    }
}
