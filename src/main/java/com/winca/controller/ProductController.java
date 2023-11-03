package com.winca.controller;

import com.winca.dto.ProductEvent;
import com.winca.entity.Product;
import com.winca.service.ProductCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductCommandService productCommandService;

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody ProductEvent product){
        Product savedProduct = productCommandService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable long id, @RequestBody ProductEvent product){
        log.info("id--", id,":", id);
        Product updatedProduct = productCommandService.updateProduct(id, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
