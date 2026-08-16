package com.nimbleways.springboilerplate.services;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;

public interface IProductHandler {

    ProductType productType();

    void handle(Product product);
}
