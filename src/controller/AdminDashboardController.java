package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Model.Book;
import Model.BorrowRecord;
import DAO.UserDAO;
import DAO.BookDAO;
import DAO.BorrowRecordDAO;
import Model.User;
import util.SceneManager;
import java.io.IOException;
import java.util.List;

public class AdminDashboardController {
    @FXML private Label welcomeLabel, totalUsersLabel, totalBooksLabel, overdueBooksLabel, statusLabel;
    @FXML private TableView<BorrowRecord> recentActivityTable;
    @FXML private TableView<User> usersTable;
    @FXML private TableView<Book> booksTable;
    @FXML private TextField userSearchField, bookSearchField;
    
    // Table columns for recent activity
    @FXML private TableColumn<BorrowRecord, String> dateColumn, activityColumn, userColumn;
    
    // Table columns for user management
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> usernameColumn, nameColumn, userTypeColumn;
    
    // Table columns for book management
    @FXML private TableColumn<Book, Integer> bookIdColumn;
    @FXML private TableColumn<Book, String> bookTitleColumn, bookAuthorColumn, bookStatusColumn;
    
    private User currentUser;
    private final UserDAO userDAO = new UserDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText(user.getFullName());
        initializeTables();
        loadDashboardData();
    }

    private void initializeTables() {
        // Recent Activity Table
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        
        // User Management Table
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
        
        // Book Management Table
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadDashboardData() {
        // Load statistics
        totalUsersLabel.setText(String.valueOf(userDAO.getUserCount()));
        totalBooksLabel.setText(String.valueOf(bookDAO.getBookCount()));
        overdueBooksLabel.setText(String.valueOf(borrowRecordDAO.getOverdueBooksCount()));
        
        // Load recent activity
        recentActivityTable.getItems().setAll(borrowRecordDAO.getRecentTransactions(10));
        
        // Load all users
        usersTable.getItems().setAll(userDAO.getAllUsers());
        
        // Load all books
        booksTable.getItems().setAll(bookDAO.getAllBooks());
    }

    @FXML
    private void handleUserManagement() {
        usersTable.getItems().setAll(userDAO.getAllUsers());
    }

    @FXML
    private void handleBookManagement() {
        booksTable.getItems().setAll(bookDAO.getAllBooks());
    }
    
    @FXML
    private void handleUserRefresh() {
        usersTable.getItems().setAll(userDAO.getAllUsers());
    }
    
    @FXML
    private void handleBookRefresh() {
        booksTable.getItems().setAll(bookDAO.getAllBooks());
    }

    @FXML
    private void handleUserSearch() {
        String searchTerm = userSearchField.getText();
        List<User> results = searchTerm.isEmpty() ? 
            userDAO.getAllUsers() : 
            userDAO.searchUsers(searchTerm);
        usersTable.getItems().setAll(results);
    }

    @FXML
    private void handleBookSearch() {
        String searchTerm = bookSearchField.getText();
        List<Book> results = searchTerm.isEmpty() ? 
            bookDAO.getAllBooks() : 
            bookDAO.searchBooks(searchTerm);
        booksTable.getItems().setAll(results);
    }

    @FXML
    private void handleAddUser() {
        try {
            RegisterController controller = SceneManager.loadAndSwitch("/view/Register.fxml");
            controller.setCurrentUser(currentUser);
        } catch (IOException e) {
            showError("Failed to load registration form: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddBook() {
//        try {
//            AddBookController controller = SceneManager.loadAndSwitch("/view/AddBook.fxml");
//            controller.setCurrentUser(currentUser);
//        } catch (IOException e) {
//            showError("Failed to load add book form: " + e.getMessage());
//        }
    }

    @FXML
    private void handleReports() {
//        try {
//            ReportsController controller = SceneManager.loadAndSwitch("/view/Reports.fxml");
//            controller.setCurrentUser(currentUser);
//        } catch (IOException e) {
//            showError("Failed to load reports: " + e.getMessage());
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