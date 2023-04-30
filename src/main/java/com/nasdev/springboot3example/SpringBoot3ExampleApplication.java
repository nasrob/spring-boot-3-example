package com.nasdev.springboot3example;

import com.github.javafaker.Faker;
import com.nasdev.springboot3example.customer.Customer;
import com.nasdev.springboot3example.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class SpringBoot3ExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot3ExampleApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) {
		return args -> {
			 var faker = new Faker();
			Random random = new Random();

			 var customer = new Customer(
					 faker.name().fullName(),
					 faker.internet().safeEmailAddress(),
					 random.nextInt(16, 99)
			 );

			 customerRepository.save(customer);
		};
	}
}
