package controller;

import DAO.UserDAO;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import util.Navigation;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;

import java.io.IOException;

public class RegisterController {

    @FXML 
    private TextField txtFirstName;
    
    @FXML 
    private TextField txtLastName;
    
    @FXML 
    private TextField txtContactNo;
    
    @FXML 
    private TextField txtEmail;
    
    @FXML 
    private TextField txtUsername;
    
    @FXML 
    private PasswordField txtPassword;

    @FXML 
    private Label lblFirstNameAlert;
    
    @FXML 
    private Label lblLastNameAlert;
    
    @FXML 
    private Label lblContactNoAlert;
    
    @FXML 
    private Label lblEmailAlert;
    
    @FXML 
    private Label lblUsernameAlert;
    
    @FXML 
    private Label lblPasswordAlert;
    
    @FXML 
    private ComboBox<String> comboMemberType;
    
    @FXML 
    private Label lblMemberTypeAlert;


    private final UserDAO userDAO = new UserDAO();
    
    @FXML
    public void initialize() {
        comboMemberType.setItems(FXCollections.observableArrayList("STUDENT", "FACULTY"));
    }


    @FXML
    private void btnSignUpOnAction(ActionEvent event) {
        clearAlerts();

        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String phone = txtContactNo.getText().trim();
        String email = txtEmail.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        boolean hasError = false;
        
        String memberType = comboMemberType.getValue();
        if (memberType == null || memberType.isEmpty()) {
            lblMemberTypeAlert.setText("Please select member type.");
            hasError = true;
        }

        if (firstName.isEmpty()) {
            lblFirstNameAlert.setText("First name is required.");
            hasError = true;
        }

        if (lastName.isEmpty()) {
            lblLastNameAlert.setText("Last name is required.");
            hasError = true;
        }

        if (phone.isEmpty()) {
            lblContactNoAlert.setText("Contact number is required.");
            hasError = true;
        }

        if (email.isEmpty()) {
            lblEmailAlert.setText("Email is required.");
            hasError = true;
        } else if (userDAO.existsByEmail(email)) {
            lblEmailAlert.setText("Email already exists.");
            hasError = true;
        }

        if (username.isEmpty()) {
            lblUsernameAlert.setText("Username is required.");
            hasError = true;
        } else if (userDAO.existsByUsername(username)) {
            lblUsernameAlert.setText("Username already exists.");
            hasError = true;
        }

        if (password.isEmpty()) {
            lblPasswordAlert.setText("Password is required.");
            hasError = true;
        }

        if (hasError) return;

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPhone(phone);
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setUserType("MEMBER");
        newUser.setMemberType(memberType);


        boolean success = userDAO.addUser(newUser);

        if (success) {
            showSuccess("Account created! You can now sign in.");
            redirectToLogin(event);
        } else {
            showError("Failed to register. Please try again.");
        }
    }

    @FXML
    private void btnSignInOnAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (IOException e) {
            showError("Unable to load login: " + e.getMessage());
        }
    }

    private void clearAlerts() {
        lblFirstNameAlert.setText("");
        lblLastNameAlert.setText("");
        lblContactNoAlert.setText("");
        lblEmailAlert.setText("");
        lblUsernameAlert.setText("");
        lblPasswordAlert.setText("");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToLogin(ActionEvent event) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (IOException e) {
            showError("Unable to load login page.");
        }
    }
}
