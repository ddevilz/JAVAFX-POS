package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;

import java.sql.*;

public class mainFormController implements Initializable {

    @FXML
    private TextField phoneNum;

    @FXML
    private TextField customerName;

    @FXML
    private TextField customerAddress;

    private Connection connection;

    @FXML
    void numOnChange(KeyEvent event) {
        String phoneNumber = phoneNum.getText();
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            fetchCustomerDetailsByPhoneNumber(phoneNumber);
        } else {
            customerName.setText("");
            customerAddress.setText("");
        }
    }

    private void fetchCustomerDetailsByPhoneNumber(String phoneNumber) {
        String query = "SELECT * FROM customer WHERE mobile = ?";
        connection = Database.connectDB();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                Customer customer = new Customer(
                    resultSet.getString("cname"),
                    resultSet.getString("add1"),
                    phoneNumber
                );
                
                setCustomerDetails(customer);
            } else {
                showAlert("Customer not found", "No customer found with this phone number.");
                customerName.setText("");
                customerAddress.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setCustomerDetails(Customer customer) {
        customerName.setText(customer.getName());
        customerAddress.setText(customer.getAddress());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phoneNum.setEditable(true);
        customerName.setEditable(true);
        customerAddress.setEditable(true);
    }
}

