package application;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	    
	    @FXML
	    private TextField serviceNameField;
	    @FXML
	    private TextField serviceTypeField;
	    @FXML
	    private TextField rate1Field;
	    @FXML
	    private TextField rate2Field;
	    @FXML
	    private TextField rate3Field;
	    @FXML
	    private TextField rate4Field;
	    @FXML
	    private TextField rate5Field;

	    private ServiceDAO serviceDAO;

		 @FXML
		    private TableView<Service> serviceTable1;
		    @FXML
		    private TableColumn<Service, String> serviceIdColumn;
		    @FXML
		    private TableColumn<Service, String> serviceNameColumn;
		    @FXML
		    private TableColumn<Service, String> serviceTypeColumn;
		    @FXML
		    private TableColumn<Service, Double> rate1Column;
		    @FXML
		    private TableColumn<Service, Double> rate2Column;
		    @FXML
		    private TableColumn<Service, Double> rate3Column;
		    @FXML
		    private TableColumn<Service, Double> rate4Column;
		    @FXML
		    private TableColumn<Service, Double> rate5Column;
	    
	    private Connection connection;
	    private static final AtomicInteger counter = new AtomicInteger(1000);
	    private static final Random random = new Random();
	    
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
	        serviceDAO = new ServiceDAO();
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
	        loadServices();
	        initializeServiceTable();
	    }
	    private void initializeServiceTable() {
	        serviceIdColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
	        serviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
	        serviceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
	        rate1Column.setCellValueFactory(new PropertyValueFactory<>("rate1"));
	        rate2Column.setCellValueFactory(new PropertyValueFactory<>("rate2"));
	        rate3Column.setCellValueFactory(new PropertyValueFactory<>("rate3"));
	        rate4Column.setCellValueFactory(new PropertyValueFactory<>("rate4"));
	        rate5Column.setCellValueFactory(new PropertyValueFactory<>("rate5"));
	    }
	    private void clearServiceFormFields() {
		    serviceNameField.clear();
		    serviceTypeField.clear();
		    rate1Field.clear();
		    rate2Field.clear();
		    rate3Field.clear();
		    rate4Field.clear();
		    rate5Field.clear();
		}
	    @FXML
		void submitServiceForm(ActionEvent event) {
			String name = serviceNameField.getText();
			String type = serviceTypeField.getText();
			double rate1 = Double.parseDouble(rate1Field.getText());
			double rate2 = Double.parseDouble(rate2Field.getText());
			double rate3 = Double.parseDouble(rate3Field.getText());
			double rate4 = Double.parseDouble(rate4Field.getText());
			double rate5 = Double.parseDouble(rate5Field.getText());

			Service newService = new Service(null, name, type, rate1, rate2, rate3, rate4, rate5);
			boolean success = serviceDAO.addService(newService);

			if (success) {
				showAlert2("Success", "Service added successfully!");
				loadServices();
	            clearServiceFormFields();
				
			} else {
				showAlert("Error", "Failed to add service. Please try again.");
			}
		}
	
	    @FXML
		private void handleDeleteCategory(ActionEvent event) {
		    Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
		    if (selectedCategory != null) {
		        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
		        confirmationAlert.setTitle("Confirmation");
		        confirmationAlert.setHeaderText("Delete Category");
		        confirmationAlert.setContentText("Are you sure you want to delete this category?");
		        
		        Optional<ButtonType> result = confirmationAlert.showAndWait();
		        if (result.isPresent() && result.get() == ButtonType.OK) {
		            boolean success = categoryDAO.deleteCategory(selectedCategory);
		            if (success) {
		                showAlert2("Success", "Category deleted successfully!");
		                loadCategoryDataTable(); // Refresh the table view
		            } else {
		                showAlert("Error", "Failed to delete category. Please try again.");
		            }
		        }
		    } else {
		        showAlert("Error", "Please select a category to delete.");
		    }
		}
	    private void showAlert2(String title, String content) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setContentText(content);
	        alert.showAndWait();
	    }
		@FXML
		private void handleDeleteService(ActionEvent event) {
		    Service selectedService = serviceTable1.getSelectionModel().getSelectedItem();
		    if (selectedService != null) {
		        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
		        confirmationAlert.setTitle("Confirmation");
		        confirmationAlert.setHeaderText("Delete Service");
		        confirmationAlert.setContentText("Are you sure you want to delete this service?");
		        
		        Optional<ButtonType> result = confirmationAlert.showAndWait();
		        if (result.isPresent() && result.get() == ButtonType.OK) {
		            boolean success = serviceDAO.deleteService(selectedService);
		            if (success) {
		                showAlert2("Success", "Service deleted successfully!");
		                loadServices();
		            } else {
		                showAlert("Error", "Failed to delete service. Please try again.");
		            }
		        }
		    } else {
		        showAlert("Error", "Please select a service to delete.");
		    }
		}
		private void loadServices() {
	        try {
	            ObservableList<Service> services = FXCollections.observableArrayList(serviceDAO.getAllServices());
	            serviceTable1.setItems(services);
	        } catch (SQLException e) {
	        	 System.err.println("Error loading Service: " + e.getMessage());
	            e.printStackTrace();
	        }
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

					Order order = new Order(orderNumber, dueDate, dueTime, customer, totalQuantity,
							OrderStatus.valueOf(orderStatus));
					orders.add(order);
				}
				orderTable.setItems(orders);
			} catch (SQLException e) {
				e.printStackTrace();
				showAlert("Database Error", "Failed to fetch orders for the selected date.");
			}
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
	   
	    private void insertOrderIntoDatabase(OrderNow order) {
	        String query = "INSERT INTO laundry.order1 (onum, odate, otime, rtype, cid, cname, mobile, address, items, quans, sub, disp, disamt, tax, gross, rof, net, ad, pby, bal, dtype, nop, ddate, dtime1, dtime2, dtime3, remarks, status, company, cash, card, netb, others, taxp, sno) " +
	                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        String queryCheck = "SELECT COUNT(*) FROM laundry.customer WHERE mobile = ?";
	        String queryInsertCustomer = "INSERT INTO laundry.customer (mobile, cname, address) VALUES (?, ?, ?)";
	        
	        try (Connection connection = Database.connectDB();
	        		 PreparedStatement stmtCheck = connection.prepareStatement(queryCheck);
	                 PreparedStatement stmtInsertCustomer = connection.prepareStatement(queryInsertCustomer);
		             PreparedStatement stmt = connection.prepareStatement(query)) {
	            	
	        	 connection.setAutoCommit(false);
	        	
	        	 stmtCheck.setString(1, order.getMobile());
	             ResultSet rs = stmtCheck.executeQuery();
	             rs.next();
	             int count = rs.getInt(1);

	             if (count == 0) {
	                 stmtInsertCustomer.setString(1, order.getMobile());
	                 stmtInsertCustomer.setString(2, order.getCname());
	                 stmtInsertCustomer.setString(3, order.getAddress());
	                 stmtInsertCustomer.executeUpdate();
	             }
	        	
	            stmt.setInt(1, order.getOnum());
	            stmt.setDate(2, java.sql.Date.valueOf(order.getOdate()));
	            stmt.setString(3, order.getOtime());
	            stmt.setString(4, order.getRtype());
	            stmt.setInt(5, order.getCid());
	            stmt.setString(6, order.getCname());
	            stmt.setString(7, order.getMobile());
	            stmt.setString(8, order.getAddress());
	            stmt.setInt(9, order.getItems());
	            stmt.setDouble(10, order.getQuans());
	            stmt.setDouble(11, order.getSub());
	            stmt.setDouble(12, order.getDisp());
	            stmt.setDouble(13, order.getDisamt());
	            stmt.setDouble(14, order.getTax());
	            stmt.setDouble(15, order.getGross());
	            stmt.setDouble(16, order.getRof());
	            stmt.setDouble(17, order.getNet());
	            stmt.setDouble(18, order.getAd());
	            stmt.setString(19, order.getPby());
	            stmt.setDouble(20, order.getBal());
	            stmt.setString(21, order.getDtype());
	            stmt.setDouble(22, order.getNop());
	            stmt.setDate(23, (Date) order.getDdate());
	            stmt.setTime(24, (Time) order.getDtime1());
	            stmt.setString(25, order.getDtime2());
	            stmt.setString(26, order.getDtime3());
	            stmt.setString(27, order.getRemarks());
	            stmt.setString(28, order.getStatus());
	            stmt.setString(29, order.getCompany());
	            stmt.setDouble(30, order.getCash());
	            stmt.setDouble(31, order.getCard());
	            stmt.setDouble(32, order.getNetb());
	            stmt.setDouble(33, order.getOthers());
	            stmt.setDouble(34, order.getTaxp());
	            stmt.setInt(35, order.getSno());
	            
	            stmt.executeUpdate();
	            System.out.println("Order inserted successfully!");
	            generateReceipt(order);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            try {
	                if (connection != null) {
	                    connection.rollback(); 
	                }
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        } finally {
	            try {
	                if (connection != null) {
	                    connection.setAutoCommit(true); 
	                }
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
	    
	    private void generateReceipt(OrderNow order) {
			
			
		}
		@FXML
	    private void onPrintButtonClick() {
	        // Collect customer details
	        String phone = phoneNum.getText();
	        String name = customerName.getText();
	        String address = customerAddress.getText();
	        
	        // Collect order details
	        ObservableList<Item> items = serviceTable.getItems();
	        int totalItems = items.size();
	        double totalQuantity = items.stream().mapToDouble(Item::getQuantity).sum();
	        double subTotal = items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
	        
	        // Collect payment details
	        double discount = Double.parseDouble(discountField.getText());
	        double advancePayment = Double.parseDouble(advancePaymentField.getText());
	        double finalAmount = Double.parseDouble(finalAmountLabel.getText().replace("$", ""));
	        
	        // Calculate other payment details
	        double discountAmount = (discount / 100) * subTotal;
	        double grossAmount = subTotal - discountAmount;
	        double netAmount = grossAmount - advancePayment;

	        // Create order object
	        OrderNow order = new OrderNow();
	        order.setSno(generateOrderNumber());
	        order.setCname(name);
	        order.setMobile(phone);
	        order.setRtype("rate-1");
	        order.setItems(totalItems);
	        order.setQuans(totalQuantity);
	        order.setSub(subTotal);
	        order.setDisp(discount);
	        order.setDisamt(discountAmount);
	        order.setGross(grossAmount);
	        order.setNet(netAmount);
	        order.setAd(advancePayment);
	        order.setDtype("Normal");
	        order.setRemarks("N/A"); 
	        order.setStatus("Pending");
	        order.setCompany("Your Company Name"); // Set your company name
	        order.setCash(0); 
	        order.setCard(0); 
	        order.setNetb(netAmount); 
	        order.setOthers(0); 
	        order.setTaxp(0); 
	        order.setAddress(address);
	        // Additional fields can be set as needed
	        order.setOnum(generateOrderNumber()); // Implement a method to generate order number
	        LocalDateTime now = LocalDateTime.now();
	        order.setOdate(now.toLocalDate()); 
	        order.setOtime(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	        
	        // Print or save the order
	        insertOrderIntoDatabase(order);
	        System.out.println(order.toString());
	        clearInputs();
	    }
	    
	    private int generateOrderNumber() {
	        int randomNum = 1000 + random.nextInt(9000); 
	        return counter.getAndIncrement() * 10000 + randomNum;
	    }
	    
	    // Show services for a category
	    private void showServices(String category) {
	        categoryScrollPane.setVisible(false);
	        itemScrollPane.setVisible(true);
	        loadItems(category);
	    }
	    
	    private void clearInputs() {
	        // Clear text fields
	        phoneNum.setText("");
	        customerName.setText("");
	        customerAddress.setText("");
	        discountField.setText("0");
	        advancePaymentField.setText("0");

	        // Clear table
	        serviceTable.getItems().clear();
	        resultsTable.getItems().clear();
	        
	        // Reset labels
	        totalAmountLabel.setText("0.00");
	        finalAmountLabel.setText("0.00");
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

	    

	    // Key listener for phone number text field
	    public void numOnChange(KeyEvent event) {
	        KeyCode keyCode = event.getCode();
	        if (keyCode == KeyCode.BACK_SPACE || event.getEventType() == KeyEvent.KEY_RELEASED) {
	            handleBackspace();
	        } else {
	            String phoneNumber = phoneNum.getText().trim();
	            if (!phoneNumber.isEmpty()) {
	                phoneNumber += event.getCharacter();
	               
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
	           
	            fetchCustomerDetailsByPhoneNumber(phoneNumber);
	        } else {
	            clearCustomerDetails();
	        }
	    }

	    // Fetch customer details by phone number
	    private void fetchCustomerDetailsByPhoneNumber(String phoneNumber) {
	        
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
	        phoneNum.setText(customer.getPhoneNumber());
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
        
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        // Set up the orderStatusColumn with a ComboBoxTableCell
        orderStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(OrderStatus.values()));

        // Add a listener to handle the edit commit event when user selects an option
        orderStatusColumn.setOnEditCommit(event -> {
            TablePosition<Order, OrderStatus> position = event.getTablePosition();
            Order order = event.getTableView().getItems().get(position.getRow());
            OrderStatus newStatusValue = (OrderStatus) event.getNewValue();
            order.setOrderStatus(newStatusValue);

            // Now you can update the database with the new order status
            updateOrderStatusInDatabase(order);
        });

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
			showAlert2("Success", "Category added successfully.");
			loadCategoryDataTable();
			clearCategoryFields();
		} else {
			showAlert("Error", "Failed to add category.");
		}
	}
    
    private void clearCategoryFields() {
		categoryNameField.clear();
		categoryIdField.clear();
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
