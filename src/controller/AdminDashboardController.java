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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;

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
    @FXML private TableColumn<User, String> usernameColumn, nameColumn, userTypeColumn, userActionsColumn;
    
    // Table columns for book management
    @FXML private TableColumn<Book, Integer> bookIdColumn;
    @FXML private TableColumn<Book, String> bookTitleColumn, bookAuthorColumn, bookStatusColumn, bookActionsColumn;
    
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
        userActionsColumn.setCellFactory(param -> {
            return new TableCell<User, String>() {
                private final Button editUserButton = new Button("Edit");
                private final Button deleteUserButton = new Button("Delete");
                
                {
                    // Set the edit button action
                    editUserButton.setOnAction(event -> handleEditUser(getTableRow().getItem()));
                    
                    // Set the delete button action
                    deleteUserButton.setOnAction(event -> handleDeleteUser(getTableRow().getItem()));
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);  // No buttons when the row is empty
                    } else {
                        // Create a horizontal layout for the buttons
                        HBox hBox = new HBox(10, editUserButton, deleteUserButton);
                        setGraphic(hBox);
                    }
                }

                private void handleDeleteUser(User user) {
                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }

                private void handleEditUser(User user) {
                    showUserRegForm(user);
                }
            };
        });
        
        // Book Management Table
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        // Set up the Actions column with edit and delete buttons
        bookActionsColumn.setCellFactory(param -> {
            return new TableCell<Book, String>() {
                private final Button editBookButton = new Button("Edit");
                private final Button deleteBookButton = new Button("Delete");
                
                {
                    // Set the edit button action
                    editBookButton.setOnAction(event -> handleEditBook(getTableRow().getItem()));
                    
                    // Set the delete button action
                    deleteBookButton.setOnAction(event -> handleDeleteBook(getTableRow().getItem()));
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);  // No buttons when the row is empty
                    } else {
                        // Create a horizontal layout for the buttons
                        HBox hBox = new HBox(10, editBookButton, deleteBookButton);
                        setGraphic(hBox);
                    }
                }

                private void handleDeleteBook(Book item) {
                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }

                private void handleEditBook(Book item) {
                    showBookForm(item);
                }
            };
        });
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
        showUserRegForm(null);
    }

    @FXML
    private void handleAddBook() {
        showBookForm(null);
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
    
    private void showUserRegForm(User user) {
        try {
            SceneManager.loadModal(
                "/view/Register.fxml",
                user == null ? "Add New User" : "Edit User Information",
                Modality.APPLICATION_MODAL,
                (RegisterController controller) -> {
                    controller.setCurrentUser(currentUser);
                    controller.setEditing(user != null);
                    if (user != null) {
//                        controller.populateForm(user);
                    }
                }
            );
            handleUserRefresh();
        } catch (IOException e) {
            showError("Failed to load registration form: " + e.getMessage());
        }
    }   

    private void showBookForm(Book book) {
        try {
            SceneManager.loadModal(
                "/view/BookForm.fxml", 
                book == null ? "Add New Book" : "Edit Book",
                Modality.APPLICATION_MODAL,
                (BookFormController controller) -> {
                    controller.setBook(book);
                }
            );

            // Refresh after the popup closes
            handleBookRefresh();
        } catch (IOException e) {
            showError("Failed to load book form: " + e.getMessage());
        }
    }

//    private void refreshBooks() {
//        booksTable.setItems(FXCollections.observableArrayList(bookDAO.getAllBooks()));
//    }

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