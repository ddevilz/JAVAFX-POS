package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
 import javafx.scene.input.KeyCode;
 import javafx.scene.input.KeyEvent;
 import javafx.scene.layout.AnchorPane;

import java.sql.*;
import java.time.LocalDate;

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
    private TableColumn<Order, OrderStatus> orderStatusColumn;

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
    private Button  add_show_category_service_btn;
    
    @FXML
    private AnchorPane add_show_category_service_page;
    
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
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private TableColumn<Category, String> catIdColumn;
    
    @FXML
    private TableColumn<Category, String> categoryNameColumn;
    
    @FXML
    private Button resetButton;
    
    @FXML
    private TextField categoryNameField;
    
    @FXML
    private TextField categoryIdField;

    private CategoryDAO categoryDAO;
    
    @FXML
    private TableView<Category> categoryTable;

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
        
        categoryDAO = new CategoryDAO();
        
        initializeOrderTable();

        resetDatePicker();
        
        datePicker.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                fetchOrdersForDate(selectedDate);
            }
        });
        
        // Load data from the database and populate the table
        initializeCategoryTable();
        loadCategoryDataTable();
       
        
    }
    private void initializeCategoryTable() {
        catIdColumn.setCellValueFactory(new PropertyValueFactory<>("catid"));
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
    }
    private void loadCategoryDataTable() {
        try {
            ObservableList<Category> categoryList = FXCollections.observableArrayList(categoryDAO.getAllCategories());
            categoryTable.setItems(categoryList);
        } catch (SQLException e) {
            System.err.println("Error loading categories: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void resetDatePicker() {
        datePicker.setValue(null);
        loadAllOrders();
    }
    
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadCategoryDataTable();
    }

    private void loadAllOrders() {
        orderTable.getItems().clear();
        ObservableList<Order> orders = FXCollections.observableArrayList(orderDAO.getOrders());
        orderTable.setItems(orders);
    }
    
    private void fetchOrdersForDate(LocalDate date) {
        orderTable.getItems().clear();

        String query = "SELECT * FROM orders WHERE DueDate = ?";

        try (Connection connection = Database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, java.sql.Date.valueOf(date));

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<Order> orders = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int orderNumber = resultSet.getInt("orderNumber");
                String dueDateString = resultSet.getString("DueDate");
                LocalDate dueDate = LocalDate.parse(dueDateString);
                String dueTime = resultSet.getTime("DueTime").toString();
                String customer = resultSet.getString("Customer");
                int totalQuantity = resultSet.getInt("totalQuantity");
                String orderStatus = resultSet.getString("orderStatus");

                Order order = new Order(orderNumber, dueDate, dueTime, customer, totalQuantity, OrderStatus.valueOf(orderStatus));
                orders.add(order);
            }
            orderTable.setItems(orders);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to fetch orders for the selected date.");
        }
    }


    public void switchForm(ActionEvent event) {
        System.out.println("Switching forms...");
        if (event.getSource() == dashboard_btn) {
            System.out.println("Switching to dashboard page.");
            dashboard_page.setVisible(true);
            take_a_order.setVisible(false);
            view_order_detail.setVisible(false);
            order_status.setVisible(false);
            add_show_category_service_page.setVisible(false);
            
        } else if (event.getSource() == order_btn) {
            System.out.println("Switching to take order page.");
            dashboard_page.setVisible(false);
            take_a_order.setVisible(true);
            view_order_detail.setVisible(false);
            order_status.setVisible(false);
            add_show_category_service_page.setVisible(false);
            
        } else if (event.getSource() == order_detail_btn) {
            System.out.println("Switching to view order detail page.");
            dashboard_page.setVisible(false);
            take_a_order.setVisible(false);
            view_order_detail.setVisible(true);
            order_status.setVisible(false);
            add_show_category_service_page.setVisible(false);
            
        } else if (event.getSource() == order_status_btn) {

            System.out.println("Switching to order status page");
        	dashboard_page.setVisible(false);
            take_a_order.setVisible(false);
            view_order_detail.setVisible(false);
            order_status.setVisible(true);
            add_show_category_service_page.setVisible(false);
            
		}
        else if (event.getSource() == add_show_category_service_btn) {

            System.out.println("Switching to add category/service page.");
        	dashboard_page.setVisible(false);
            take_a_order.setVisible(false);
            view_order_detail.setVisible(false);
            order_status.setVisible(false);
            add_show_category_service_page.setVisible(true);
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
//        alert.showAndWait();
    }

    private void updateOrderStatusInDatabase(Order order) {
        // Assuming you have a connection to your database
        PreparedStatement preparedStatement = null;

        try {
            // Prepare the update query
            String updateQuery = "UPDATE orders SET orderStatus = ? WHERE orderNumber = ?";
            
            // Create a prepared statement
            preparedStatement = connection.prepareStatement(updateQuery);
            
            // Set the parameters
            preparedStatement.setString(1, order.getOrderStatus().toString());
            preparedStatement.setInt(2, order.getOrderNumber());
            
            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            
            // Check if the update was successful
            if (rowsAffected > 0) {
                System.out.println("Order status updated successfully.");
            } else {
                System.out.println("Failed to update order status.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


    private void initializeOrderTable() {
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dueTime"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        totalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        
        // Set cell value factory for orderStatusColumn
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        System.out.println("hello");
        // Set up the orderStatusColumn with a ComboBoxTableCell
        orderStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(OrderStatus.values()));
        System.out.println("hello");

        // Add a listener to handle the edit commit event when user selects an option
        orderStatusColumn.setOnEditCommit(event -> {
            TablePosition<Order, OrderStatus> position = event.getTablePosition();
            Order order = event.getTableView().getItems().get(position.getRow());
            OrderStatus newStatusValue = (OrderStatus) event.getNewValue();
            order.setOrderStatus(newStatusValue);

            // Now you can update the database with the new order status
            updateOrderStatusInDatabase(order);
        });
        System.out.println("hello");
        // Load orders from the database and set them to the table
        ObservableList<Order> orders = FXCollections.observableArrayList(orderDAO.getOrders());
        orderTable.setItems(orders);
    }


    public void printCard(ActionEvent event) {
        System.out.println("Printing card...");
    }

    @FXML
    private void handleSubmit() {
        String categoryName = categoryNameField.getText().trim();
        String categoryId = categoryIdField.getText().trim(); 
        if (categoryName.isEmpty() || categoryId.isEmpty()) {
            showAlert("Error", "Category name and ID cannot be empty.");
            return;
        }

        boolean success = categoryDAO.addCategory(categoryName, categoryId);
        if (success) {
            showAlert("Success", "Category added successfully.");
            categoryNameField.clear();
            categoryIdField.clear(); 
        } else {
            showAlert("Error", "Failed to add category.");
        }
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
