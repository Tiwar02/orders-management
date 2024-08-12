package com.api.orders.controller;


import com.api.orders.exception.RequestException;
import com.api.orders.model.Customer;
import com.api.orders.model.Order;
import com.api.orders.service.CustomerService;
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
    private CustomerService customerService;

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
        Optional<Customer> customer;
        try{
            customer = Optional.ofNullable(customerService.getCustomerById(id));
        }catch (NoSuchElementException e){
            throw new RequestException(HttpStatus.NOT_FOUND, "Cliente no encontrado ID: " + id);
        }
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()) ;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws RequestException{
        /*if(customer.getId()==null){
            throw new RequestException(HttpStatus.BAD_REQUEST, "El id no puede ser aplicado");
        }*/
        if (customer.getEmail().isEmpty()){
            throw new RequestException(HttpStatus.BAD_REQUEST, "El email no puede estar vacio");
        }
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PatchMapping("edit/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable Long id) throws RequestException{
        if (id == null || id == 0)   {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de cliente no valido");
        }
        Customer selectedCustomer;
        try {
            selectedCustomer = customerService.getCustomerById(id);
        } catch (NoSuchElementException e){
            throw new RequestException(HttpStatus.NOT_FOUND, "El cliente no fue encontrado ID: " +id);
        }

        String firstName = selectedCustomer.getFirstName();
        String lastName = selectedCustomer.getLastName();
        String phone = selectedCustomer.getPhone();
        String customerEmail = selectedCustomer.getEmail();
        if(customer.getEmail() != null){
            customerEmail = customer.getEmail();
        }
        if (customer.getFirstName() != null){
            firstName = customer.getFirstName();
        }
        if (customer.getLastName() != null){
            lastName = customer.getLastName();
        }
        if (customer.getPhone() != null){
            phone = customer.getPhone();
        }
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(customerEmail);
        customer.setId(id);
        Customer updatedCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        if (id == null || id == 0)   {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Id de cliente no valido");
        }
        Customer selectedCustomer;
        try {
            selectedCustomer = customerService.getCustomerById(id);
        } catch (NoSuchElementException e){
            throw new RequestException(HttpStatus.NOT_FOUND, "El cliente no fue encontrado ID: " +id);
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
