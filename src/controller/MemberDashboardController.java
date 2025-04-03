package controller;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import util.Navigation;

import java.io.IOException;

public class MemberDashboardController {

    private User currentUser;
    private Pane pagingPane;

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setPagingPane(Pane pagingPane) {
        this.pagingPane = pagingPane;
    }

    @FXML
    private void btnBorrowedBookOnAction(ActionEvent event) {
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
    private void btnReturnedBookOnAction(ActionEvent event) {
        try {
            Navigation.switchPaging(pagingPane, "MemberReturnedBooks.fxml", (MemberReturnedBooksController controller) -> {
                controller.setUser(currentUser);
                controller.setPagingPane(pagingPane);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnAvailableBookOnAction(ActionEvent event) {
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
    private void btnLogOutAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
