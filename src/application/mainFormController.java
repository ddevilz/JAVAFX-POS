package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.*;
public class mainFormController implements Initializable {
	

    @FXML
    private Button dashboardButton;

    @FXML
    private Button takeOrderButton;

    @FXML
    private Button viewOrderDetailButton;

    @FXML
    private Button orderDetailsButton;

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private AnchorPane orderPane;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button fetchCustomerButton;

    @FXML
    private TableView<?> orderTable;

    @FXML
    private TableColumn<?, ?> colNo;

    @FXML
    private TableColumn<?, ?> colService;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label customerAddressLabel;

    @FXML
    private Label customerMobileLabel;

    @FXML
    private Label customerDiscountLabel;

    private Connection connection;

    @FXML
    public void initialize() {
        connectToDatabase();

        fetchCustomerButton.setOnAction(event -> fetchCustomerDetails());

        takeOrderButton.setOnAction(event -> switchPane(orderPane));
        dashboardButton.setOnAction(event -> switchPane(dashboardPane));
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/laundry";
        String user = "root";
        String password = "Mysql@123";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void switchPane(AnchorPane pane) {
        dashboardPane.setVisible(false);
        orderPane.setVisible(false);

        pane.setVisible(true);
    }

    private void fetchCustomerDetails() {
        String mobile = phoneTextField.getText();

        try {
            String query = "SELECT * FROM customer WHERE mobile = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, mobile);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customerNameLabel.setText(resultSet.getString("cname"));
                customerAddressLabel.setText(resultSet.getString("add1"));
                customerMobileLabel.setText(resultSet.getString("mobile"));
                customerDiscountLabel.setText(String.valueOf(resultSet.getDouble("dis")));
            } else {
                // Code to handle new customer entry
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
