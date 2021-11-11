package com.infy.service;

import java.util.List;


import com.infy.dto.OrderDTO;
import com.infy.exception.OrderException;

public interface OrderService {
	public String addOrder(OrderDTO orderDTO) throws OrderException;
	public OrderDTO viewOrder(String orderId) throws OrderException;
	public List<OrderDTO> getAllOrders() throws OrderException;
}
