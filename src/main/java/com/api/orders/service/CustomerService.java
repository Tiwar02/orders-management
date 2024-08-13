package com.api.orders.service;

import com.api.orders.exception.RequestException;
import com.api.orders.model.Customer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface CustomerService {

    public List<Customer> getAllCustomers();
    public Customer getCustomerById(Long id);
    public Customer createCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
    public void deleteCustomer(Long id);
    public List<Customer> getAllCustomersPageable(int pageNumber, int pageSize);
}
