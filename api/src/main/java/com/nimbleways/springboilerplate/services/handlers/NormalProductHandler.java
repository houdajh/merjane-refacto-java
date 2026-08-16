package com.nimbleways.springboilerplate.services.handlers;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.IProductHandler;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NormalProductHandler implements IProductHandler {

    private final NotificationService notificationService;
    private final ProductRepository productRepository;

    public NormalProductHandler(NotificationService notificationService, ProductRepository productRepository) {
        this.notificationService = notificationService;
        this.productRepository = productRepository;
    }

    @Override
    public ProductType productType() {
        return ProductType.NORMAL;
    }

    @Override
    public void handle(Product product) {
        if (product.isAvailable()) {
            product.decreamentStock();
        } else {
            int leadTime = product.getLeadTime();
            if (leadTime > 0) {
                notificationService.sendDelayNotification(leadTime, product.getName());
            }
        }
        productRepository.save(product);
    }
}
