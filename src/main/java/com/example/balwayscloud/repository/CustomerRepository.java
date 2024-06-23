package com.example.balwayscloud.repository;

import com.example.balwayscloud.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    Customer findByUsername(String username);
}
