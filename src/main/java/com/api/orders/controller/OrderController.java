package com.api.orders.controller;

import com.api.orders.exception.RequestException;
import com.api.orders.model.Order;
import com.api.orders.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() throws RequestException{
        Optional<List<Order>> getOrders = Optional.ofNullable(orderService.getAllOrders());
        if (getOrders.isEmpty()){
            throw new RequestException(HttpStatus.NOT_FOUND,"Ha ocurrido un error al obtener la lista");
        }
        return getOrders.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) throws RequestException{
        if (id == null || id == 0)  {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de orden no valido");
        }
        Optional<Order> order;
        try{
            order = Optional.ofNullable(orderService.getOrderById(id));
        }catch (NoSuchElementException e){
            throw new RequestException(HttpStatus.NOT_FOUND, "Orden no encontrada ID: " + id);
        }

        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()) ;
    }

    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) throws RequestException{
        if(order.getCustomerId()==null){
            throw new RequestException(HttpStatus.BAD_REQUEST, "El cliente no ha sido encontrado");
        }

        BigDecimal big0 = new BigDecimal("0");
        int price = big0.compareTo(order.getTotalAmount());
        if(price >= 0){
            throw new RequestException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor que 0");
        }
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order, @PathVariable Long id) throws RequestException{
        if (id == null || id == 0)   {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de orden no valido");
        }
        Order selectedOrder;
        try{
            selectedOrder = orderService.getOrderById(id);
        }catch (NoSuchElementException e){
            throw new RequestException(HttpStatus.NOT_FOUND, "Orden no encontrada ID: " + id);
        }

        order.setId(id);

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

        Order updatedOrder =orderService.updateOrder(order);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) throws RequestException{
        if (id == null || id == 0)  {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de orden no valido");
        }
        Order selectedOrder;
        try{
            selectedOrder = orderService.getOrderById(id);
        }catch (NoSuchElementException e){
            throw new RequestException(HttpStatus.NOT_FOUND, "Orden no encontrada ID: " + id);
        }
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("customer/{id}")
    public ResponseEntity<List<Order>> getOrderByCustomerId(@PathVariable Long id) throws RequestException{
        Optional<List<Order>> ordersByCustomer = Optional.ofNullable(orderService.getOrderByCustomerId(id));
        if (id == null || id == 0)  {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de cliente no valido");
        }

        if (ordersByCustomer.isEmpty()){
            throw new RequestException(HttpStatus.NOT_FOUND,"Ha ocurrido un error al obtener la lista");
        }
        return ordersByCustomer.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.notFound().build());
    }

}
