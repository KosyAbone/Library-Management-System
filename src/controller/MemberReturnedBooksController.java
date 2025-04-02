package controller;

import DAO.BorrowRecordDAO;
import Model.BorrowRecord;
import Model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import util.Navigation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MemberReturnedBooksController {

    @FXML private TableView<BorrowRecord> returnedBooksTable;
    @FXML private TableColumn<BorrowRecord, String> colIsbn;
    @FXML private TableColumn<BorrowRecord, String> colTitle;
    @FXML private TableColumn<BorrowRecord, String> colReturnDate;
    @FXML private TableColumn<BorrowRecord, String> colBorrowDate;
    @FXML private TableColumn<BorrowRecord, String> colStatus;

    @FXML private TextField txtSearch;
    @FXML private Label lblSearchAlert;

    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        loadReturnedBooks();
    }

    @FXML
    public void initialize() {
        colIsbn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getBookId())));
        colTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));
        colReturnDate.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getReturnDate() != null ? data.getValue().getReturnDate().toString() : "-"
        ));
        colBorrowDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBorrowDate().toString()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
    }

    private void loadReturnedBooks() {
        List<BorrowRecord> records = borrowRecordDAO.getBorrowRecordsByUser(currentUser.getUserId())
                .stream()
                .filter(r -> "RETURNED".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toList());

        returnedBooksTable.getItems().setAll(records);
        lblSearchAlert.setText(records.isEmpty() ? "No returned books found." : "");
    }

    @FXML
    private void txtSearchOnAction() {
        String keyword = txtSearch.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            loadReturnedBooks();
            return;
        }

        List<BorrowRecord> filtered = borrowRecordDAO.getBorrowRecordsByUser(currentUser.getUserId())
            .stream()
            .filter(r -> "RETURNED".equalsIgnoreCase(r.getStatus()))
            .filter(r -> r.getBookTitle() != null && r.getBookTitle().toLowerCase().contains(keyword))
            .collect(Collectors.toList());

        returnedBooksTable.getItems().setAll(filtered);
        lblSearchAlert.setText(filtered.isEmpty() ? "No books matching title." : "");
    }


    @FXML
    private void btnBorrowedBooksOnAction(ActionEvent event) throws IOException {
            Navigation.switchNavigation("MemberBorrowedBooks.fxml", event); {
            
        }
}

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    @FXML
    private void btnHomeOnAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("MemberDashboard.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnLogOutAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
