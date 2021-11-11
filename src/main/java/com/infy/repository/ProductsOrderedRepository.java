package com.infy.repository;

import org.springframework.data.repository.CrudRepository;

import com.infy.entity.ProductsOrdered;

public interface ProductsOrderedRepository extends CrudRepository<ProductsOrdered, String> {

}
