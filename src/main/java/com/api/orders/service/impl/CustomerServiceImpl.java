package com.api.orders.service.impl;


import com.api.orders.exception.RequestException;
import com.api.orders.model.Customer;
import com.api.orders.repository.CustomerRepository;
import com.api.orders.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id){
        return customerRepository.findById(id).orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Customer not found ID: " + id));
    }

    public Customer createCustomer(Customer customer){

        if (customer.getEmail().isEmpty()){
            throw new RequestException(HttpStatus.BAD_REQUEST, "Email can't be empty");
        }

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer){

        Customer selectedCustomer = getCustomerById(customer.getId());;

        String firstName = selectedCustomer.getFirstName();
        String lastName = selectedCustomer.getLastName();
        String phone = selectedCustomer.getPhone();
        String customerEmail = selectedCustomer.getEmail();
        if(customer.getEmail() != null){
            customerEmail = customer.getEmail();
        }
        if (Objects.equals(customer.getEmail(), "")){
            customerEmail = selectedCustomer.getEmail();
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

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id){
        Customer selectedCustomer = getCustomerById(id);

        customerRepository.deleteById(id);
    }

    public List<Customer> getAllCustomersPageable(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return customerRepository.findAll(pageable).getContent();
    }
}
