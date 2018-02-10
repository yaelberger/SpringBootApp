package com.example;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootAppTests {

	private MockMvc mockMvc;

	@Mock
	private ProductService productService = new ProductService();

	@InjectMocks
	private ProductController producyController;

	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(producyController).build();
	}

	@Test
	public void testGetAllProducts() throws Exception {
		Product p1 = new Product();
		p1.setId("125");
		p1.setName("MS Sculpt Ergonomic Keyboard");
		Product p2 = new Product();
		p2.setId("123");
		p2.setName("Microsoft Natural Ergonomic Keyboard");
		List<Product> products = Arrays.asList(p1, p2);
		
		when(productService.getProducts()).thenReturn(products);
		
		mockMvc.perform(get("/shop/products"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id", is("125")))
		.andExpect(jsonPath("$[0].name", is("MS Sculpt Ergonomic Keyboard")))
		.andExpect(jsonPath("$[1].id", is("123")))
		.andExpect(jsonPath("$[1].name", is("Microsoft Natural Ergonomic Keyboard")));

		verify(productService, times(1)).getProducts();
	}

	@Test
	public void testGetProductByIdSuccess() throws Exception {
		Product p1 = new Product();
		p1.setId("125");
		p1.setName("MS Sculpt Ergonomic Keyboard");

		when(productService.findProductById("125")).thenReturn(p1);

		mockMvc.perform(get("/shop/product/{id}", "125"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is("125")))
		.andExpect(jsonPath("$.name", is("MS Sculpt Ergonomic Keyboard")));

		verify(productService, times(1)).findProductById("125");
	}

	@Test
	public void testGetProductByIdNotFound() throws Exception {

		when(productService.findProductById("125")).thenReturn(null);

		mockMvc.perform(get("/shop/product/{id}", "125"))
		.andExpect(status().isNotFound());

		verify(productService, times(1)).findProductById("125");
	}

	@Test
	public void testAddProduct() throws Exception {
		Product p1 = new Product();
		p1.setId("125");
		p1.setName("MS Sculpt Ergonomic Keyboard");

		when(productService.isProductExist(p1.getId())).thenReturn(false);
		doNothing().when(productService).addProduct(p1);

		mockMvc.perform(
				post("/shop/product/{id}", "125")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(p1)))
		.andExpect(status().isOk());
		
		verify(productService, times(1)).isProductExist(p1.getId());
	}

	@Test
	public void testUpdateProduct() throws Exception {
		Product p1 = new Product();
		p1.setId("125");
		p1.setName("MS Sculpt Ergonomic Keyboard");

		when(productService.isProductExist("125")).thenReturn(true);
		doNothing().when(productService).updateProduct(p1.getId(), p1.getName());

		mockMvc.perform(
				put("/shop/product/{id}", "125")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(p1)))
		.andExpect(status().isOk());
		
		verify(productService, times(1)).isProductExist(p1.getId());
		verify(productService, times(1)).updateProduct(p1.getId(), p1.getName());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
