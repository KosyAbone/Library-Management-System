package controller;

import DAO.UserDAO;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminDeleteUserController {

    @FXML
    private Text txtConfirmation;
    @FXML
    private Label lblConfirm;

    private User userToDelete;
    private final UserDAO userDAO = new UserDAO();
    private Runnable onUserDeleted;

    public void setUser(User user) {
        this.userToDelete = user;
        if (user != null) {
            txtConfirmation.setText(
                String.format("Are you sure you want to delete user \"%s\" (username: %s)?",
                user.getFullName(), user.getUsername()));
        }
    }

    public void setOnUserDeleted(Runnable callback) {
        this.onUserDeleted = callback;
    }

    @FXML
    private void btnConfirmOnAction() {
        if (userToDelete != null && userDAO.deleteUser(userToDelete.getUserId())) {
            if (onUserDeleted != null) {
                onUserDeleted.run();
            }
            closePopup();
        } else {
            txtConfirmation.setText("Failed to delete user. Please try again.");
        }
    }

    @FXML
    private void btnCloseOnAction() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) txtConfirmation.getScene().getWindow();
        stage.close();
    }
}
