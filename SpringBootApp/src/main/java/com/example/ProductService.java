package com.example;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
	
	private List<Product> products = new ArrayList<>();
	
	public List<Product> getProducts(){
		return products;
	}

	public Product findProductById(String id){
		Product product = null;
		for(Product p : products){
			if(id.equals(p.getId())){
				product = p;
				break;
			}
		}
		
		return product;
	}
	
	public void addProduct(Product product){
		products.add(product);
	}
	
	public boolean isProductExist(String id){
		boolean isExist = false;
		for(Product p : products){
			if(id.equals(p.getId())){
				isExist = true;
				break;
			}
		}
		
		return isExist;
	}
	
	public void updateProduct(String id, String nameToUpdate){
		for(Product p : products){
			if(id.equals(p.getId())){
				p.setName(nameToUpdate);
			}
		}
	}
}
