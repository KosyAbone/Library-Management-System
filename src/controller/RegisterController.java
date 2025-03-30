package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.User;
import DAO.UserDAO;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Duration;

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
    @FXML private Label successLabel;
    @FXML private Text formTitleText;
    @FXML private Button registerBtn;
    
    // DAO and state
    private final UserDAO userDAO = new UserDAO();
    private User currentUser;
    private boolean isModal = false;
    private boolean isEditing = false;
    
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
            
            errorLabel.setVisible(false);
            successLabel.setVisible(false);
            
            if (!validateInputs()) return;
            
            String userType = userTypeCombo.getValue();
            String memberType = "MEMBER".equals(userType) ? memberTypeCombo.getValue() : null;
            
            // Permission checks
            if (currentUser != null && !userDAO.canCreateUserType(currentUser.getUserType(), userType)) {
                showError("You don't have permission to create " + userType + " accounts");
                return;
            }
            
            // Anonymous registration restrictions
            else if ("MEMBER".equals(userType)) {
                showError("Only member registration is allowed");
                return;
            }
            
            if (!validateUniqueCredentials()) return;
            
            if (createNewUser(userType, memberType)) {
                showSuccess("✅ Registration successful!");
                
                if(currentUser == null ) { //public registration. redirect to login
                    showSuccess("✅ Registration successful! Redirecting to login...");
                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                    delay.setOnFinished(event -> handleCancel());
                    delay.play();
                }
                else{
                    // For admins, prepare for next registration
                    clearForm();
                    firstNameField.requestFocus();
                }
            }
        } catch (Exception e) {
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
            showError("User exists. Please login.");
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
            newUser.setPassword(passwordField.getText().trim());
            newUser.setUserType(userType);
            newUser.setMemberType(memberType);
            
            boolean success = userDAO.addUser(newUser);
            if (!success) {
                showError("Registration failed. Please try again.");
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error creating user account");
            return false;
        }
    }
   
    public void setEditing(boolean editing) {
        this.isEditing = editing;
        Platform.runLater(() -> {
            formTitleText.setText(isEditing ? "Edit User" : "Register New Account");
            registerBtn.setText(isEditing ? "Confirm" : "Register");
         });
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }
    
    private void showSuccess(String message) {
        errorLabel.setVisible(false);
        successLabel.setText(message);
        successLabel.setVisible(true);
        
        // Auto-hide after 3 seconds
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
        visiblePause.setOnFinished(event -> successLabel.setVisible(false));
        visiblePause.play();
    }

    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(true);
    }
    
    private void showAnimatedSuccess() {
        ImageView checkmark = new ImageView(new Image("/images/checkmark.gif") {});
        checkmark.setFitHeight(50);
        checkmark.setFitWidth(50);

        StackPane pane = new StackPane(checkmark);
        pane.setAlignment(Pos.CENTER);

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(new Scene(pane, 100, 100));
        popup.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> popup.close());
        delay.play();
    }
    
    private void clearForm(){
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
    
    
    private void closeWindow() {
        ((Stage) usernameField.getScene().getWindow()).close();
    }
    
    public void setIsModal(boolean isModal) {
        this.isModal = isModal;
    }
}