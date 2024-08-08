package com.api.orders.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.api.orders.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @Test
    @DisplayName("Test for save customer")
    public void CustomerRepository_SaveCustomer_ReturnSavedCustomer(){

        customer = Customer.builder()
                    .firstName("Emilio")
                    .lastName("Perez")
                    .email("emilio@gmail.com")
                    .phone("3018997865")
                    .build();

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);

    }


    @Test
    @DisplayName("Test for obtain all customers")
    public void CustomerRepository_GetAll_ReturnMoreThenOneCustomer(){
        customer = Customer.builder()
                .firstName("Emilio")
                .lastName("Perez")
                .email("emilio@gmail.com")
                .phone("3018997865")
                .build();
        Customer customer2 = Customer.builder()
                .firstName("Pedro")
                .lastName("Perez")
                .email("pedro@gmail.com")
                .phone("3018997865")
                .build();

        customerRepository.save(customer);
        customerRepository.save(customer2);

        List<Customer> customerList = customerRepository.findAll();

        assertThat(customerList).isNotNull();
        assertThat(customerList.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("Test for obtain a customer per id")
    public void CustomerRepository_FindById_ReturnCustomer(){
        customer = Customer.builder()
                .firstName("Emilio")
                .lastName("Perez")
                .email("emilio@gmail.com")
                .phone("3018997865")
                .build();

        customerRepository.save(customer);

        Customer savedCustomer = customerRepository.findById(customer.getId()).get();

        assertThat(savedCustomer).isNotNull();

    }

    @Test
    @DisplayName("Test for update customer")
    public void CustomerRepository_UpdateCustomer_ReturnCustomerNotNull(){
        customer = Customer.builder()
                .firstName("Emilio")
                .lastName("Perez")
                .email("emilio@gmail.com")
                .phone("3018997865")
                .build();

        customerRepository.save(customer);

        Customer savedCustomer = customerRepository.findById(customer.getId()).get();

        savedCustomer.setPhone("300600900");
        savedCustomer.setFirstName("Pablito");

        Customer updatedCustomer = customerRepository.save(customer);

        assertThat(updatedCustomer.getFirstName()).isNotNull();
        assertThat(updatedCustomer.getFirstName()).isEqualTo("Pablito");
        assertThat(updatedCustomer.getPhone()).isNotNull();
        assertThat(updatedCustomer.getPhone()).isEqualTo("300600900");

    }

    @Test
    @DisplayName("Test for delete customer")
    public void CustomerRepository_DeleteCustomer_ReturnEmptyCustomer(){
        customer = Customer.builder()
                .firstName("Emilio")
                .lastName("Perez")
                .email("emilio@gmail.com")
                .phone("3018997865")
                .build();

        customerRepository.save(customer);

        customerRepository.deleteById(customer.getId());
        Optional<Customer> customerReturn = customerRepository.findById(customer.getId());

        assertThat(customerReturn).isEmpty();
    }

    /*@Test
    @DisplayName("Test for obtain a empty list of customers")
    public void CustomerRepository_GetAllEmpty_ReturnException(){

        List<Customer> customerList = customerRepository.findAll();

        assertThat(customerList).isEmpty();


    }*/

}
