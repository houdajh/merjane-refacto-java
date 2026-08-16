package com.nimbleways.springboilerplate.services.handlers;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.IProductHandler;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class SeasonalProductHandler implements IProductHandler {

    private final NotificationService notificationService;
    private final ProductRepository productRepository;

    public SeasonalProductHandler(NotificationService notificationService, ProductRepository productRepository) {
        this.notificationService = notificationService;
        this.productRepository = productRepository;
    }


    @Override
    public ProductType productType() {
        return ProductType.SEASONAL;
    }

    @Override
    public void handle(Product product) {
        if (product.isAvailable() && product.isInSeasonPeriod()) {
            product.decreamentStock();
        } else if (product.isSeasonEndingBeforeRestock()) {
            notificationService.sendOutOfStockNotification(product.getName());
            product.markOutOfStock();
        } else if (product.isSeasonNotStarted()) {
            notificationService.sendOutOfStockNotification(product.getName());
        } else {
            //!product.isAvailable() && product.isInSeason() && !product.isSeasonEndingBeforeRestock())
            notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
        }
        productRepository.save(product);
    }
}
