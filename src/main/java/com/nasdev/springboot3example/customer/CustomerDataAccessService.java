package com.nasdev.springboot3example.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDataAccessService implements CustomerDao {

    private static final List<Customer> customers; // db

    static {
        customers = new ArrayList<>();
        Customer john = new Customer(1, "John", "john@gmail.com", 23);
        customers.add(john);

        Customer jane = new Customer(1, "Jane", "jane@gmail.com", 33);
        customers.add(jane);
    }


    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }
}
