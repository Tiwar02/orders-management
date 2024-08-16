package com.api.orders.service;

import com.api.orders.model.Customer;
import com.api.orders.model.Order;
import com.api.orders.repository.CustomerRepository;
import com.api.orders.repository.OrderRepository;

import com.api.orders.service.impl.CustomerServiceImpl;
import com.api.orders.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Order order;
    private Customer customer;
    @Test
    public void OrderService_CreateOrder_ReturnsCreatedOrder() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.createOrder(order);

        assertThat(savedOrder).isNotNull();
    }

    @Test
    public void OrderService_GetAllOrders_ReturnsOrders() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> savedOrder = orderService.getAllOrders();

        assertThat(savedOrder).isNotNull();
    }

    @Test
    public void OrderService_GetOrderById_ReturnOrder() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));

        Order savedOrder = orderService.getOrderById(1L);

        assertThat(savedOrder).isNotNull();
    }

    @Test
    public void OrderService_UpdateOrder_ReturnOrder() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .id(1L)
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        given(orderRepository.findById(order.getId())).willReturn(Optional.ofNullable(order));
        given(orderRepository.save(order)).willReturn(order);

        Order updatedOrder = orderService.updateOrder(order);

        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getCustomerId()).isEqualTo(1L);
    }

    @Test
    public void OrderService_DeleteOrderById_ReturnStatus() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .id(1L)
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        given(orderRepository.findById(order.getId())).willReturn(Optional.ofNullable(order));

        assertAll(() -> orderService.deleteOrder(1L));

    }

    @Test
    public void OrderService_GetOrdersByCustomerId_ReturnOrders() throws ParseException {
        Long customerId =1L;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        String dateInString = "7-Jun-2013";
        Date date = formatter.parse(dateInString);

        order = Order.builder()
                .orderDate(date)
                .totalAmount(BigDecimal.valueOf(12000))
                .customerId(1L)
                .build();

        customer = Customer.builder()
                .firstName("Emilio")
                .lastName("Perez")
                .email("emilio@gmail.com")
                .phone("3018997865")
                .build();

        when(orderRepository.findOrderByCustomerId(customerId)).thenReturn(Arrays.asList(order));


        List<Order> orderReturn = orderService.getOrderByCustomerId(1L);

        assertThat(orderReturn).isNotNull();


    }

}
