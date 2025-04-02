package controller;

import DAO.BookDAO;
import DAO.BorrowRecordDAO;
import Model.Book;
import Model.BorrowRecord;
import Model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.DateTime;

import java.sql.Date;

public class BorrowBookPopupController {

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, String> colStatus;

    @FXML private Label lblConfirm;
    @FXML private Label lblCancel;

    private Book selectedBook;
    private User currentUser;
    private Runnable onBorrowSuccess;

    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private final BookDAO bookDAO = new BookDAO();

    public void setBook(Book book) {
        this.selectedBook = book;
        booksTable.setItems(FXCollections.observableArrayList(book));
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setOnBorrowSuccess(Runnable callback) {
        this.onBorrowSuccess = callback;
    }

    @FXML
    public void initialize() {
        colIsbn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIsbn()));
        colTitle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        colAuthor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAuthor()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
    }

    @FXML
    private void btnConfirmOnAction() {
        if (selectedBook == null || currentUser == null) {
            showAlert("Error", "Book or user not set.");
            return;
        }

        BorrowRecord record = new BorrowRecord();
        record.setBookId(selectedBook.getBookId());
        record.setUserId(currentUser.getUserId());
        record.setBorrowDate(Date.valueOf(DateTime.dateNow()));
        record.setDueDate(Date.valueOf(DateTime.dateAftermonth()));
        record.setStatus("BORROWED");

        boolean success = borrowRecordDAO.addBorrowRecord(record);

        if (success) {
            bookDAO.updateAvailableQuantity(selectedBook.getBookId(), -1);
            if (onBorrowSuccess != null) {
                onBorrowSuccess.run();
            }
            closePopup();
        } else {
            showAlert("Borrow Failed", "Could not complete borrow request.");
        }
    }

    @FXML
    private void btnCancelOnAction() {
        closePopup();
    }

    @FXML
    private void btnConfirmOnMouseEntered() {
        lblConfirm.setStyle("-fx-underline: true;");
    }

    @FXML
    private void btnConfirmOnMouseExited() {
        lblConfirm.setStyle("-fx-underline: false;");
    }

    @FXML
    private void btnCancelOnMouseEntered() {
        lblCancel.setStyle("-fx-underline: true;");
    }

    @FXML
    private void btnCancelOnMouseExited() {
        lblCancel.setStyle("-fx-underline: false;");
    }

    private void closePopup() {
        Stage stage = (Stage) booksTable.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
