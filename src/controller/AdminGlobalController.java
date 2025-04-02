package controller;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import util.Navigation;

public class AdminGlobalController {

    @FXML private Pane pagingPane;
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
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
    private void btnBranchesOnAction(ActionEvent event) {
        // TODO: Add support for branches page
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
