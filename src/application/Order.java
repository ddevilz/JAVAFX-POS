package application;

import java.time.LocalDate;

public class Order {
    private int orderNumber;
    private LocalDate dueDate;
    private String dueTime;
    private String customer;
    private int totalQuantity;
    private OrderStatus orderStatus;
    
	public Order(int orderNumber, LocalDate dueDate, String dueTime, String customer, int totalQuantity,
			OrderStatus orderStatus) {
		this.orderNumber = orderNumber;
		this.dueDate = dueDate;
		this.dueTime = dueTime;
		this.customer = customer;
		this.totalQuantity = totalQuantity;
		this.orderStatus = orderStatus;
	}
	
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public String getDueTime() {
		return dueTime;
	}
	public void setDueTime(String dueTime) {
		this.dueTime = dueTime;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

  
	
	    

}
