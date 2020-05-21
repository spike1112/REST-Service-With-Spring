package com.example.demo;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(Long id) {
        super("could not find order " + id);
    }
}
