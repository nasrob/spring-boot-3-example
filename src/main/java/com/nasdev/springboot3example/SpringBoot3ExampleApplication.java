package com.nasdev.springboot3example;

import com.nasdev.springboot3example.customer.Customer;
import com.nasdev.springboot3example.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringBoot3ExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot3ExampleApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) {
		return args -> {
			Customer john = new Customer("John", "john@gmail.com", 23);
			Customer jane = new Customer("Jane", "jane@gmail.com", 33);

			List<Customer> customers = List.of(john, jane);
			// customerRepository.saveAll(customers);
		};
	}
}
