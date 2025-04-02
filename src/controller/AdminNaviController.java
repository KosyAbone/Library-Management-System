package controller;

import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import util.Navigation;
import util.DateTime;

import java.io.IOException;

public class AdminNaviController {

    @FXML private Label lblAdmin;
    @FXML private Label lblDate;
    @FXML private Label lblTime;
    @FXML private Pane pagingPane;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        lblAdmin.setText(user.getFirstName() + " " + user.getLastName());
        lblDate.setText(DateTime.dateNowFormatted());
        lblTime.setText(DateTime.timeNow());
    }

    @FXML
    private void initialize() {
        // Time/date will be injected after setUser() is called
    }
    
    public void loadDashboard() {
    try {
        Navigation.switchPaging(pagingPane, "AdminDashboard.fxml", (AdminDashboardController controller) -> {
            controller.setUser(currentUser);
        });
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @FXML
    private void btnDashboardOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "AdminDashboard.fxml", (AdminDashboardController controller) -> {
                controller.setUser(currentUser);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCatalogOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "AdminBorrowed.fxml", (AdminBorrowedController controller) -> {
                controller.setUser(currentUser);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnBooksOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "AdminBookManagement.fxml", (AdminBookManagementController controller) -> {
                controller.setUser(currentUser);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnUsersOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "AdminUserManagement.fxml", (AdminUserManagementController controller) -> {
                controller.setUser(currentUser);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnLibrariansOnAction(ActionEvent event) {
        // Optional - implement your AdminBranchController here
    }

    @FXML
    private void btnSettingsOnAction(ActionEvent event) {
        // Optional - show a settings popup if needed
    }

    @FXML
    private void btnLogOutOnAction(ActionEvent event) {
        Navigation.exit();
    }
}
