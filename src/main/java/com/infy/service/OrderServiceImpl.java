package com.infy.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.exception.OrderException;
import com.infy.repository.OrderRepository;
import com.infy.repository.ProductsOrderedRepository;
import com.infy.dto.OrderDTO;
import com.infy.dto.ProductsorderedDTO;
import com.infy.entity.Order;
import com.infy.entity.Productsordered;

@Service(value = "orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductsOrderedRepository proRepository;

	@Override
	public String addOrder(OrderDTO orderDTO) throws OrderException {
		Order o1= new Order();
		o1.setOrderId(orderDTO.getOrderId());
		o1.setBuyerId(orderDTO.getBuyerId());
		o1.setAddress(orderDTO.getAddress());
		o1.setAmount(orderDTO.getAmount());
		o1.setDate(orderDTO.getDate());
		o1.setStatus(orderDTO.getStatus());
		Order o2=orderRepository.save(o1);
		return o2.getOrderId();
	}

	@Override
	public OrderDTO viewOrder(String orderId) throws OrderException {
		Optional<Order> optional = orderRepository.findById(orderId);
		Order order = optional.orElseThrow(() -> new OrderException("Service.ORDER_NOT_FOUND"));
		OrderDTO o1=new OrderDTO();
		o1.setOrderId(order.getOrderId());
		o1.setBuyerId(order.getBuyerId());
		o1.setAddress(order.getAddress());
		o1.setAmount(order.getAmount());
		o1.setDate(order.getDate());
		o1.setStatus(order.getStatus());
		Iterable<Productsordered> pos = proRepository.findAll();
		List<ProductsorderedDTO> pDTOs = new ArrayList<>();
		pos.forEach(po -> {
			ProductsorderedDTO po1=new ProductsorderedDTO();
			po1.setBuyerId(po.getBuyerId());
			po1.setProdId(po.getProdId());
			po1.setSellerId(po.getSellerId());
			po1.setQuantity(po.getQuantity());
			po1.setStatus(po.getStatus());
			po1.setOrderId(po.getOrderId());
			pDTOs.add(po1);
		});
		o1.setProductsOrdered(pDTOs);
		return o1;
	}


	@Override
	public List<OrderDTO> getAllOrders() throws OrderException {
		Iterable<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = new ArrayList<>();
		orders.forEach(order -> {
			OrderDTO o1=new OrderDTO();
			o1.setOrderId(order.getOrderId());
			o1.setBuyerId(order.getBuyerId());
			o1.setAddress(order.getAddress());
			o1.setAmount(order.getAmount());
			o1.setDate(order.getDate());
			o1.setStatus(order.getStatus());
			orderDTOs.add(o1);
			Iterable<Productsordered> pos = proRepository.findAll();
			List<ProductsorderedDTO> pDTOs = new ArrayList<>();
			pos.forEach(po -> {
				ProductsorderedDTO po1=new ProductsorderedDTO();
				po1.setBuyerId(po.getBuyerId());
				po1.setProdId(po.getProdId());
				po1.setSellerId(po.getSellerId());
				po1.setQuantity(po.getQuantity());
				po1.setStatus(po.getStatus());
				po1.setOrderId(po.getOrderId());
				pDTOs.add(po1);
			});
			o1.setProductsOrdered(pDTOs);
		});
		
		if (orderDTOs.isEmpty())
			throw new OrderException("Service.ORDERS_NOT_FOUND");
		return orderDTOs;
	}

}
