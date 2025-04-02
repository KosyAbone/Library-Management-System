package controller;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.layout.Pane;
import util.Navigation;

public class AdminGlobalController {

    @FXML private Pane pagingPane;
     @FXML private Label lblAdmin;
    @FXML private Label lblDate;
    @FXML private Label lblTime;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;

        if (user != null) {
            lblAdmin.setText(user.getFirstName() + " " + user.getLastName());
        } else {
            lblAdmin.setText("Admin");
        }

        setDateTime();
    }

    private void setDateTime() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy"); // e.g., Apr 02, 2025
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");       // e.g., 04:22 PM

        lblDate.setText(now.format(dateFormat));
        lblTime.setText(now.format(timeFormat));
    }

    @FXML
    private void btnDashboardOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "AdminDashboard.fxml", (AdminDashboardController controller) -> {
                controller.setUser(currentUser);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCatalogOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "AdminBorrowed.fxml", (AdminBorrowedController controller) -> {
                controller.setUser(currentUser);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnBooksOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "AdminBookManagement.fxml", (AdminBookManagementController controller) -> {
                controller.setUser(currentUser);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnUsersOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "AdminUserManagement.fxml", (AdminUserManagementController controller) -> {
                controller.setUser(currentUser);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnLibrariansOnAction(ActionEvent event) {
        // TODO: Add support for librarians page
    }

    @FXML
    private void btnLogOutOnAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnSettingsOnAction(ActionEvent event) {
        // Toggle visibility of settingsPane or open settings popup
    }
}
