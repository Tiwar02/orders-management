package com.api.orders.repository;

import com.api.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


//    @Query("SELECT o From orders o WHERE o.customerId = :customerId")
    List<Order> findOrderByCustomerId(Long customerId);
}
