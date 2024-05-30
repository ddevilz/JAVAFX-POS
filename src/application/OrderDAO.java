package application;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
	private Connection connection;

    public OrderDAO() {
        this.connection = Database.connectDB(); // Assume Database.connectDB() establishes the connection
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders"; // Replace with your actual table name and query

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

               while (rs.next()) {
                   int orderNumber = rs.getInt("orderNumber");
                   String dueDateString = rs.getString("DueDate");
                   LocalDate dueDate = LocalDate.parse(dueDateString); 
                   String dueTime = rs.getString("DueTime");
                   String customer = rs.getString("Customer");
                   int totalQuantity = rs.getInt("totalQuantity");
                   String statusFromDatabase = rs.getString("orderStatus");
                   OrderStatus orderStatus = OrderStatus.valueOf(statusFromDatabase.toUpperCase());


                   orders.add(new Order(orderNumber, dueDate, dueTime, customer, totalQuantity, orderStatus));
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }

        return orders;
    }

}
