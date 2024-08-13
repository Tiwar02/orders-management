package com.api.orders.controller;


import com.api.orders.exception.RequestException;
import com.api.orders.model.Customer;
import com.api.orders.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() throws RequestException{
        Optional<List<Customer>> getCustomers = Optional.ofNullable(customerService.getAllCustomers());
        if (getCustomers.isEmpty()){
            throw new RequestException(HttpStatus.NOT_FOUND,"Ha ocurrido un error al obtener la lista");
        }
        return getCustomers.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) throws RequestException {
        if (id == null || id == 0)  {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de cliente no valido");
        }

        Optional<Customer> customer = Optional.ofNullable(customerService.getCustomerById(id));
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()) ;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws RequestException{

        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PatchMapping("edit/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) throws RequestException{
        if (customer.getId() == null || customer.getId() == 0)   {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de cliente no valido");
        }

        Customer updatedCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        if (id == null || id == 0)   {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de cliente no valido");
        }

        customerService.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("pages")
    public ResponseEntity<List<Customer>> getAllCustomersPaginable(@RequestParam int pageNumber, @RequestParam int pageSize) throws RequestException{
        Optional<List<Customer>> getCustomers = Optional.ofNullable(customerService.getAllCustomersPageable(pageNumber, pageSize));
        if (getCustomers.isEmpty()){
            throw new RequestException(HttpStatus.NOT_FOUND,"Ha ocurrido un error al obtener la lista");
        }
        return getCustomers.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.notFound().build());
    }

}
