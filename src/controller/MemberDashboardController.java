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
            Navigation.switchNavigation("MemberBorrowedBooks.fxml", event); {
            
        }
    }

    @FXML
    private void btnReturnedBookOnAction(ActionEvent event) throws IOException {
            Navigation.switchNavigation("MemberReturnedBooks.fxml", event); {
            
        }
    }

    @FXML
    private void btnAvailableBookOnAction(ActionEvent event) throws IOException {
            Navigation.openPopup("MemberBorrow.fxml", (MemberBorrowController controller) -> {
            controller.setUser(currentUser);
            
            System.out.println("User from Dashboard to borrow " + currentUser);
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
