package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.IProductHandler;
import com.nimbleways.springboilerplate.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    @Autowired
    ProductRepository pr;

    @Autowired
    NotificationService ns;

    private final Map<ProductType, IProductHandler> productHandlerMap;

    public ProductService(List<IProductHandler> productHandlerList) {
        this.productHandlerMap = productHandlerList.stream().collect(Collectors.toMap(IProductHandler::productType, (p) -> p));
    }

    @Override
    public void processProducts(Set<Product> products) {
        products.forEach(this::processProduct);
    }

    public void processProduct(Product product) {
        IProductHandler productHandler = productHandlerMap.get(product.getType());
        if (productHandler == null) {
            throw new RuntimeException("No handler for type " + product.getType());
        }
        productHandler.handle(product);
    }

}