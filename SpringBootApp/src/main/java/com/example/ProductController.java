package com.example;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop")
@SpringBootApplication
public class ProductController {
	
	ProductService productSrevice = new ProductService();
	
	public static void main(String[] args) {
        SpringApplication.run(ProductController.class, args);
    }

	@RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productSrevice.getProducts();
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductById(@PathVariable("id") String id) {
        Product product = productSrevice.findProductById(id);
		if(product == null){
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

	@RequestMapping(value = "/product/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> addProduct(@PathVariable("id") String id, @RequestBody Product product) {
        if (productSrevice.isProductExist(id)) {
            return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
        }
        productSrevice.addProduct(product);
 
        return new ResponseEntity<String>(HttpStatus.OK);
    }
	
	@RequestMapping(value = "/product/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateProduct(@PathVariable("id") String id, @RequestBody Product product) {
        if (!productSrevice.isProductExist(id)) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        productSrevice.updateProduct(id, product.getName());
 
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}

