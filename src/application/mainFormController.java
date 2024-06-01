package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
	    private Button add_show_category_service_btn;
	    @FXML
	    private AnchorPane add_show_category_service_page;
	    @FXML
	    private AnchorPane view_order_detail;
	    private OrderDAO orderDAO = new OrderDAO();
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

	    @FXML
	    private AnchorPane mainContainer;
	    @FXML
	    private ScrollPane categoryScrollPane;
	    @FXML
	    private AnchorPane categoryContainer;
	    @FXML
	    private ScrollPane itemScrollPane;
	    @FXML
	    private AnchorPane itemContainer;
	    @FXML
	    private TableView<Item> serviceTable;
	    @FXML
	    private TableColumn<Item, String> noColumn;
	    @FXML
	    private TableColumn<Item, String> serviceColumn;
	    @FXML
	    private TableColumn<Item, Double> priceColumn;
	    @FXML
	    private TableColumn<Item, Integer> quantity;
	    @FXML
	    private TableColumn<Item, Double> totalColumn;
	    @FXML
	    private TextField textField1;
	    @FXML
	    private TextField textField2;
	    @FXML
	    private TextField textField3;
	    @FXML
	    private TextField textField4;
	    @FXML
	    private Button backButton;
	    @FXML
	    private Button printReceiptButton;
	    @FXML
	    private Label totalAmountLabel;
	    @FXML
	    private TextField discountField;
	    @FXML
	    private TextField advancePaymentField;
	    @FXML
	    private Label finalAmountLabel;
	    @FXML
	    private Button applyDiscountButton;
	    @FXML
	    private Button applyAdvanceButton;

	    private Connection connection;

	    @Override
	    public void initialize(URL url, ResourceBundle resourceBundle) {
	        System.out.println("Initializing controller...");
	        connection = Database.connectDB();
	        if (connection != null) {
	            System.out.println("Database connection established.");
	        } else {
	            System.out.println("Failed to establish database connection.");
	        }

	        // Set TextField properties
	        phoneNum.setEditable(true);
	        customerName.setEditable(true);
	        customerAddress.setEditable(true);

	        // Initialize Customer TableView columns
	        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
	        addressColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));
	        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));

	        // Listener for row selection in Customer TableView
	        resultsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	            if (newValue != null) {
	                System.out.println("Customer selected: " + newValue.getName());
	                setCustomerDetails(newValue);
	            }
	        });

	        // Key listener for phoneNum TextField
	        phoneNum.setOnKeyReleased(this::numOnChange);

	        // Initialize DAOs
	        categoryDAO = new CategoryDAO();

	        // Initialize Order Table and Date Picker
	        initializeOrderTable();
	        resetDatePicker();
	        datePicker.setOnAction(event -> {
	            LocalDate selectedDate = datePicker.getValue();
	            if (selectedDate != null) {
	                fetchOrdersForDate(selectedDate);
	            }
	        });
	        
	        noColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIndex())));
	        serviceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServiceName()));
	        quantity.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
	        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
