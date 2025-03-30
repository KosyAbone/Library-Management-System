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
import javafx.beans.property.SimpleIntegerProperty;

public class LibrarianDashboardController {
    @FXML private Label welcomeLabel, availableBooksLabel, checkedOutBooksLabel, overdueBooksLabel, statusLabel;
    @FXML private TableView<BorrowRecord> todaysCheckoutsTable;
    @FXML private TableView<Book> booksTable;
    @FXML private TableView<User> membersTable;
    @FXML private TextField bookSearchField, memberSearchField;
    
    // Table columns
    @FXML private TableColumn<BorrowRecord, String> bookTitleColumn, memberUserNameColumn, dueDateColumn;
    @FXML private TableColumn<Book, Integer> bookIdColumn;
    @FXML private TableColumn<Book, String> bookTitle, bookAuthorColumn, bookStatusColumn;
    @FXML private TableColumn<User, Integer> memberIdColumn, booksOutColumn;
    @FXML private TableColumn<User, String> memberFullNameColumn, memberTypeColumn;
    
    private User currentUser;
    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getFullName());
        initializeTables();
        loadDashboardData();
    }

    private void initializeTables() {
        // Today's Checkouts Table
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        memberUserNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        
        // Book Management Table
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Member Management Table
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        memberFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        memberTypeColumn.setCellValueFactory(new PropertyValueFactory<>("memberType"));
        booksOutColumn.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getBorrowedBooksCount()).asObject());
    }

    private void loadDashboardData() {
        availableBooksLabel.setText(String.valueOf(bookDAO.getAvailableBookCount()));
        checkedOutBooksLabel.setText(String.valueOf(bookDAO.getCheckedOutBookCount()));
        overdueBooksLabel.setText(String.valueOf(borrowRecordDAO.getOverdueBooksCount()));
        
        todaysCheckoutsTable.getItems().setAll(borrowRecordDAO.getTodaysCheckouts());
        booksTable.getItems().setAll(bookDAO.getAllBooks());
        
        // Load only member accounts
        membersTable.getItems().setAll(userDAO.getAllMembers());
    }

    @FXML
    private void handleBookManagement() {
        booksTable.getItems().setAll(bookDAO.getAllBooks());
    }

    @FXML
    private void handleMemberManagement() {
        membersTable.getItems().setAll(userDAO.getAllMembers());
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
    private void handleMemberSearch() {
        String searchTerm = memberSearchField.getText();
        List<User> results = searchTerm.isEmpty() ? 
            userDAO.getAllMembers() : 
            userDAO.searchUsers(searchTerm);
        membersTable.getItems().setAll(results);
    }

    @FXML
    private void handleCheckOut() {
//        try {
//            CheckOutController controller = SceneManager.loadAndSwitch("/view/CheckOut.fxml");
//            controller.setCurrentUser(currentUser);
//        } catch (IOException e) {
//            showError("Failed to load checkout form: " + e.getMessage());
//        }
    }

    @FXML
    private void handleReturns() {
//        try {
//            ReturnController controller = SceneManager.loadAndSwitch("/view/Return.fxml");
//            controller.setCurrentUser(currentUser);
//        } catch (IOException e) {
//            showError("Failed to load returns form: " + e.getMessage());
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