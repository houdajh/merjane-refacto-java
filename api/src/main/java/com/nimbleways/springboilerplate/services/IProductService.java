package com.nimbleways.springboilerplate.services;

import com.nimbleways.springboilerplate.entities.Product;

import java.util.Set;

public interface IProductService {
    void processProducts(Set<Product> products);
}
