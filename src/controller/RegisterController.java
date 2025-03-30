/**
 *
 * @author kossy
 */
package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.User;
import DAO.UserDAO;
import util.PasswordUtils;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RegisterController {
    // FXML injected fields
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> userTypeCombo;
    @FXML private ComboBox<String> memberTypeCombo;
    @FXML private Label errorLabel;
    
    // DAO and state
    private UserDAO userDAO = new UserDAO();
    private User currentUser;

    @FXML
    public void initialize() {
        
        // Initialize with defaults
        ObservableList<String> defaultUserTypes = FXCollections.observableArrayList("ADMIN", "LIBRARIAN", "MEMBER");
        ObservableList<String> memberTypes = FXCollections.observableArrayList("FACULTY", "STUDENT");
        
        try {
            userTypeCombo.setItems(defaultUserTypes);
            memberTypeCombo.setItems(memberTypes);
            
            userTypeCombo.getSelectionModel().selectFirst();
            memberTypeCombo.getSelectionModel().selectFirst();
            
            // Set initial visibility
            memberTypeCombo.setVisible(false);
            
            // Set up listener only if components are properly injected
            if (userTypeCombo != null && memberTypeCombo != null) {
                userTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        memberTypeCombo.setVisible("MEMBER".equals(newVal));
                    }
                });
            }
            
        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        
        // Refresh UI if components are ready
        if (userTypeCombo != null) {
            setupUserTypeCombo();
        }
    }

    private void setupUserTypeCombo() {
        try {
            ObservableList<String> options = FXCollections.observableArrayList();
            
            if (currentUser != null) {
                if ("ADMIN".equals(currentUser.getUserType())) {
                    options.addAll("ADMIN", "LIBRARIAN", "MEMBER");
                } else if ("LIBRARIAN".equals(currentUser.getUserType())) {
                    options.addAll("STUDENT", "FACULTY");
                }
            }
            
            // Default options for public registration
            if (options.isEmpty()) {
                options.addAll("LIBRARIAN", "MEMBER");
            }
            
            userTypeCombo.setItems(options);
            userTypeCombo.getSelectionModel().selectFirst();
            
        } catch (Exception e) {
            System.err.println("Error setting up user types: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            if (!validateInputs()) return;
            
            String userType = userTypeCombo.getValue();
            String memberType = "MEMBER".equals(userType) ? memberTypeCombo.getValue() : null;
            
            // Permission checks
            if (currentUser != null && !userDAO.canCreateUserType(currentUser.getUserType(), userType)) {
                showError("You don't have permission to create " + userType + " accounts");
                return;
            }
            
            // Anonymous registration restrictions
            if (currentUser == null && !"MEMBER".equals(userType)) {
                showError("Only member registration is allowed");
                return;
            }
            
            if (!validateUniqueCredentials()) return;
            
            if (createNewUser(userType, memberType)) {
                showSuccess("Registration successful!" + 
                           (currentUser == null ? " Please login." : ""));
                handleCancel();
            }
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            showError("An unexpected error occurred during registration");
        }
    }

    private boolean validateInputs() {
        try {
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
                
                showError("All fields are required");
                return false;
            }
            
            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                showError("Passwords do not match");
                return false;
            }
            
            if (passwordField.getText().length() < 6) {
                showError("Password must be at least 6 characters");
                return false;
            }
            
            if (!emailField.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showError("Invalid email format");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Validation error: " + e.getMessage());
            return false;
        }
    }

    private boolean validateUniqueCredentials() {
        try {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            
            if (userDAO.getUserByUsername(username) != null) {
                showError("Username already exists");
                return false;
            }
            
            if (userDAO.getUserByEmail(email) != null) {
                showError("Email already registered");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error checking credentials: " + e.getMessage());
            showError("Error checking credentials availability");
            return false;
        }
    }

    private boolean createNewUser(String userType, String memberType) {
        try {
            User newUser = new User();
            newUser.setFirstName(firstNameField.getText().trim());
            newUser.setLastName(lastNameField.getText().trim());
            newUser.setEmail(emailField.getText().trim());
            newUser.setPhone(phoneField.getText().trim());
            newUser.setUsername(usernameField.getText().trim());
            newUser.setPassword(PasswordUtils.hashPassword(passwordField.getText()));
            newUser.setUserType(userType);
            newUser.setMemberType(memberType);
            
            boolean success = userDAO.addUser(newUser);
            if (!success) {
                showError("Registration failed. Please try again.");
            }
            return success;
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            showError("Error creating user account");
            return false;
        }
    }

    @FXML
    private void handleCancel() {
        try {
            String fxml = currentUser != null ? "/view/AdminDashboard.fxml" : "/view/Login.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error navigating back: " + e.getMessage());
            e.printStackTrace();
            showError("Error navigating back");
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(true);
    }
    
    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setVisible(true);
    }
}