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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@UnitTest
public class ExpirableProductHandlerTest {
    @Mock
    private NotificationService notificationService;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ExpirableProductHandler expirableProductHandler;

    @Test
    public void testAvailableNotYetExpiredProduct() {
        //arrange
        Product product=new Product(null, 15, 30, ProductType.EXPIRABLE, "Butter", LocalDate.now().plusDays(26), null,
                null);

        //act
        expirableProductHandler.handle(product);

        //asset
        verify(productRepository).save(product);
        assertEquals(29,product.getAvailable());
    }

    @Test
    public void testExpiredProduct() {
        //arrange
        Product product=new Product(null, 90, 6, ProductType.EXPIRABLE, "Milk", LocalDate.now().minusDays(2), null, null);

        //act
        expirableProductHandler.handle(product);

        //asset
        verify(productRepository).save(product);
        verify(notificationService).sendExpirationNotification("Milk", LocalDate.now().minusDays(2));
        assertEquals(0,product.getAvailable());
    }
}
