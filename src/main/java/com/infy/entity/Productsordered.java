package com.infy.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Productsordered {
	@Id
	@Column(name="prod_id")
	private String prodId;
	@Column(name="order_id")
	private String orderId;
	@Column(name="buyer_id")
	private String buyerId;
	@Column(name="seller_id")
	private String sellerId;
	
	private Integer quantity;
	private String status;
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

	public String getBuyerId() {
		return buyerId;
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
	public int hashCode() {
		return Objects.hash(buyerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Productsordered other = (Productsordered) obj;
		return Objects.equals(buyerId, other.buyerId);
	}

	@Override
	public String toString() {
		return "Productsordered [buyerId=" + buyerId + ", prodId=" + prodId + ", sellerId=" + sellerId + ", quantity="
				+ quantity + "]";
	}
	

}
