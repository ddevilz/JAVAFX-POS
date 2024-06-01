package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ServiceDAO {
	private Connection connection;

    public ServiceDAO() {
        connection = Database.connectDB();
    }

    public List<Service> getAllServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM item_master";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Service service = new Service(
                    resultSet.getString("ino"),
                    resultSet.getString("iname"),
                    resultSet.getString("itype"),
                    resultSet.getDouble("rate1"),
                    resultSet.getDouble("rate2"),
                    resultSet.getDouble("rate3"),
                    resultSet.getDouble("rate4"),
                    resultSet.getDouble("rate5")
                );
                services.add(service);
            }
        }
        return services;
    }

    public boolean addService(Service service) {
        String query = "INSERT INTO item_master (ino, iname, itype, rate1, rate2, rate3, rate4, rate5) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, service.getServiceId());
            preparedStatement.setString(2, service.getName());
            preparedStatement.setString(3, service.getType());
            preparedStatement.setDouble(4, service.getRate1());
            preparedStatement.setDouble(5, service.getRate2());
            preparedStatement.setDouble(6, service.getRate3());
            preparedStatement.setDouble(7, service.getRate4());
            preparedStatement.setDouble(8, service.getRate5());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteService(Service service) {
        String query = "DELETE FROM item_master WHERE ino = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, service.getServiceId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
