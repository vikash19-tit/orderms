package com.infy.service;

import java.util.List;

import com.infy.dto.OrderDTO;
import com.infy.dto.ProductsorderedDTO;
import com.infy.entity.Productsordered;
import com.infy.exception.OrderException;

public interface ProductsorderedService {
	public List<ProductsorderedDTO> viewProductOrderedDetails() throws OrderException;
	public String addProductsOrder(ProductsorderedDTO pDTO) throws OrderException;
}
