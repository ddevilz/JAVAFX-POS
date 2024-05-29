package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
 import javafx.scene.control.cell.PropertyValueFactory;
 import javafx.scene.input.KeyCode;
 import javafx.scene.input.KeyEvent;
 import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

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

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button order_btn;

    @FXML
    private Button order_detail_btn;

    @FXML
    private AnchorPane dashboard_page;

    @FXML
    private AnchorPane take_a_order;
    
    @FXML
    private AnchorPane order_status;

    @FXML
    private Button order_status_btn;
    
    @FXML
    private AnchorPane view_order_detail;

    @FXML
    private TableView<Customer> resultsTable;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    private Connection connection;

    private long lastKeyPressTime = 0;
    private static final long DEBOUNCE_DELAY = 300; // milliseconds

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initializing controller...");
        connection = Database.connectDB();
        if (connection != null) {
            System.out.println("Database connection established.");
        } else {
            System.out.println("Failed to establish database connection.");
        }

        phoneNum.setEditable(true);
        customerName.setEditable(true);
        customerAddress.setEditable(true);

        // Set up TableView columns
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        addressColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));

        // Add listener for row selection
        resultsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Customer selected: " + newValue.getName());
                setCustomerDetails(newValue);
            }
        });

        // Set up key listener for phoneNum TextField
        phoneNum.setOnKeyReleased(this::numOnChange);
    }

    public void switchForm(ActionEvent event) {
        System.out.println("Switching forms...");
        if (event.getSource() == dashboard_btn) {
            System.out.println("Switching to dashboard page.");
            dashboard_page.setVisible(true);
            take_a_order.setVisible(false);
            view_order_detail.setVisible(false);
            order_status.setVisible(false);
        } else if (event.getSource() == order_btn) {
            System.out.println("Switching to take order page.");
            dashboard_page.setVisible(false);
            take_a_order.setVisible(true);
            view_order_detail.setVisible(false);
            order_status.setVisible(false);
        } else if (event.getSource() == order_detail_btn) {
            System.out.println("Switching to view order detail page.");
            dashboard_page.setVisible(false);
            take_a_order.setVisible(false);
            view_order_detail.setVisible(true);
            order_status.setVisible(false);
        } else if (event.getSource() == order_status_btn) {
        	dashboard_page.setVisible(false);
            take_a_order.setVisible(false);
            view_order_detail.setVisible(false);
            order_status.setVisible(true);
		}
    }

    public void numOnChange(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.BACK_SPACE || event.getEventType() == KeyEvent.KEY_RELEASED) {
            // Handle backspace
            handleBackspace();
        } else {
            // Handle regular character input
            lastKeyPressTime = System.currentTimeMillis();
            String phoneNumber = phoneNum.getText().trim();
            if (!phoneNumber.isEmpty()) {
                phoneNumber += event.getCharacter();
                System.out.println("Phone number after debounce: " + phoneNumber);
                fetchCustomerDetailsByPhoneNumber(phoneNumber);
            } else {
                clearCustomerDetails();
            }
        }
    }


    private void handleBackspace() {
        String phoneNumber = phoneNum.getText().trim();
        if (!phoneNumber.isEmpty()) {
            // Remove the last character from the phone number
            phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
            System.out.println("Phone number after backspace: " + phoneNumber);
            fetchCustomerDetailsByPhoneNumber(phoneNumber);
        } else {
            // If the phone number is empty, clear customer details
            clearCustomerDetails();
        }
    }
    
    private void fetchCustomerDetailsByPhoneNumber(String phoneNumber) {
        System.out.println("Fetching customer details for phone number: " + phoneNumber);
        String query = "SELECT * FROM customer WHERE mobile LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the parameter value in the prepared statement
            preparedStatement.setString(1, phoneNumber + "%");

            // Print the SQL query statement
            System.out.println("Query: " + preparedStatement.toString());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ObservableList<Customer> customers = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    String customerName = resultSet.getString("cname");
                    String customerAddress = resultSet.getString("add1");
                    String customerMobile = resultSet.getString("mobile");
                    
                    // Print each row of the result set
                    System.out.println("Customer Name: " + customerName + ", Address: " + customerAddress + ", Mobile: " + customerMobile);
                    
                    // Add the customer to the list
                    customers.add(new Customer(customerName, customerAddress, customerMobile));
                }
                System.out.println("Number of customers found: " + customers.size());
                if (!customers.isEmpty()) {
                    resultsTable.setItems(customers);
                } else {
                    clearCustomerDetails();
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            // Handle other exceptions
            System.err.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private void setCustomerDetails(Customer customer) {
        System.out.println("Setting customer details: " + customer.getName());
        customerName.setText(customer.getName());
        customerAddress.setText(customer.getAddress());
    }

    private void clearCustomerDetails() {
        System.out.println("Clearing customer details.");
        customerName.clear();
        customerAddress.clear();
    }

    private void showAlert(String title, String content) {
        System.out.println("Showing alert: " + title + " - " + content);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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

    public void printCard(ActionEvent event) {
        System.out.println("Printing card...");
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                System.out.println("Closing database connection.");
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finalize() {
        System.out.println("Finalizing controller...");
        closeConnection();
    }
}
