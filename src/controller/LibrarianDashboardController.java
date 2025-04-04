package controller;

import DAO.BookDAO;
import DAO.BorrowRecordDAO;
import DAO.UserDAO;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import util.Navigation;
import java.io.IOException;
import javafx.scene.control.Label;

public class LibrarianDashboardController implements UserAwareController {
    
    private User currentUser;
    @FXML 
    private Label lblTotalMemberCount;
    
    @FXML 
    private Label lblTotalBookCount;
    
    @FXML 
    private Label lblTotalOverdueBookCount;
    
    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    
    @Override
    public void setUser(User user) {
        this.currentUser = user;
        loadDashboardData();
    }
    
    private void loadDashboardData() {
        int totalBooks = bookDAO.getBookCount();
        int totalMembers = userDAO.getMemberCount();
        int overdueBooks = borrowRecordDAO.getOverdueBooksCount();
        
        lblTotalBookCount.setText(String.format("%,d", totalBooks));
        lblTotalMemberCount.setText(String.format("%,d", totalMembers));
        lblTotalOverdueBookCount.setText(String.format("%,d", overdueBooks));
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
    }
}