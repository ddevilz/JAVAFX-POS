package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FXMLDocumentController implements Initializable{

	@FXML
	private Button login_btn;

	@FXML
	private AnchorPane main_form;

	@FXML
	private PasswordField password;

	@FXML
	private TextField username;
	
	
    
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private Alert alert;
    
    
    
    
    public void loginAccount(){
        
        if(username.getText().isEmpty() || password.getText().isEmpty()){
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Username/Password");
            alert.showAndWait();
        }else{
            
            String sql = "SELECT * FROM employee WHERE username = '" + username.getText() 
                    + "' AND password = '" + password.getText() + "'";
            
            connect = Database.connectDB();
            try{
                prepare = connect.prepareStatement(sql);
                result = prepare.executeQuery();
                
                if(result.next()){
                    // IF CORRECT USERNAME AND PASSWORD
                    
                    data.username = username.getText();
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();
                    // LINK YOUR MAIN FORM
                    Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    
                    stage.setMinWidth(1280);
                    stage.setMinHeight(750);
                    
                    stage.setTitle("Laundry Shop Management System");
                    
                    stage.setScene(scene);
                    stage.show();
                    
                    // TO HIDE YOUR LOGIN FORM
                    login_btn.getScene().getWindow().hide();
                    
                }else{
                    // IF INCORRECT USERNAME OR PASSWORD
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password");
                    alert.showAndWait();
                }
                
            }catch(Exception e){e.printStackTrace();}
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

}
