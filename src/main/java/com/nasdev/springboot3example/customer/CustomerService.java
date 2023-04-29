package com.nasdev.springboot3example.customer;

import com.nasdev.springboot3example.exception.DuplicateResourceException;
import com.nasdev.springboot3example.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer customerId) {
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("customer with id [%s] not found".formatted(customerId)));
    }

    public void addCustomer(CustomerRequest customerRequest) {
        String email = customerRequest.email();
        if (customerDao.isExistsWithEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }

        Customer customer = new Customer(customerRequest.name(), customerRequest.email(), customerRequest.age());
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomer(Integer id) {
        if (!customerDao.isExistsWithId(id)) {
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }

        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Integer customerId, CustomerRequest customerRequest) {

        Customer customer = customerDao.getCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("customer with id [%s] not found".formatted(customerId)));

        customer.setName(customerRequest.name());
        customer.setEmail(customerRequest.email());
        customer.setAge(customerRequest.age());

        customerDao.updateCustomer(customer);
    }
}
