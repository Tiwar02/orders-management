package com.api.orders.controller;

import com.api.orders.exception.RequestException;
import com.api.orders.model.Order;
import com.api.orders.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

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
        order = Optional.ofNullable(orderService.getOrderById(id));

        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()) ;
    }

    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) throws RequestException{
        if(order.getCustomerId()==null){
            throw new RequestException(HttpStatus.BAD_REQUEST, "El cliente no ha sido encontrado");
        }

        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) throws RequestException{
        if (order.getId() == null || order.getId() == 0)   {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de orden no valido");
        }
        Order updatedOrder = orderService.updateOrder(order);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) throws RequestException{
        if (id == null || id == 0)  {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de orden no valido");
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

    @GetMapping("pages")
    public ResponseEntity<List<Order>> getAllOrdersPaginable(@RequestParam int pageNumber, @RequestParam int pageSize) throws RequestException{
        Optional<List<Order>> getOrders = Optional.ofNullable(orderService.getAllOrdersPageable(pageNumber,pageSize));
        if (getOrders.isEmpty()){
            throw new RequestException(HttpStatus.NOT_FOUND,"Ha ocurrido un error al obtener la lista");
        }
        return getOrders.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.notFound().build());
    }

}
