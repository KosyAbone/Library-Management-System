package controller;

import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import util.Navigation;
import util.DateTime;

import java.io.IOException;

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
        // Optional - implement your MemberBranchController here
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
