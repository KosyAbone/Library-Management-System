package controller;

import DAO.BookDAO;
import DAO.BorrowRecordDAO;
import Model.BorrowRecord;
import Model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import util.Navigation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MemberBorrowedBooksController {

    @FXML private TableView<BorrowRecord> borrowedBooksTable;
    @FXML private TableColumn<BorrowRecord, String> colIsbn;
    @FXML private TableColumn<BorrowRecord, String> colTitle;
    @FXML private TableColumn<BorrowRecord, String> colDueDate;
    @FXML private TableColumn<BorrowRecord, String> colBorrowDate;
    @FXML private TableColumn<BorrowRecord, Void> colAction;

    @FXML private TextField txtSearch;
    @FXML private Label lblSearchAlert;

    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private final BookDAO bookDAO = new BookDAO();
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        loadBorrowedBooks();
    }

    @FXML
    public void initialize() {
        colIsbn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getBookId())));
        colTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));
        colDueDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDueDate().toString()));
        colBorrowDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBorrowDate().toString()));

        addReturnButtonToTable();
        highlightOverdueRows();
    }

    private void loadBorrowedBooks() {
        List<BorrowRecord> records = borrowRecordDAO.getUserActiveLoans(currentUser.getUserId());
        borrowedBooksTable.getItems().setAll(records);
        lblSearchAlert.setText(records.isEmpty() ? "No borrowed books found." : "");
    }

    @FXML
    private void txtSearchOnAction() {
        String keyword = txtSearch.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            loadBorrowedBooks();
            return;
        }

        List<BorrowRecord> filtered = borrowRecordDAO.getUserActiveLoans(currentUser.getUserId())
                .stream()
                .filter(r -> r.getBookTitle() != null && r.getBookTitle().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        borrowedBooksTable.getItems().setAll(filtered);
        lblSearchAlert.setText(filtered.isEmpty() ? "No books matching title." : "");
    }

    @FXML
    private void btnReturnedBooksOnAction(ActionEvent event) throws IOException {
        Navigation.switchNavigation("MemberReturnedBooks.fxml", event); {
        }
    }


    private void addReturnButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Return");

            {
                btn.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 6;");
                btn.setOnAction(event -> {
                    BorrowRecord record = getTableView().getItems().get(getIndex());
                    openReturnPopup(record);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    private void highlightOverdueRows() {
        borrowedBooksTable.setRowFactory(tableView -> new TableRow<>() {
            @Override
            protected void updateItem(BorrowRecord record, boolean empty) {
                super.updateItem(record, empty);
                if (record == null || empty) {
                    setStyle("");
                } else if (record.getDueDate().before(new Date(System.currentTimeMillis()))) {
                    setStyle("-fx-background-color: #ffd6d6;"); // light red for overdue
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void openReturnPopup(BorrowRecord record) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReturnBookPopup.fxml"));
            Parent root = loader.load();

            ReturnBookPopupController controller = loader.getController();
            controller.setBorrowRecord(record);
            controller.setOnReturnSuccess(v -> loadBorrowedBooks());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Return Book");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            showAlert("Failed to open return popup: " + e.getMessage());
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
