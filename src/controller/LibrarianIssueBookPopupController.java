package controller;

import DAO.BorrowRecordDAO;
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

public class LibrarianIssueBookPopupController {

    @FXML private ComboBox<Book> cbBooks;
    @FXML private ComboBox<User> cbUsers;
    @FXML private Label lblError;
    
    private ObservableList<Book> availableBooks;
    private ObservableList<User> availableUsers;
    private User currentUser;
    private Runnable onSuccess;
    private final UserDAO userDAO = new UserDAO();
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();

    @FXML
    public void initialize() {
        cbBooks.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
            @Override
            public ListCell<Book> call(ListView<Book> param) {
                return new ListCell<Book>() {
                    @Override
                    protected void updateItem(Book item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getTitle() + " by " + item.getAuthor() + " (ISBN: " + item.getIsbn() + ")");
                        }
                    }
                };
            }
        });
        
        cbBooks.setButtonCell(new ListCell<Book>() {
            @Override
            protected void updateItem(Book item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getTitle() + " by " + item.getAuthor());
                }
            }
        });
        
        cbUsers.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getFirstName() + " " + item.getLastName() + " (ID: " + item.getUserId() + ")");
                        }
                    }
                };
            }
        });
        
        cbUsers.setButtonCell(new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName());
                }
            }
        });
    }

    public void setAvailableBooks(List<Book> books) {
        this.availableBooks = FXCollections.observableArrayList(books);
        cbBooks.setItems(availableBooks);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadAvailableUsers();
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    private void loadAvailableUsers() {
        List<User> users = userDAO.getAllMembers();
        availableUsers = FXCollections.observableArrayList(users);
        cbUsers.setItems(availableUsers);
    }

    @FXML
    private void handleIssueBook() {
        Book selectedBook = cbBooks.getSelectionModel().getSelectedItem();
        User selectedUser = cbUsers.getSelectionModel().getSelectedItem();
        
        if (selectedBook == null) {
            lblError.setText("Please select a book");
            return;
        }
        
        if (selectedUser == null) {
            lblError.setText("Please select a user");
            return;
        }

        BorrowRecord record = new BorrowRecord();
        record.setBookId(selectedBook.getBookId());
        record.setUserId(selectedUser.getUserId());

        String result = borrowRecordDAO.issueBook(record);
        
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