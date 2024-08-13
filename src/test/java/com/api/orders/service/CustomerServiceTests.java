package com.api.orders.service;

import com.api.orders.model.Customer;
import com.api.orders.repository.CustomerRepository;
import com.api.orders.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;
    private Customer customer;

    @BeforeEach
    public void init(){
        customer = Customer.builder()
                .firstName("Emilio")
                .lastName("Perez")
                .email("emilio@gmail.com")
                .phone("3018997865")
                .build();
    }

    @Test
    @DisplayName("Test for create a new customer")
    public void CustomerService_CreateCustomer_ReturnsCustomer(){
        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.createCustomer(customer);

        assertThat(savedCustomer).isNotNull();
    }

    @Test
    @DisplayName("Test for find a customer per id")
    public void CustomerService_FindById_ReturnCustomer(){
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(customer));

        Customer customerReturn = customerService.getCustomerById(1L);

        assertThat(customerReturn).isNotNull();
    }

    @Test
    @DisplayName("Test for update a customer")
    public void CustomerService_UpdateCustomer_ReturnCustomer(){

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer  updatedCustomer = customerService.updateCustomer(customer);

        assertThat(updatedCustomer).isNotNull();
    }

    @Test
    @DisplayName("Test for delete customer")
    public void CustomerService_DeleteCustomer_ReturnVoid(){
        Long customerId = 1L;

        assertAll( () -> customerService.deleteCustomer(customerId));
    }

    @Test
    @DisplayName("Test for obtain a list of customers")
    public void CustomerService_GetAll_ReturnListCustomers(){

        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        List<Customer> getAllOrders = customerService.getAllCustomers();

        assertThat(getAllOrders).isNotNull();
    }
}
