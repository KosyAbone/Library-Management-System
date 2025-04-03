package controller;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import util.Navigation;
import java.io.IOException;

public class LibrarianDashboardController implements UserAwareController {
    
    private User currentUser;

    @Override
    public void setUser(User user) {
        this.currentUser = user;
        // You can add any initialization that needs the user here
    }

    @FXML
    private void handleBookManagement(ActionEvent event) {
        try {
            Navigation.switchNavigation("LibrarianBookManagement.fxml", event, 
                (UserAwareController controller) -> controller.setUser(currentUser));
        } catch (IOException e) {
            showError("Navigation to Book Management failed", e);
        }
    }

    @FXML
    private void handleMemberManagement(ActionEvent event) {
        try {
            Navigation.switchNavigation("LibrarianMemberManagement.fxml", event,
                    (LibrarianMemberManagementController controller) -> controller.setUser(currentUser));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOverdueBooks(ActionEvent event) {
        try {
            Navigation.switchNavigation("LibrarianOverdueBooks.fxml", event,
                    (LibrarianOverdueBooksController controller) -> controller.setUser(currentUser));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (IOException e) {
            showError("Failed to logout", e);
        }
    }

    private void showError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
        // Consider adding an alert dialog here:
        // Alert alert = new Alert(Alert.AlertType.ERROR, message);
        // alert.showAndWait();
    }
}