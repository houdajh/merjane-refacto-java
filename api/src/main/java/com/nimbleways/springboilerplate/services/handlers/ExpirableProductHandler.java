package com.nimbleways.springboilerplate.services.handlers;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.IProductHandler;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class ExpirableProductHandler implements IProductHandler {

    private final NotificationService notificationService;
    private final ProductRepository productRepository;

    public ExpirableProductHandler(NotificationService notificationService, ProductRepository productRepository) {
        this.notificationService = notificationService;
        this.productRepository = productRepository;
    }


    @Override
    public ProductType productType() {
        return ProductType.EXPIRABLE;
    }

    @Override
    public void handle(Product product) {
        if (product.isAvailable() && !product.isExpired()) {
            product.decreamentStock();
        } else {
            notificationService.sendExpirationNotification(product.getName(), product.getExpiryDate());
            product.markOutOfStock();
        }
        productRepository.save(product);
    }
}
