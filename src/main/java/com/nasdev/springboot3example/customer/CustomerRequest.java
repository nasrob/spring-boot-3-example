package com.nasdev.springboot3example.customer;

public record CustomerRequest(
        String name,
        String email,
        Integer age
)
{}
