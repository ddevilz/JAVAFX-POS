package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.sql.*;

public class mainFormController implements Initializable {

    @FXML
    private TextField phoneNum;

    @FXML
    private TextField customerName;

    @FXML
    private TextField customerAddress;
    
    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, String> orderNumberColumn;

    @FXML
    private TableColumn<Order, String> dueDateColumn;

    @FXML
    private TableColumn<Order, String> dueTimeColumn;

    @FXML
    private TableColumn<Order, String> customerColumn;

    @FXML
    private TableColumn<Order, Integer> totalQuantityColumn;

    @FXML
    private TableColumn<Order, String> orderStatusColumn;

    private OrderDAO orderDAO = new OrderDAO();

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
    
    @FXML
    public void initializeOrder() {
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dueTime"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        totalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

        ObservableList<Order> orders = FXCollections.observableArrayList(orderDAO.getOrders());
        orderTable.setItems(orders);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phoneNum.setEditable(true);
        customerName.setEditable(true);
        customerAddress.setEditable(true);
        initializeOrder();
    }
}