//	        totalColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalPrice()).asObject());

	        // Load categories and initialize Category Table
	        loadCategories();
	        initializeCategoryTable();
	        loadCategoryDataTable();
	    }

	    // Action handler for back button
	    @FXML
	    private void handleBackButtonAction(ActionEvent event) {
	        categoryScrollPane.setVisible(true);
	        itemScrollPane.setVisible(false);
	    }
	    
	    

	    // Load categories from the database
	    private void loadCategories() {
	        ObservableList<String> categories = getCategoriesFromDB();
	        double layoutX = 23.0;
	        double layoutY = 14.0;
	        double paneWidth = 135.0;
	        double paneHeight = 139.0;
	        double padding = 10.0;
	        double containerWidth = categoryContainer.getPrefWidth();

	        for (String category : categories) {
	            AnchorPane categoryPane = createCategoryPane(category, layoutX, layoutY);
	            categoryContainer.getChildren().add(categoryPane);

	            layoutX += paneWidth + padding;
	            if (layoutX + paneWidth + padding > containerWidth) {
	                layoutX = 23.0;
	                layoutY += paneHeight + padding;
	            }
	        }
	    }

	    // Fetch categories from the database
	    private ObservableList<String> getCategoriesFromDB() {
	        ObservableList<String> categories = FXCollections.observableArrayList();
	        String query = "SELECT category FROM item_category"; // Adjust table and column names
	        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                categories.add(rs.getString("category"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return categories;
	    }

	    // Create category pane
	    private AnchorPane createCategoryPane(String category, double layoutX, double layoutY) {
	        AnchorPane pane = new AnchorPane();
	        pane.setLayoutX(layoutX);
	        pane.setLayoutY(layoutY);
	        pane.setPrefHeight(139.0);
	        pane.setPrefWidth(135.0);
	        pane.getStyleClass().addAll("white-form", "shadow");

	        Label label = new Label(category);
	        label.setAlignment(javafx.geometry.Pos.CENTER);
	        label.setPrefHeight(139.0);
	        label.setPrefWidth(135.0);
	        label.getStyleClass().add("category-label");

	        pane.getChildren().add(label);

	        pane.setOnMouseClicked(event -> showServices(category));

	        return pane;
	    }
	    @FXML
	    private void onPrintButtonClick() {
	        Order order = new Order();
	        order.setSno(Integer.parseInt(snoField.getText()));
	        order.setOnum(Integer.parseInt(onumField.getText()));
	        order.setOdate(java.sql.Date.valueOf(odateField.getValue()));
	        order.setOtime(otimeField.getText());
	        order.setRtype(rtypeField.getText());
	        order.setCid(Integer.parseInt(cidField.getText()));
	        order.setCname(cnameField.getText());
	        order.setMobile(mobileField.getText());
	        order.setItems(Integer.parseInt(itemsField.getText()));
	        order.setQuans(Double.parseDouble(quansField.getText()));
	        order.setSub(Double.parseDouble(subField.getText()));
	        order.setDisp(Double.parseDouble(dispField.getText()));
	        order.setDisamt(Double.parseDouble(disamtField.getText()));
	        order.setTax(Double.parseDouble(taxField.getText()));
	        order.setGross(Double.parseDouble(grossField.getText()));
	        order.setRof(Double.parseDouble(rofField.getText()));
	        order.setNet(Double.parseDouble(netField.getText()));
	        order.setAd(Double.parseDouble(adField.getText()));
	        order.setPby(pbyField.getText());
	        order.setBal(Double.parseDouble(balField.getText()));
	        order.setDtype(dtypeField.getText());
	        order.setNop(Double.parseDouble(nopField.getText()));
	        order.setDdate(java.sql.Date.valueOf(ddateField.getValue()));
	        order.setDtime1(Time.valueOf(dtime1Field.getText()));
	        order.setDtime2(dtime2Field.getText());
	        order.setDtime3(dtime3Field.getText());
	        order.setRemarks(remarksField.getText());
	        order.setStatus(statusField.getText());
	        order.setCompany(companyField.getText());
	        order.setCash(Double.parseDouble(cashField.getText()));
	        order.setCard(Double.parseDouble(cardField.getText()));
	        order.setNetb(Double.parseDouble(netbField.getText()));
	        order.setOthers(Double.parseDouble(othersField.getText()));
	        order.setTaxp(Double.parseDouble(taxpField.getText()));

	        // Handle the order object (e.g., save to database, print)
	        System.out.println(order);
	    }
	    // Show services for a category
	    private void showServices(String category) {
	        categoryScrollPane.setVisible(false);
	        itemScrollPane.setVisible(true);
	        loadItems(category);
	    }
	    @FXML
	    private void handleAddButtonAction(ActionEvent event) {
	        String serviceName = textField1.getText();
	        String priceText = textField2.getText();
	        String quantityText = textField3.getText();

	        if (!isNumeric(quantityText) || !isNumeric(priceText)) {
	            showAlert("Invalid Input", "Quantity and price must be numeric values.");
	            return;
	        }

	        int quantity = Integer.parseInt(quantityText);
	        double price = Double.parseDouble(priceText);
	        double totalPrice = price * quantity;

	        String index = String.valueOf(serviceTable.getItems().size() + 1);
	        Item newItem = new Item(serviceName, quantity, price, totalPrice);

	        serviceTable.getItems().add(newItem);
	        updateTotalAmount();

	        textField1.clear();
	        textField2.clear();
	        textField3.clear();
	    }

	    private boolean isNumeric(String str) {
	        return str.matches("-?\\d+(\\.\\d+)?");
	    }
	    private void updateTotalAmount() {
	        double total = serviceTable.getItems().stream()
	                .mapToDouble(Item::getTotalPrice)
	                .sum();
	        totalAmountLabel.setText(String.valueOf(total));
	        updateFinalAmount();
	    }

	    @FXML
	    private void applyDiscount(ActionEvent event) {
	        updateFinalAmount();
	    }

	    @FXML
	    private void applyAdvance(ActionEvent event) {
	        updateFinalAmount();
	    }

	    private void updateFinalAmount() {
	        double totalAmount = Double.parseDouble(totalAmountLabel.getText());
	        double discount = discountField.getText().isEmpty() ? 0 : Double.parseDouble(discountField.getText());
	        double advancePayment = advancePaymentField.getText().isEmpty() ? 0 : Double.parseDouble(advancePaymentField.getText());
	        double finalAmount = totalAmount - discount - advancePayment;
	        finalAmountLabel.setText(String.valueOf(finalAmount));
	    }
	    
	    // Load items for a category from the database
	    private void loadItems(String category) {
	        itemContainer.getChildren().clear();
	        ObservableList<Item> items = FXCollections.observableArrayList();
	        String query = "SELECT iname, rate1 FROM item_master WHERE itype = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setString(1, category);
	            ResultSet rs = stmt.executeQuery();
	            double layoutX = 23.0;
	            double layoutY = 14.0;
	            double paneWidth = 135.0;
	            double paneHeight = 139.0;
	            double padding = 10.0;
	            double containerWidth = itemContainer.getPrefWidth();

	            while (rs.next()) {
	                String iname = rs.getString("iname");
	                double rate1 = rs.getDouble("rate1");
	                Item item = new Item(0, iname, rate1);
	                items.add(item);

	                AnchorPane itemPane = createItemPane(item, layoutX, layoutY);
	                itemContainer.getChildren().add(itemPane);

	                layoutX += paneWidth + padding;
	                if (layoutX + paneWidth + padding > containerWidth) {
	                    layoutX = 23.0;
	                    layoutY += paneHeight + padding;
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Create item pane
	    private AnchorPane createItemPane(Item item, double layoutX, double layoutY) {
	        AnchorPane pane = new AnchorPane();
	        pane.setLayoutX(layoutX);
	        pane.setLayoutY(layoutY);
	        pane.setPrefHeight(139.0);
	        pane.setPrefWidth(135.0);
	        pane.getStyleClass().addAll("white-form", "shadow");

	        Label label = new Label(item.getServiceName() + "\nPrice: " + item.getPrice());
	        label.setAlignment(javafx.geometry.Pos.CENTER);
	        label.setPrefHeight(139.0);
	        label.setPrefWidth(135.0);
	        label.getStyleClass().add("category-label");

	        pane.getChildren().add(label);

	        pane.setOnMouseClicked(event -> handleItemClicked(item));

	        return pane;
	    }

	    // Handle item clicked event
	    private void handleItemClicked(Item item) {
	        textField1.setText(item.getServiceName());
	        textField2.setText(String.valueOf(item.getPrice()));
	        textField3.setText(String.valueOf(1));
	    }

	    // Initialize category table
	    private void initializeCategoryTable() {
	        catIdColumn.setCellValueFactory(new PropertyValueFactory<>("catid"));
	        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
	    }

	    // Load category data into table
	    private void loadCategoryDataTable() {
	        try {
	            ObservableList<Category> categoryList = FXCollections.observableArrayList(categoryDAO.getAllCategories());
	            categoryTable.setItems(categoryList);
	        } catch (SQLException e) {
	            System.err.println("Error loading categories: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    // Reset date picker and load all orders
	    @FXML
	    private void resetDatePicker() {
	        datePicker.setValue(null);
	        loadAllOrders();
	    }

	    // Handle refresh button action
	    @FXML
	    private void handleRefresh(ActionEvent event) {
	        loadCategoryDataTable();
	    }

	    // Load all orders from the database
	    private void loadAllOrders() {
	        orderTable.getItems().clear();
	        ObservableList<Order> orders = FXCollections.observableArrayList(orderDAO.getOrders());
	        orderTable.setItems(orders);
	    }

	    // Fetch orders for a specific date
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

	    // Key listener for phone number text field
	    public void numOnChange(KeyEvent event) {
	        KeyCode keyCode = event.getCode();
	        if (keyCode == KeyCode.BACK_SPACE || event.getEventType() == KeyEvent.KEY_RELEASED) {
	            handleBackspace();
	        } else {
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

	    // Handle backspace in phone number field
	    private void handleBackspace() {
	        String phoneNumber = phoneNum.getText().trim();
	        if (!phoneNumber.isEmpty()) {
	            phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
	            System.out.println("Phone number after backspace: " + phoneNumber);
	            fetchCustomerDetailsByPhoneNumber(phoneNumber);
	        } else {
	            clearCustomerDetails();
	        }
	    }

	    // Fetch customer details by phone number
	    private void fetchCustomerDetailsByPhoneNumber(String phoneNumber) {
	        System.out.println("Fetching customer details for phone number: " + phoneNumber);
	        String query = "SELECT * FROM customer WHERE mobile LIKE ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, phoneNumber + "%");

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                ObservableList<Customer> customers = FXCollections.observableArrayList();
	                while (resultSet.next()) {
	                    String customerName = resultSet.getString("cname");
	                    String customerAddress = resultSet.getString("add1");
	                    String customerMobile = resultSet.getString("mobile");

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
	            System.err.println("SQL Exception: " + e.getMessage());
	            e.printStackTrace();
	        } catch (Exception ex) {
	            System.err.println("Exception: " + ex.getMessage());
	            ex.printStackTrace();
	        }
	    }

	    // Set customer details in text fields
	    private void setCustomerDetails(Customer customer) {
	        customerName.setText(customer.getName());
	        customerAddress.setText(customer.getAddress());
	    }

	    // Clear customer details from text fields
	    private void clearCustomerDetails() {
	        customerName.clear();
	        customerAddress.clear();
	    }

	    // Show alert dialog
	    private void showAlert(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
	



//    private void setCustomerDetails(Customer customer) {
//        System.out.println("Setting customer details: " + customer.getName());
//        customerName.setText(customer.getName());
//        customerAddress.setText(customer.getAddress());
//    }
//
//    private void clearCustomerDetails() {
//        System.out.println("Clearing customer details.");
//        customerName.clear();
//        customerAddress.clear();
//    }
//
//    private void showAlert(String title, String content) {
//        System.out.println("Showing alert: " + title + " - " + content);
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setContentText(content);
//    }

   
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
}
