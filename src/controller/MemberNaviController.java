package controller;

import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import util.Navigation;
import util.DateTime;

import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MemberNaviController {

    @FXML private Label lblUser;
    @FXML private Label lblDate;
    @FXML private Label lblTime;
    @FXML private Pane pagingPane;

    private User currentUser;
    

    public void setUser(User user) {
        this.currentUser = user;
        lblUser.setText(user.getFirstName() + " " + user.getLastName());
        lblDate.setText(DateTime.dateNowFormatted());
        lblTime.setText(DateTime.timeNow());
    }

    @FXML
    private void initialize() {

    }
    
    public void loadDashboard() {
    try {
        Navigation.switchPaging(pagingPane, "MemberDashboard.fxml", (MemberDashboardController controller) -> {
            controller.setUser(currentUser);
            controller.setPagingPane(pagingPane);
        });
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @FXML
    private void btnDashboardOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "MemberDashboard.fxml", (MemberDashboardController controller) -> {
                controller.setUser(currentUser);
                controller.setPagingPane(pagingPane);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCatalogOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "MemberBorrowedBooks.fxml", (MemberBorrowedBooksController controller) -> {
                controller.setUser(currentUser);
                controller.setPagingPane(pagingPane);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnBooksOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "MemberBorrow.fxml", (MemberBorrowController controller) -> {
                controller.setUser(currentUser);
                controller.setPagingPane(pagingPane);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void btnLibrariansOnAction(ActionEvent event) {
    }

    @FXML
    private void btnSettingsOnAction(ActionEvent event) {
       try {
            Navigation.openPopup("MemberSelfUpdate.fxml", (MemberSelfUpdateController controller) -> {
                controller.setUser(currentUser);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnLogOutOnAction(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm Logout");
    alert.setHeaderText("Are you sure you want to sign out?");

    ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

    if (result == ButtonType.OK) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
}
