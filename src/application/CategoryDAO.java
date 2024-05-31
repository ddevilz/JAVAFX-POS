package application;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CategoryDAO {
	 private Connection connection;

	    public CategoryDAO() {
	        connection = Database.connectDB();
	    }

	    public boolean addCategory(String categoryName, String categoryId) {
	        String query = "INSERT INTO item_category (category, catid) VALUES (?, ?)";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, categoryName);
	            preparedStatement.setString(2, categoryId);
	            System.out.println("Executing query: " + preparedStatement);
	            int rowsAffected = preparedStatement.executeUpdate();
	            return rowsAffected > 0;
	        } catch (SQLException e) {
	            System.err.println("SQL Exception: " + e.getMessage());
	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    public List<Category> getAllCategories() throws SQLException {
	        List<Category> categories = new ArrayList<>();
	        String query = "SELECT * FROM item_category";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
	             ResultSet resultSet = preparedStatement.executeQuery()) {
	            while (resultSet.next()) {
	                String categoryName = resultSet.getString("category");
	                String categoryId = resultSet.getString("catid");
	                categories.add(new Category(categoryId, categoryName));
	            }
	        }
	        return categories;
	    }


}
