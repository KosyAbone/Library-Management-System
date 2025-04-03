package controller;

import DAO.BorrowRecordDAO;
import DAO.BookDAO;
import DAO.UserDAO;
import Model.BorrowRecord;
import Model.User;
import Model.Book;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class LibrarianReturnBookPopupController {

    @FXML private ComboBox<BorrowRecord> cbBorrowedBooks;
    @FXML private Label lblError;
    
    private ObservableList<BorrowRecord> borrowedBooks;
    private User currentUser;
    private Runnable onSuccess;
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        // Set up custom cell factory for the ComboBox
        cbBorrowedBooks.setCellFactory(new Callback<ListView<BorrowRecord>, ListCell<BorrowRecord>>() {
            @Override
            public ListCell<BorrowRecord> call(ListView<BorrowRecord> param) {
                return new ListCell<BorrowRecord>() {
                    @Override
                    protected void updateItem(BorrowRecord item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            Book book = bookDAO.getBookById(item.getBookId());
                            User user = userDAO.getUserById(item.getUserId());
                            if (book != null && user != null) {
                                setText(book.getTitle() + " borrowed by " + user.getFirstName() + " " + user.getLastName());
                            } else {
                                setText("Record ID: " + item.getBorrowId());
                            }
                        }
                    }
                };
            }
        });
        
        cbBorrowedBooks.setButtonCell(new ListCell<BorrowRecord>() {
            @Override
            protected void updateItem(BorrowRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    Book book = bookDAO.getBookById(item.getBookId());
                    User user = userDAO.getUserById(item.getUserId());
                    if (book != null && user != null) {
                        setText(book.getTitle() + " - " + user.getFirstName());
                    } else {
                        setText("Record ID: " + item.getBorrowId());
                    }
                }
            }
        });
    }

    public void setBorrowedBooks(List<BorrowRecord> books) {
        this.borrowedBooks = FXCollections.observableArrayList(books);
        cbBorrowedBooks.setItems(borrowedBooks);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    @FXML
    private void handleReturnBook() {
        BorrowRecord selectedRecord = cbBorrowedBooks.getSelectionModel().getSelectedItem();
        
        if (selectedRecord == null) {
            lblError.setText("Please select a book to return");
            return;
        }

        String result = borrowRecordDAO.returnBook(selectedRecord.getBorrowId());
        
        if (result.startsWith("Success")) {
            if (onSuccess != null) onSuccess.run();
            closeWindow();
        } else {
            String errorMessage = result.replace("Error: ", "");
            lblError.setText(errorMessage);
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        lblError.getScene().getWindow().hide();
    }
}