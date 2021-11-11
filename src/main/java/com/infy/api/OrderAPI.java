package com.infy.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.dto.CartDTO;
import com.infy.dto.OrderDTO;
import com.infy.dto.PlacedOrderDTO;
import com.infy.dto.ProductDTO;
import com.infy.dto.ProductsOrderedDTO;
import com.infy.exception.OrderException;
import com.infy.service.OrderService;
import com.infy.service.ProductsOrderedService;

import aj.org.objectweb.asm.TypeReference;
@Controller
@RestController
@RequestMapping(value = "/api")
public class OrderAPI {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductsOrderedService productsOrderedService;

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

	//placing order by buyer(buyer id)
	@PostMapping(value = "/placeOrder/{buyerId}")					
	public ResponseEntity<String> placeOrder(@PathVariable String buyerId, @RequestBody OrderDTO order){
		
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			List<ProductDTO> productList = new ArrayList<>();
			String userUri;
			List<CartDTO> cartList = mapper.convertValue(
					new RestTemplate().getForObject(userUri+"/userMS/buyer/cart/get/" + buyerId, List.class),
				    new TypeReference<List<CartDTO>>(){}
				);
			
			String productUri;
			cartList.forEach(item ->{
				ProductDTO prod = new RestTemplate().getForObject(productUri+"/prodMS/getById/" +item.getProdId(),ProductDTO.class) ; //getByProdId/{productId}
				System.out.println(prod.getProductDescription());
				productList.add(prod);
			});
			
			PlacedOrderDTO orderPlaced = orderService.placeOrder(productList,cartList,order);
			cartList.forEach(item->{
				new RestTemplate().getForObject(productUri+"/prodMS/updateStock/" +item.getProdId()+"/"+item.getQuantity(), boolean.class) ;
				new RestTemplate().postForObject(userUri+"/userMS/buyer/cart/remove/"+buyerId+"/"+item.getProdId(),null, String.class);
			});			
			
			new RestTemplate().getForObject(userUri+"/userMS/updateRewardPoints/"+buyerId+"/"+orderPlaced.getRewardPoints() , String.class);
			
			return new ResponseEntity<>(orderPlaced.getOrderId(),HttpStatus.ACCEPTED);
		}
		catch(Exception e)
		{
			String newMsg = "There was some error";
			if(e.getMessage().equals("404 null"))
			{
				newMsg = "Error while placing the order";
			}
			return new ResponseEntity<>(newMsg,HttpStatus.UNAUTHORIZED);
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
	
	@GetMapping(value = "/productsOrders")
	public ResponseEntity<List<ProductsOrderedDTO>> viewProductOrderedDetails() throws OrderException {
		try {
			List<ProductsOrderedDTO> productorderedDTO = productsOrderedService.viewProductOrderedDetails();
			return new ResponseEntity<>(productorderedDTO, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()), exception);
		}
	}
	//to re-order an order(order id) by buyer(buyer id)
		@PostMapping(value = "/reOrder/{buyerId}/{orderId}")							
		public ResponseEntity<String> reOrder(@PathVariable String buyerId, @PathVariable String orderId){
			
			try {
				
				String id = orderService.reOrder(buyerId,orderId);
				return new ResponseEntity<>("Order ID: Successfully done "+id,HttpStatus.ACCEPTED);
			}
			catch(Exception e)
			{
				return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
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
				ProductsOrderedDTO productOrder = new ProductsOrderedDTO();
				productOrder.setBuyerId(orderDTO.getBuyerId());
				productOrder.setOrderId(orderDTO.getOrderId());
				productOrder.setStatus("ORDER PLACED");
				productOrder.setProdId(cart.getProductId());
				productOrder.setQuantity(cart.getQuantity());
				productOrder.setSellerId(product.getSellerId());
				productsOrderedService.addProductsOrder(productOrder);
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
