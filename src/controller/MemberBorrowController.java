package controller;

import DAO.BookDAO;
import Model.Book;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;
import util.Navigation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MemberBorrowController {

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, Integer> bookIdColumn;
    @FXML private TableColumn<Book, String> bookTitle;
    @FXML private TableColumn<Book, String> bookAuthorColumn;
    @FXML private TableColumn<Book, String> bookStatusColumn;
    @FXML private TableColumn<Book, Void> bookActionsColumn;
    @FXML private TextField bookSearchField;
    @FXML private Label lblSearchAlert;

    private final BookDAO bookDAO = new BookDAO();
    private User currentUser;
    private Book selectedBook;
    private Pane pagingPane;
    
    public void setPagingPane(Pane pagingPane) {
        this.pagingPane = pagingPane;
    }
    
    public void setUser(User user) {
        this.currentUser = user;
        
        System.out.println("First User on Member borrow " + currentUser);
    }

    @FXML
    public void initialize() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadBooks();
        addBorrowButtonToTable();
        
        bookSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleBookSearch(); // Call search on every text change
        });
    }

    private void loadBooks() {
        List<Book> availableBooks = bookDAO.getAvailableBooks();

        booksTable.getItems().setAll(availableBooks);
        lblSearchAlert.setText(availableBooks.isEmpty() ? "No available books found." : "");
    }

    @FXML
    private void handleBookSearch() {
        String search = bookSearchField.getText().trim().toLowerCase();

        if (search.isEmpty()) {
            loadBooks();
            return;
        }

        List<Book> filtered = bookDAO.getAvailableBooks().stream()
                .filter(book ->
                        book.getTitle().toLowerCase().contains(search) ||
                        book.getAuthor().toLowerCase().contains(search) ||
                        book.getIsbn().toLowerCase().contains(search))
                .collect(Collectors.toList());
        
        booksTable.getItems().setAll(filtered);
        lblSearchAlert.setText(filtered.isEmpty() ? "No books matching your search." : "");
    }

    @FXML
    private void btnRefreshTableOnAction() {
        bookSearchField.clear();
        loadBooks();
    }

    @FXML
    private void btnBorrowBookOnAction() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Please select a book to borrow.");
            return;
        }

        openBorrowPopup(selectedBook);
    }
    
    
    
    private void addBorrowButtonToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btn = new Button("Borrow");

            {
                btn.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 6;");
                btn.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    
                    openBorrowPopup(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        };

        bookActionsColumn.setCellFactory(cellFactory);
    }

    private void openBorrowPopup(Book book) {
    try {
        Navigation.openPopup("BorrowBookPopup.fxml", (BorrowBookPopupController controller) -> {
            controller.setUser(currentUser);
            controller.setBook(book);
            controller.setOnBorrowSuccess(this::loadBooks);
            
            System.out.println("book from nav" + book);
            System.out.println("user from nav" + currentUser);
        });
    } catch (IOException e) {
        e.printStackTrace();
    }
}



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
