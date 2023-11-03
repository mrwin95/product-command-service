package com.winca.service;

import com.winca.dto.ProductEvent;
import com.winca.entity.Product;
import com.winca.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommandService {

    private final ProductRepository productRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Product createProduct(ProductEvent productEvent){
        Product productDTO = productRepository.save(productEvent.getProduct());
        log.info("saved Product", productDTO);
        ProductEvent event = new ProductEvent("CreateProduct", productDTO);
        kafkaTemplate.send("product-event-topic", event);

        return productDTO;
    }

    public Product updateProduct(long id, ProductEvent productEvent){

        try {
            Product existingProduct = productRepository.findById(id).get();
            Product newProduct = productEvent.getProduct();
            existingProduct.setName(newProduct.getName());
            existingProduct.setPrice(newProduct.getPrice());
            existingProduct.setDescription(newProduct.getDescription());

            Product productDTO = productRepository.save(existingProduct);
            ProductEvent event = new ProductEvent("UpdateProduct", productDTO);
            kafkaTemplate.send("product-event-topic", event);
            return productDTO;
        }catch (Exception e){
            log.info("", e);
        }
        return null;
    }

    public void deleteProduct(long id){
        Product deletingProduct = productRepository.findById(id).get();
        productRepository.delete(deletingProduct);
        ProductEvent event = new ProductEvent("DeleteProduct", deletingProduct);
        kafkaTemplate.send("product-event-topic", event);
    }
}
