package controller;

import DAO.BookDAO;
import DAO.BorrowRecordDAO;
import DAO.UserDAO;
import Model.Book;
import Model.BorrowRecord;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import util.Navigation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LibrarianBookManagementController implements UserAwareController {

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colTotalQty;
    @FXML private TableColumn<Book, Integer> colAvailableQty;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TextField txtSearch;
    @FXML private Button btnIssueBook;
    @FXML private Button btnReturnBook;
    
    private final BookDAO bookDAO = new BookDAO();
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private final UserDAO userDAO = new UserDAO();
    private User currentUser;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @Override
    public void setUser(User user) {
        this.currentUser = user;
        initialize();
        loadBooks();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
    }

    private void setupTableColumns() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colTotalQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colAvailableQty.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadBooks() {
        List<Book> books = bookDAO.getAllBooks();
        bookList.setAll(books);
        booksTable.setItems(bookList);
    }

    @FXML
    private void handleSearch() {
        String query = txtSearch.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            booksTable.setItems(bookList);
            return;
        }

        ObservableList<Book> filteredList = bookList.filtered(book ->
            book.getTitle().toLowerCase().contains(query) ||
            book.getAuthor().toLowerCase().contains(query) ||
            book.getIsbn().toLowerCase().contains(query)
        );
        booksTable.setItems(filteredList);
    }

    @FXML
    private void handleIssueBook(ActionEvent event) {
        try {
            List<Book> availableBooks = bookList.stream()
                .filter(book -> book.getAvailableQuantity() > 0)
                .collect(Collectors.toList());
            
            if (availableBooks.isEmpty()) {
                showAlert("No Books Available", "There are no books available for issuing.");
                return;
            }
            
            Navigation.openPopup("LibrarianIssueBookPopup.fxml", controller -> {
                LibrarianIssueBookPopupController popupController = (LibrarianIssueBookPopupController) controller;
                popupController.setAvailableBooks(availableBooks);
                popupController.setCurrentUser(currentUser);
                popupController.setOnSuccess(this::loadBooks);
            });
        } catch (IOException e) {
            showAlert("Error", "Failed to open dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleReturnBook(ActionEvent event) {
        try {
            List<BorrowRecord> borrowedBooks = borrowRecordDAO.getAllBorrowRecords();
            
            if (borrowedBooks.isEmpty()) {
                showAlert("No Books Borrowed", "There are no books currently borrowed.");
                return;
            }
            
            Navigation.openPopup("LibrarianReturnBookPopup.fxml", controller -> {
                LibrarianReturnBookPopupController popupController = (LibrarianReturnBookPopupController) controller;
                popupController.setBorrowedBooks(borrowedBooks);
                popupController.setCurrentUser(currentUser);
                popupController.setOnSuccess(this::loadBooks);
            });
        } catch (IOException e) {
            showAlert("Error", "Failed to open dialog: " + e.getMessage());
        }
    }

    @FXML
    private void btnHomeOnAction(ActionEvent event) throws IOException {
        Navigation.switchNavigation("LibrarianDashboard.fxml", event, 
            controller -> ((UserAwareController)controller).setUser(currentUser));
    }

    @FXML
    private void btnLogOutAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (IOException e) {
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}