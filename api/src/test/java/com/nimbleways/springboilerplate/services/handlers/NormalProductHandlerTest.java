package com.nimbleways.springboilerplate.services.handlers;


import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@UnitTest
public class NormalProductHandlerTest {

    @Mock
    private NotificationService notificationService;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private NormalProductHandler normalProductHandler;

    @Test
    public void normalAvailableProductHandlerTest() {
        //arrange
        Product product = new Product(null, 15, 30, ProductType.NORMAL, "USB Cable", null, null, null);

        //act
        normalProductHandler.handle(product);

        //assert
        assertEquals(29,product.getAvailable().intValue());
        verify(productRepository).save(product);

    }

    @Test
    public void normalNotAvailableProductHandlerTest() {
        //arrange
        Product product = new Product(null, 10, 0, ProductType.NORMAL, "USB Dongle", null, null, null);
        //act
        normalProductHandler.handle(product);

        //assert
        assertEquals(0,product.getAvailable().intValue());
        verify(productRepository).save(product);
        verify(notificationService).sendDelayNotification(product.getLeadTime(),product.getName());

    }
}
