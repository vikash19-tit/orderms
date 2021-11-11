package com.infy.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {

	private String orderId;
	private String buyerId;
	private Integer amount;
	private LocalDate date;
	private String address;
	private String status;
	private List<ProductsorderedDTO> productsOrdered;
	public List<ProductsorderedDTO> getProductsOrdered() {
		return productsOrdered;
	}
	public void setProductsOrdered(List<ProductsorderedDTO> productsOrdered) {
		this.productsOrdered = productsOrdered;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "OrderDTO [orderId=" + orderId + ", buyerId=" + buyerId + ", amount=" + amount + ", date=" + date
				+ ", address=" + address + ", status=" + status + "]";
	}
	
}
