package com.infy.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.infy.dto.CartDTO;
import com.infy.dto.OrderDTO;
import com.infy.dto.ProductDTO;
import com.infy.dto.ProductsorderedDTO;
import com.infy.exception.OrderException;
import com.infy.service.OrderService;
import com.infy.service.ProductsorderedService;

@RestController
@RequestMapping(value = "/api")
public class OrderAPI {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductsorderedService productsorderedService;

	@Autowired
	private Environment environment;

	@GetMapping(value = "/orders")
	public ResponseEntity<List<OrderDTO>> getAllOrders() throws OrderException {
		try {
		List<OrderDTO> orderDTOs = orderService.getAllOrders();
		return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
		}
	}

	@GetMapping(value = "/orders/{orderId}")
	public ResponseEntity<OrderDTO> viewOrder(@PathVariable String orderId) throws OrderException {
		try {
			OrderDTO orderDTO = orderService.viewOrder(orderId);
			return new ResponseEntity<>(orderDTO, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
		}
	}
	
	@GetMapping(value = "/productorders")
	public ResponseEntity<List<ProductsorderedDTO>> viewProductOrderedDetails() throws OrderException {
		try {
			List<ProductsorderedDTO> productorderedDTO = productsorderedService.viewProductOrderedDetails();
			return new ResponseEntity<>(productorderedDTO, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
		}
	}

	@PostMapping(value = "/orders")
	public ResponseEntity<String> addOrder(@RequestBody OrderDTO orderDTO) throws OrderException {
		try {
			Integer totalAmount = 0;
			String url = "http://localhost:8100/api/buyer/cart/"+orderDTO.getBuyerId();
			RestTemplate restTemplate = new RestTemplate();
			CartDTO[] cartList = restTemplate.getForObject(url, CartDTO[].class);
			for(CartDTO cart: cartList) {
				String urlP = "http://localhost:8300/api/product/"+cart.getProductId();
				ProductDTO product = restTemplate.getForObject(urlP, ProductDTO.class);
				ProductsorderedDTO productOrder = new ProductsorderedDTO();
				productOrder.setBuyerId(orderDTO.getBuyerId());
				productOrder.setOrderId(orderDTO.getOrderId());
				productOrder.setStatus("ORDER PLACED");
				productOrder.setProdId(cart.getProductId());
				productOrder.setQuantity(cart.getQuantity());
				productOrder.setSellerId(product.getSellerId());
				productsorderedService.addProductsOrder(productOrder);
				totalAmount += product.getProductPrice() * cart.getQuantity();
			}
			OrderDTO finalOrder = new OrderDTO();
			finalOrder.setOrderId(orderDTO.getOrderId());
			finalOrder.setBuyerId(orderDTO.getBuyerId());
			finalOrder.setAddress(orderDTO.getAddress());
			finalOrder.setStatus("ORDER PLACED");
			finalOrder.setDate(LocalDate.now());
			finalOrder.setAmount(totalAmount);
			String orderId = orderService.addOrder(finalOrder);
			String successMessage = environment.getProperty("API.INSERT_SUCCESS") + cartList;
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
		}
	}

}
