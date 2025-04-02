package controller;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import util.Navigation;
import java.io.IOException;

public class MemberDashboardController {

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void btnBorrowedBookOnAction(ActionEvent event) throws IOException {
            Navigation.switchNavigation("MemberBorrowedBooks.fxml", event, (MemberBorrowedBooksController controller) -> {
        controller.setUser(currentUser);
            });
    }

    @FXML
    private void btnReturnedBookOnAction(ActionEvent event) throws IOException {
            Navigation.switchNavigation("MemberReturnedBooks.fxml", event, (MemberReturnedBooksController controller) -> {
        controller.setUser(currentUser);
            });
    }

    @FXML
    private void btnAvailableBookOnAction(ActionEvent event) throws IOException {
            Navigation.switchNavigation("MemberBorrow.fxml", event, (MemberBorrowController controller) -> {
                    controller.setUser(currentUser);
        });
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
