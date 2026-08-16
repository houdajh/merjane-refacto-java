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
public class SeasonalProductHandlerTest {

    @Mock
    private NotificationService notificationService;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SeasonalProductHandler seasonalProductHandler;

    @Test
    public void productAvailableAndInSeasonPeriodTest(){
        //arrange
        Product product= new Product(null, 15, 30, ProductType.SEASONAL, "Watermelon", null, LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(58));

        //act
        seasonalProductHandler.handle(product);

        //assert
        verify(productRepository).save(product);
        assertEquals(29,product.getAvailable());
    }

    @Test
    public void seasonEndingBeforeRestockTest(){
        //arrange
        Product product = new Product(null, 245, 30, ProductType.SEASONAL, "Grapes", null, LocalDate.now().plusDays(180),
                LocalDate.now().plusDays(240));

        //act
        seasonalProductHandler.handle(product);

        //assert
        verify(notificationService).sendOutOfStockNotification(product.getName());
        verify(productRepository).save(product);
        assertEquals(0,product.getAvailable());
    }

    @Test
    public void seasonNotStartedTest(){
        //arrange
        Product product = new Product(null, 15, 30, ProductType.SEASONAL, "Grapes", null, LocalDate.now().plusDays(180),
                LocalDate.now().plusDays(240));

        //act
        seasonalProductHandler.handle(product);

        //assert
        verify(notificationService).sendOutOfStockNotification(product.getName());
        verify(productRepository).save(product);
        assertEquals(30,product.getAvailable());
    }

    @Test
    public void inSeasonButOutOfStockTest() {
        //arrange
        Product product = new Product(null, 5, 0, ProductType.SEASONAL, "Watermelon", null,
                LocalDate.now().minusDays(30), LocalDate.now().plusDays(60));

        //act
        seasonalProductHandler.handle(product);

        //assert
        verify(notificationService).sendDelayNotification(5, "Watermelon");
        verify(productRepository).save(product);
        assertEquals(0, product.getAvailable().intValue());
    }
}
