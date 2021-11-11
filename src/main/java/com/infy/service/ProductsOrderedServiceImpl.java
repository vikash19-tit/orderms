package com.infy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.dto.OrderDTO;
import com.infy.dto.ProductsOrderedDTO;
import com.infy.entity.Order;
import com.infy.entity.ProductsOrdered;
import com.infy.entity.ProductsOrdered;
import com.infy.exception.OrderException;
import com.infy.repository.OrderRepository;
import com.infy.repository.ProductsOrderedRepository;

@Service(value = "productsOrderedService")
@Transactional
public class ProductsOrderedServiceImpl implements ProductsOrderedService{

	@Autowired
	private ProductsOrderedRepository proRepository;



	@Override
	public List<ProductsOrderedDTO> viewProductOrderedDetails() throws OrderException {
		Iterable<ProductsOrdered> pos = proRepository.findAll();
		List<ProductsOrderedDTO> pDTOs = new ArrayList<>();
		pos.forEach(po -> {
			ProductsOrderedDTO po1=new ProductsOrderedDTO();
			po1.setBuyerId(po.getBuyerId());
			po1.setProdId(po.getProdId());
			po1.setSellerId(po.getSellerId());
			po1.setQuantity(po.getQuantity());
			po1.setStatus(po.getStatus());
			po1.setOrderId(po.getOrderId());
			pDTOs.add(po1);
		});
		if (pDTOs.isEmpty())
			throw new OrderException("Service.PRODUCT_ORDERS_NOT_FOUND");
		return pDTOs;
	}
	@Override
	public String addProductsOrder(ProductsOrderedDTO pDTO) throws OrderException {
		ProductsOrdered p = new ProductsOrdered();
		p.setBuyerId(pDTO.getBuyerId());
		p.setOrderId(pDTO.getOrderId());
		p.setProdId(pDTO.getProdId());
		p.setQuantity(pDTO.getQuantity());
		p.setSellerId(pDTO.getSellerId());
		p.setStatus(pDTO.getStatus());
		ProductsOrdered p2=proRepository.save(p);
		return p2.getOrderId();
	}

}
