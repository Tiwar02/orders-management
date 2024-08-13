package com.api.orders.service.impl;

import com.api.orders.exception.RequestException;
import com.api.orders.model.Order;
import com.api.orders.repository.OrderRepository;
import com.api.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;


    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Order not found ID: " + id));
    }

    public Order createOrder(Order order){

        BigDecimal big0 = new BigDecimal("0");
        int price = big0.compareTo(order.getTotalAmount());
        if(price >= 0){
            throw new RequestException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor que 0");
        }

        return orderRepository.save(order);
    }

    public Order updateOrder(Order order){

        Order selectedOrder = getOrderById(order.getId());

        BigDecimal totalAmount;
        totalAmount = selectedOrder.getTotalAmount();
        if ( order.getTotalAmount() != null){
            totalAmount = order.getTotalAmount();
        }
        BigDecimal big0 = new BigDecimal("0");
        int price = big0.compareTo(totalAmount);
        if(price >= 0){
            throw new RequestException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor que 0");
        }

        Long selectCustomerId = selectedOrder.getCustomerId();
        Long customerId = selectedOrder.getCustomerId();
        Date orderDate = selectedOrder.getOrderDate();
        if (order.getCustomerId() != null ){
            customerId = order.getCustomerId();
        }
        int customerComp = selectCustomerId.compareTo(customerId);
        if (customerComp != 0){
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de cliente no valido");
        }
        if (order.getOrderDate() != null){
            orderDate = order.getOrderDate();
        }
        order.setOrderDate(orderDate);
        order.setCustomerId(customerId);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id){
        Order selectedOrder = getOrderById(id);

        orderRepository.deleteById(selectedOrder.getId());
    }

    public List<Order> getOrderByCustomerId(Long id){
        return orderRepository.findOrderByCustomerId(id);
    }

    public List<Order> getAllOrdersPageable(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return orderRepository.findAll(pageable).getContent();
    }
}
