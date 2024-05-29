package application;

public class Order {
	  	private String orderNumber;
	    private String dueDate;
	    private String dueTime;
	    private String customer;
	    private int totalQuantity;
	    private String orderStatus;
	    
		public Order(String orderNumber, String dueDate, String dueTime, String customer, int totalQuantity,
				String orderStatus) {
			this.orderNumber = orderNumber;
			this.dueDate = dueDate;
			this.dueTime = dueTime;
			this.customer = customer;
			this.totalQuantity = totalQuantity;
			this.orderStatus = orderStatus;
		}

		public String getOrderNumber() {
			return orderNumber;
		}

		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}

		public String getDueDate() {
			return dueDate;
		}

		public void setDueDate(String dueDate) {
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

		public String getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}
		
	    
	    

}
