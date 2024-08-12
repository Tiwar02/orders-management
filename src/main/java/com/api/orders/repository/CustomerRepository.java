package com.api.orders.repository;

import com.api.orders.model.Customer;
import com.api.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, ListPagingAndSortingRepository<Customer, Long> {

}
