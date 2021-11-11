package com.infy.service;

import java.util.List;

import com.infy.dto.CartDTO;
import com.infy.dto.OrderDTO;
import com.infy.dto.PlacedOrderDTO;
import com.infy.dto.ProductDTO;
import com.infy.exception.OrderException;

public interface OrderService {
	public String addOrder(OrderDTO orderDTO) throws OrderException;
	
	public OrderDTO viewOrder(String orderId) throws OrderException;
	
	public List<OrderDTO> getAllOrders() throws OrderException;
    
	public String reOrder(String buyerId, String orderId) throws OrderException;

	public PlacedOrderDTO placeOrder(List<ProductDTO> productList, List<CartDTO> cartList, OrderDTO order);
}
