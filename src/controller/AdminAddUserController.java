package controller;

import DAO.UserDAO;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class AdminAddUserController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboUserType;
    @FXML private ComboBox<String> comboMemberType;

    @FXML private Label lblFirstNameAlert;
    @FXML private Label lblLastNameAlert;
    @FXML private Label lblEmailAlert;
    @FXML private Label lblUsernameAlert;
    @FXML private Label lblPasswordAlert;
    @FXML private Label lblMemberTypeAlert;

    private User currentUser;
    private Runnable onUserAdded;

    private final UserDAO userDAO = new UserDAO();

    public void initialize() {
        comboUserType.getItems().addAll("ADMIN", "LIBRARIAN", "MEMBER");
        comboMemberType.getItems().addAll("STUDENT", "FACULTY");

        // Default visibility logic
        comboUserType.setOnAction(event -> {
            String selected = comboUserType.getValue();
            comboMemberType.setVisible("MEMBER".equals(selected));
        });
    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setOnUserAdded(Runnable callback) {
        this.onUserAdded = onUserAdded;
    }

    @FXML
    private void btnAddOnAction() {
        clearAlerts();

        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String userType = comboUserType.getValue();
        String memberType = "MEMBER".equals(userType) ? comboMemberType.getValue() : null;

        if (!validateInputs(firstName, lastName, email, username, password, userType, memberType)) return;

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setUserType(userType);
        newUser.setMemberType(memberType);

        if (userDAO.addUser(newUser)) {
            if (onUserAdded != null) {
                onUserAdded.run();
            }
            closePopup();
        } else {
            lblUsernameAlert.setText("Failed to add user. Try another username.");
        }
    }

    @FXML
    private void btnCancelOnAction() {
        closePopup();
    }

    @FXML
    private void btnCloseOnAction() {
        closePopup();
    }

    private boolean validateInputs(String firstName, String lastName, String email, String username,
                                   String password, String userType, String memberType) {
        boolean isValid = true;

        if (firstName.isEmpty()) {
            lblFirstNameAlert.setText("First name is required.");
            isValid = false;
        }
        if (lastName.isEmpty()) {
            lblLastNameAlert.setText("Last name is required.");
            isValid = false;
        }
        if (email.isEmpty() || !email.contains("@")) {
            lblEmailAlert.setText("Valid email required.");
            isValid = false;
        }
        if (username.isEmpty()) {
            lblUsernameAlert.setText("Username is required.");
            isValid = false;
        }
        if (password.isEmpty() || password.length() < 6) {
            lblPasswordAlert.setText("Password must be at least 6 characters.");
            isValid = false;
        }
        if (userType == null) {
            lblUsernameAlert.setText("User type required.");
            isValid = false;
        }
        if ("MEMBER".equals(userType) && memberType == null) {
            lblMemberTypeAlert.setText("Please select member type.");
            isValid = false;
        }

        return isValid;
    }

    private void clearAlerts() {
        lblFirstNameAlert.setText("");
        lblLastNameAlert.setText("");
        lblEmailAlert.setText("");
        lblUsernameAlert.setText("");
        lblPasswordAlert.setText("");
        lblMemberTypeAlert.setText("");
    }

    private void closePopup() {
        Stage stage = (Stage) txtFirstName.getScene().getWindow();
        stage.close();
    }
}
