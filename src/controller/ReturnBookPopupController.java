package controller;

import DAO.BookDAO;
import DAO.BorrowRecordDAO;
import Model.Book;
import Model.BorrowRecord;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.Consumer;

public class ReturnBookPopupController {

    @FXML private TableView<BorrowRecord> returnTable;
    @FXML private TableColumn<BorrowRecord, String> colIsbn;
    @FXML private TableColumn<BorrowRecord, String> colTitle;
    @FXML private TableColumn<BorrowRecord, String> colAuthor;
    @FXML private Label lblReturn, lblCancel;

    private BorrowRecord borrowRecord;
    private Consumer<Void> onReturnSuccess;

    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private final BookDAO bookDAO = new BookDAO();

    public void setBorrowRecord(BorrowRecord record) {
        this.borrowRecord = record;
        returnTable.getItems().setAll(record);
    }

    public void setOnReturnSuccess(Consumer<Void> callback) {
        this.onReturnSuccess = callback;
    }

    @FXML
    public void initialize() {
        colIsbn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));  // or .getIsbn() if added
        colTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));
        colAuthor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserName())); // Change to author if needed
    }

    @FXML
    private void btnReturnOnAction() {
        if (borrowRecord == null) {
            showAlert("No book selected to return.");
            return;
        }

        borrowRecord.setReturnDate(Date.valueOf(LocalDate.now()));
        borrowRecord.setStatus("RETURNED");

        boolean success = borrowRecordDAO.updateBorrowRecord(borrowRecord);
        if (success) {
            bookDAO.updateAvailableQuantity(borrowRecord.getBookId(), 1);

            if (onReturnSuccess != null) {
                onReturnSuccess.accept(null);
            }

            closePopup();
        } else {
            showAlert("Return failed. Please try again.");
        }
    }

    @FXML 
    private void btnCancelOnAction() { 
        closePopup(); 
    }


    private void closePopup() {
        Stage stage = (Stage) returnTable.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Return Book");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
