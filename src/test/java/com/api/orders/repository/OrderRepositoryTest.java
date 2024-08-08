package com.api.orders.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.api.orders.model.Customer;
import com.api.orders.model.Order;
import org.aspectj.weaver.ast.Or;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @Test
    public void OrderRepository_SaveOrder_ReturnSavedOrder() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isGreaterThan(0);

    }

    @Test
    public void OrderRepository_GetAll_ReturnMoreThanOneOrder() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        Order order1 = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(24000))
                .customerId(1L)
                .build();

        orderRepository.save(order);
        orderRepository.save(order1);

        List<Order> orderList = orderRepository.findAll();

        assertThat(orderList).isNotNull();
        assertThat(orderList.size()).isGreaterThan(1);
    }

    @Test
    public void OrderRepository_FindById_ReturnOrder() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        orderRepository.save(order);

        Order savedOrder = orderRepository.findById(order.getId()).get();

        assertThat(savedOrder).isNotNull();
    }

    @Test
    public void OrderRepository_FindByCustomer_ReturnListOrder() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        orderRepository.save(order);

        Order orderList = orderRepository.findOrderByCustomerId(order.getCustomerId()).getFirst();

        assertThat(orderList).isNotNull();
        assertThat(orderList.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test for update order")
    public void OrderRepository_UpdateOrder_ReturnOrder() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        orderRepository.save(order);

        Order updatedOrder = orderRepository.findById(order.getId()).get();

        updatedOrder.setTotalAmount(BigDecimal.valueOf(15000));

        orderRepository.save(updatedOrder);

        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getTotalAmount()).isEqualTo(BigDecimal.valueOf(15000));

    }

    @Test
    @DisplayName("Test for delete customer")
    public void OrderRepository_DeleteOrder_ReturnEmptyOrder() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        orderRepository.save(order);
        orderRepository.deleteById(order.getId());

        Optional<Order> orderReturn = orderRepository.findById(order.getId());

        assertThat(orderReturn).isEmpty();
    }
}
