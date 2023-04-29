package com.nasdev.springboot3example.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomer() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomer(@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRequest customerRequest) {
        customerService.addCustomer(customerRequest);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable Integer customerId,
                                   @RequestBody CustomerRequest customerRequest)
    {
        customerService.updateCustomer(customerId, customerRequest);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable Integer customerId) {
        customerService.deleteCustomer(customerId);
    }
}
