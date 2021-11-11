package com.infy.service;

import java.util.List;

import com.infy.dto.OrderDTO;
import com.infy.dto.ProductsOrderedDTO;
import com.infy.entity.ProductsOrdered;
import com.infy.exception.OrderException;

public interface ProductsOrderedService {
	public List<ProductsOrderedDTO> viewProductOrderedDetails() throws OrderException;
	public String addProductsOrder(ProductsOrderedDTO pDTO) throws OrderException;
}
