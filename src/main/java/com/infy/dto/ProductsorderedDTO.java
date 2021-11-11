package com.infy.dto;


public class ProductsorderedDTO {
	
	private String orderId;
	private String buyerId;
	private String prodId;
	private String sellerId;
	private Integer quantity;
	private String status;
	
	public String getBuyerId() {
		return buyerId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "ProductsorderedDTO [buyerId=" + buyerId + ", prodId=" + prodId + ", sellerId=" + sellerId
				+ ", quantity=" + quantity + "]";
	}
	
}
