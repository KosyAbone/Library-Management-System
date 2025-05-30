package controller;

import DAO.UserDAO;
import Model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.Navigation;

public class MemberSelfUpdateController {

    @FXML private TextField txtFirstName, txtLastName, txtEmail, txtUsername, txtPhone;
    @FXML private PasswordField txtPassword;

    @FXML private ComboBox<String> comboUserType;
    @FXML private ComboBox<String> comboMemberType;

    @FXML private Label lblFirstNameAlert, lblLastNameAlert, lblEmailAlert, lblUsernameAlert, lblPasswordAlert, lblPhoneAlert, lblMemberTypeAlert;

    private User currentUser;
    private Runnable onUserUpdated;
    @FXML
    private Pane closePane;
    @FXML
    private ImageView imgClose;
    @FXML
    private Pane cancelPane;
    @FXML
    private Label lblCancel;
    @FXML
    private Pane updatePane;
    @FXML
    private Label lblUpdate;

    public void initialize() {
    }

    public void setUser(User user) {
        this.currentUser = user;

        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtEmail.setText(user.getEmail());
        txtUsername.setText(user.getUsername());
        txtPassword.setText(user.getPassword());
        txtPhone.setText(user.getPhone());

        }

    public void setOnUserUpdated(Runnable onUserUpdated) {
        this.onUserUpdated = onUserUpdated;
    }

    @FXML
    private void btnUpdateOnAction() {
        if (!validateInputs()) return;

        User updatedUser = new User();
        updatedUser.setUserId(currentUser.getUserId());
        updatedUser.setFirstName(txtFirstName.getText().trim());
        updatedUser.setLastName(txtLastName.getText().trim());
        updatedUser.setEmail(txtEmail.getText().trim());
        updatedUser.setUsername(txtUsername.getText().trim());
        updatedUser.setPassword(txtPassword.getText());
        updatedUser.setPhone(txtPhone.getText().trim());
        updatedUser.setUserType(currentUser.getUserType());
        updatedUser.setMemberType(currentUser.getMemberType());


        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(updatedUser);

        if (success) {
            if (onUserUpdated != null) onUserUpdated.run();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Successful");
            alert.setHeaderText(null);
            alert.setContentText("User profile updated successfully!");
            alert.showAndWait();

            closePopup();
        } else {
            lblUsernameAlert.setText("Failed to update user. Try again.");
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        lblFirstNameAlert.setText("");
        lblLastNameAlert.setText("");
        lblEmailAlert.setText("");
        lblUsernameAlert.setText("");
        lblPasswordAlert.setText("");
        lblPhoneAlert.setText("");

        if (txtFirstName.getText().isEmpty()) {
            lblFirstNameAlert.setText("First name required");
            isValid = false;
        }
        if (txtLastName.getText().isEmpty()) {
            lblLastNameAlert.setText("Last name required");
            isValid = false;
        }
        if (txtEmail.getText().isEmpty()) {
            lblEmailAlert.setText("Email required");
            isValid = false;
        }
        if (txtUsername.getText().isEmpty()) {
            lblUsernameAlert.setText("Username required");
            isValid = false;
        }
        if (txtPassword.getText().isEmpty()) {
            lblPasswordAlert.setText("Password required");
            isValid = false;
        }
        if (txtPhone.getText().isEmpty()) {
            lblPhoneAlert.setText("Phone required");
            isValid = false;
        }
        return isValid;
    }

    @FXML
    private void btnCancelOnAction() {
        closePopup();
    }

    @FXML
    private void btnCloseOnAction() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) txtFirstName.getScene().getWindow();
        stage.close();
    }
}
