package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Model.Book;
import Model.BorrowRecord;
import DAO.BookDAO;
import DAO.BorrowRecordDAO;
import Model.User;
import util.SceneManager;
import java.io.IOException;
import java.util.List;

public class MemberDashboardController {
    @FXML private Label welcomeLabel, memberTypeLabel, booksCheckedOutLabel, overdueBooksLabel, statusLabel;
    @FXML private TableView<BorrowRecord> currentLoansTable;
    @FXML private TableView<Book> availableBooksTable;
    @FXML private TextField bookSearchField;
    
    // Current Loans Table Columns
    @FXML private TableColumn<BorrowRecord, String> loanTitleColumn, checkoutDateColumn, dueDateColumn, loanStatusColumn;
    
    // Available Books Table Columns
    @FXML private TableColumn<Book, String> titleColumn, authorColumn, availableColumn;
    
    private User currentUser;
    private final BookDAO bookDAO = new BookDAO();
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();

    public void setUser(User user) {
        this.currentUser = user;
        if(user != null){
            welcomeLabel.setText("Welcome, " + user.getFullName());
            memberTypeLabel.setText("(" + user.getMemberType() + ")");
            initializeTables();
            loadDashboardData();   
        }
        
    }

    private void initializeTables() {
        // Current Loans Table
        loanTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        checkoutDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        loanStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Available Books Table
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
    }

    private void loadDashboardData() {
        try{
            List<BorrowRecord> currentLoans = borrowRecordDAO.getUserActiveLoans(currentUser.getUserId());
        booksCheckedOutLabel.setText(String.valueOf(currentLoans.size()));
        
        long overdueCount = currentLoans.stream()
            .filter(record -> "Overdue".equals(record.getStatus()))
            .count();
        overdueBooksLabel.setText(String.valueOf(overdueCount));
        
        currentLoansTable.getItems().setAll(currentLoans);
        
        // Load available books by default
        availableBooksTable.getItems().setAll(bookDAO.getAvailableBooks());
        }
        catch (Exception e) {
            showError("Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBrowse() {
        availableBooksTable.getItems().setAll(bookDAO.getAvailableBooks());
    }

    @FXML
    private void handleMyBooks() {
        currentLoansTable.getItems().setAll(borrowRecordDAO.getUserActiveLoans(currentUser.getUserId()));
    }

    @FXML
    private void handleBookSearch() {
        String searchTerm = bookSearchField.getText();
        List<Book> results = searchTerm.isEmpty() ? 
            bookDAO.getAvailableBooks() : 
            bookDAO.searchAvailableBooks(searchTerm);
        availableBooksTable.getItems().setAll(results);
    }

    @FXML
    private void handleBorrowBook() {
        Book selectedBook = availableBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            statusLabel.setText("Please select a book first");
            return;
        }

        String result = borrowRecordDAO.borrowBook(selectedBook.getBookId(), currentUser.getUserId());

        // Extract user-friendly message
        String userMessage = result.replaceFirst("^(Success|Error):\\s*", "");
        statusLabel.setText(userMessage);

        // Check success
        if (result.startsWith("Success")) {
            loadDashboardData();
        }
    }

    @FXML
    private void handleProfile() {
//        try {
//            ProfileController controller = SceneManager.loadAndSwitch("/view/Profile.fxml");
//            controller.setCurrentUser(currentUser);
//        } catch (IOException e) {
//            showError("Failed to load profile: " + e.getMessage());
//        }
    }

    @FXML
    private void handleLogout() {
        try {
            SceneManager.loadAndSwitch("/view/Login.fxml");
        } catch (IOException e) {
            showError("Logout failed: " + e.getMessage());
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}