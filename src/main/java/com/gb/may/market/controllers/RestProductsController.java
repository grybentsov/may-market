package com.gb.may.market.controllers;

import com.gb.may.market.entities.Product;
import com.gb.may.market.entities.dtos.ProductDto;
import com.gb.may.market.exceptions.ProductNotFoundException;
import com.gb.may.market.services.ProductsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
@Api("Set of endpoints for CRUD operations for Products")
public class RestProductsController {
    private ProductsService productsService;

    @Autowired
    public RestProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/dto")
    @ApiOperation("Returns list of all products data transfer objects")
    public List<ProductDto> getAllProductsDto(){
        return productsService.getDtoData();
    }

    @GetMapping(produces = "application/json")
    @ApiOperation("Returns list of all products")
    public List<Product> getAllProducts(){
        return productsService.findAll();
    }

//    @GetMapping("/{id}")
//    public Product getOneProduct(@PathVariable Long id){
//        return productsService.findById(id);
//    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ApiOperation("Returns one product by id")
    public ResponseEntity<?> getOneProduct
            (@PathVariable @ApiParam("Id of the product to be requested. It cannot be empty.") Long id){
        if (!productsService.existsById(id)){
            throw new ProductNotFoundException("Product is not found, id: " + id);
        }
        return new ResponseEntity<>(productsService.findById(id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Removes one product by id")
    public void deleteOneProduct (@PathVariable Long id){
        productsService.deleteById(id);
    }

    @DeleteMapping
    @ApiOperation("Removes all products")
    public String deleteAllProducts (){
        productsService.deleteAll();
        return "OK";
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates a new product")
    public Product saveNewProduct(@RequestBody Product product){
        if (product.getId() != null){
            product.setId(null);
        }
        return productsService.saveOrUpdate(product);
    }

//    @PutMapping
//    public Product modifyProduct(@RequestBody Product product){
//        if (product.getId() == null || !productsService.existsById(product.getId())){
//            throw new ProductNotFoundException("Product is not found, id: " + product.getId());
//        }
//        return productsService.saveOrUpdate(product);
//    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation("Modifies an existing product")
    public ResponseEntity<?> modifyProduct(@RequestBody Product product){
        if (product.getId() == null || !productsService.existsById(product.getId())){
            throw new ProductNotFoundException("Product is not found, id: " + product.getId());
        }
        if (product.getPrice().doubleValue() < 0.0){
            return new ResponseEntity<>("Product's price cannot be negative!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productsService.saveOrUpdate(product), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException (ProductNotFoundException exc){
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }
}
