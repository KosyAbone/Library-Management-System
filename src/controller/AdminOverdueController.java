package controller;

import DAO.BorrowRecordDAO;
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

public class AdminOverdueController {

    @FXML private TableView<BorrowRecord> overdueTable;
    @FXML private TableColumn<BorrowRecord, Integer> colUserId;
    @FXML private TableColumn<BorrowRecord, String> colUsername;
    @FXML private TableColumn<BorrowRecord, String> colBookTitle;
    @FXML private TableColumn<BorrowRecord, String> colDueDate;
    @FXML private TableColumn<BorrowRecord, String> colBorrowedDate;

    @FXML private TextField txtSearch;
    @FXML private Label lblSearchAlert;

    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadOverdueRecords();
    }

    private void setupTableColumns() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colBorrowedDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
    }

    private void loadOverdueRecords() {
        List<BorrowRecord> records = borrowRecordDAO.getOverdueBooks();
        ObservableList<BorrowRecord> data = FXCollections.observableArrayList(records);
        overdueTable.setItems(data);
    }

    @FXML
    private void txtSearchOnAction() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            lblSearchAlert.setText("Please enter a username or user ID");
            return;
        }
        lblSearchAlert.setText("");

        List<BorrowRecord> records = borrowRecordDAO.getOverdueBooks();
        ObservableList<BorrowRecord> filtered = FXCollections.observableArrayList();

        for (BorrowRecord record : records) {
            if (String.valueOf(record.getUserId()).contains(keyword) ||
                (record.getUserName() != null && record.getUserName().toLowerCase().contains(keyword))) {
                filtered.add(record);
            }
        }

        overdueTable.setItems(filtered);
    }

    @FXML
    private void btnRefreshTableOnAction() {
        txtSearch.clear();
        lblSearchAlert.setText("");
        loadOverdueRecords();
    }

    @FXML
    private void btnBorrowedBooksOnAction(ActionEvent event) throws IOException {
            Navigation.switchNavigation("AdminBorrowed.fxml", event);
    }
}
