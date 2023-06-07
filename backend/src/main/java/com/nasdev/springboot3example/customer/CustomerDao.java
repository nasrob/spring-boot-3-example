package com.nasdev.springboot3example.customer;


import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);

    void insertCustomer(Customer customer);

    boolean isExistsWithEmail(String email);

    void deleteCustomerById(Integer id);

    boolean isExistsWithId(Integer customerId);

    Optional<Customer> getCustomerById(Integer customerId);

    void updateCustomer(Customer customer);
}
