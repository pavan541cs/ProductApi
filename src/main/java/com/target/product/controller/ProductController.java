package com.target.product.controller;

import com.target.product.model.ProductDescription;
import com.target.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    ResponseEntity<List<ProductDescription>> getAllProducts() {
        List<ProductDescription> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<ProductDescription> createProduct(@Valid @RequestBody ProductDescription product) {
        try {
            ProductDescription newProuct = productService.createProduct(product);
            return new ResponseEntity<>(newProuct, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDescription> findOneProduct(@PathVariable String id) {
        Optional<ProductDescription> product = productService.findById(id);
        HttpStatus myStatus = HttpStatus.OK;
        if(!product.isPresent()) {
            myStatus = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(null, myStatus);
        }
        return new ResponseEntity<>(product.get(), myStatus);
    }

    @PutMapping("/{id}")
    ResponseEntity updateOneProduct(@PathVariable String id, @Valid @RequestBody ProductDescription product) {
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            if(!productService.updateById(id, product)) {
                status = HttpStatus.NOT_FOUND;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }

}
