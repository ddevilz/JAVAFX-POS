package application;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class OrderDAO {
	 public List<Order> getOrders() {
	        List<Order> orders = new ArrayList<>();
	        String query = "SELECT order_number, due_date, due_time, customer, total_quantity, order_status FROM orders";

	        try (Connection connection = Database.connectDB();
	             Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(query)) {

	            while (resultSet.next()) {
	                String orderNumber = resultSet.getString("order_number");
	                String dueDate = resultSet.getString("due_date");
	                String dueTime = resultSet.getString("due_time");
	                String customer = resultSet.getString("customer");
	                int totalQuantity = resultSet.getInt("total_quantity");
	                String orderStatus = resultSet.getString("order_status");

	                orders.add(new Order(orderNumber, dueDate, dueTime, customer, totalQuantity, orderStatus));
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return orders;
	    }

}
