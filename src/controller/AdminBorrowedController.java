package controller;

import DAO.BorrowRecordDAO;
import Model.BorrowRecord;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import util.Navigation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminBorrowedController {

    @FXML private TableView<BorrowRecord> tableBorrowedBooks;
    @FXML private TableColumn<BorrowRecord, Integer> colBookId;
    @FXML private TableColumn<BorrowRecord, String> colBookTitle;
    @FXML private TableColumn<BorrowRecord, String> colUsername;
    @FXML private TableColumn<BorrowRecord, String> colBorrowDate;
    @FXML private TableColumn<BorrowRecord, String> colDueDate;
    @FXML private TextField txtSearch;
    @FXML private Label lblSearchAlert;

    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
     private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        List<BorrowRecord> records = borrowRecordDAO.getAllBorrowRecords().stream()
                .filter(record -> "BORROWED".equalsIgnoreCase(record.getStatus()))
                .collect(Collectors.toList());

        ObservableList<BorrowRecord> data = FXCollections.observableArrayList(records);
        tableBorrowedBooks.setItems(data);
    }

    @FXML
    private void txtSearchOnAction() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            lblSearchAlert.setText("Please enter ISBN or Title to search.");
            return;
        }
        
        List<BorrowRecord> filtered = borrowRecordDAO.getAllBorrowRecords().stream()
            .filter(record -> "BORROWED".equalsIgnoreCase(record.getStatus()))
            .filter(r ->
                    String.valueOf(r.getBookId()).contains(keyword) || 
                    (r.getBookTitle() != null && r.getBookTitle().toLowerCase().contains(keyword))
            )
            .collect(Collectors.toList());

    tableBorrowedBooks.setItems(FXCollections.observableArrayList(filtered));
    lblSearchAlert.setText(filtered.isEmpty() ? "No matching records found." : "");
}

    @FXML
    private void btnRefreshTableOnAction() {
        txtSearch.clear();
        lblSearchAlert.setText("");
        loadBorrowedBooks();
    }

    @FXML
    private void btnOverdueBorrowersOnAction(ActionEvent event) throws IOException {
            Navigation.switchNavigation("AdminOverdue.fxml", event);
    }
}
