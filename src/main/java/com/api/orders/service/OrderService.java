package com.api.orders.service;

import com.api.orders.model.Order;
import com.api.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    public List<Order> getAllOrders();
    public Order getOrderById(Long id);
    public Order createOrder(Order order);
    public Order updateOrder(Order order);
    public void deleteOrder(Long id);
    public List<Order> getOrderByCustomerId(Long id);
    public List<Order> getAllOrdersPageable(int pageNumber, int pageSize);
}
