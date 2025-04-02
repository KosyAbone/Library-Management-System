package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import Model.User;

public class AdminViewUserController {

    @FXML
    private Label lblFullName;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblPhone;
    @FXML
    private Label lblUserType;
    @FXML
    private Label lblMemberType;

    private User user;

    public void setUser(User user) {
        this.user = user;
        displayUserDetails();
    }

    private void displayUserDetails() {
        if (user != null) {
            lblFullName.setText(user.getFullName());
            lblUsername.setText(user.getUsername());
            lblEmail.setText(user.getEmail());
            lblPhone.setText(user.getPhone());
            lblUserType.setText(user.getUserType());

            if ("MEMBER".equalsIgnoreCase(user.getUserType())) {
                lblMemberType.setText(user.getMemberType());
            } else {
                lblMemberType.setText("N/A");
            }
        }
    }

    @FXML
    private void btnCloseOnAction(ActionEvent event) {
        Stage stage = (Stage) lblFullName.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void btnExitOnAction(ActionEvent event) {
        Stage stage = (Stage) lblFullName.getScene().getWindow();
        stage.close();
    }
}
