package controller;

import DAO.BorrowRecordDAO;
import DAO.FineDAO;
import Model.BorrowRecord;
import Model.Fine;
import Model.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import util.Navigation;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class LibrarianOverdueBooksController {

    @FXML private TableView<BorrowRecord> overdueTable;
    @FXML private TableColumn<BorrowRecord, String> colBookTitle;
    @FXML private TableColumn<BorrowRecord, String> colMemberName;
    @FXML private TableColumn<BorrowRecord, Date> colDueDate;
    @FXML private TableColumn<BorrowRecord, Integer> colDaysOverdue;
    @FXML private TableColumn<BorrowRecord, Double> colFineAmount;
    @FXML private TableColumn<BorrowRecord, String> colFineStatus;
    @FXML private TextField txtSearch;
    
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private final FineDAO fineDAO = new FineDAO();
    private User currentUser;
    private static final double DAILY_FINE_RATE = 10.0;
    private Map<Integer, String> fineStatusMap = new HashMap<>();
    private Map<Integer, Double> fineAmountMap = new HashMap<>();

    public void setUser(User user) {
        this.currentUser = user;
        initializeTable();
        loadOverdueBooks();
    }

    private void initializeTable() {
        colBookTitle.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBookTitle()));

        colMemberName.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getUserName()));

        colDueDate.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getDueDate()));

        // Days Overdue column
        colDaysOverdue.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    BorrowRecord record = getTableRow().getItem();
                    long daysOverdue = ChronoUnit.DAYS.between(
                        record.getDueDate().toLocalDate(),
                        LocalDate.now()
                    );
                    setText(String.valueOf(daysOverdue));
                }
            }
        });
        
        // Fine Amount column
        colFineAmount.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    BorrowRecord record = getTableRow().getItem();
                    double amount = fineAmountMap.getOrDefault(record.getBorrowId(), 0.0);
                    setText(String.format("$%.2f", amount));
                }
            }
        });
        
        // Fine Status column
        colFineStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    BorrowRecord record = getTableRow().getItem();
                    String status = fineStatusMap.getOrDefault(record.getBorrowId(), "UNPAID");
                    setText(status);
                    if ("PAID".equals(status)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    private void loadOverdueBooks() {
        try {

            List<BorrowRecord> overdueRecords = borrowRecordDAO.getOverdueBooks();

            if (overdueRecords.isEmpty()) {
                showAlert("Information", "No overdue books found");
                return;
            }

            // Preload fine information
            fineStatusMap.clear();
            fineAmountMap.clear();

            overdueRecords.forEach(record -> {
                System.out.println("Processing record: " + record.getBookTitle());

                List<Fine> fines = fineDAO.getFinesByBorrowId(record.getBorrowId());
                if (!fines.isEmpty()) {
                    Fine fine = fines.get(0);
                    fineStatusMap.put(record.getBorrowId(), fine.getStatus());
                    fineAmountMap.put(record.getBorrowId(), fine.getAmount());

                } else {
                    long daysOverdue = ChronoUnit.DAYS.between(
                        record.getDueDate().toLocalDate(),
                        LocalDate.now()
                    );
                    double calculatedFine = daysOverdue * DAILY_FINE_RATE;
                    fineAmountMap.put(record.getBorrowId(), calculatedFine);
                    fineStatusMap.put(record.getBorrowId(), "UNPAID");
                }
            });

            overdueTable.setItems(FXCollections.observableArrayList(overdueRecords));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load overdue books: " + e.getMessage());
        }
    }

    @FXML
    private void handleMarkAsPaid(ActionEvent event) {
        BorrowRecord selectedRecord = overdueTable.getSelectionModel().getSelectedItem();
        if (selectedRecord == null) {
            showAlert("No Selection", "Please select an overdue book to mark as paid");
            return;
        }

        List<Fine> existingFines = fineDAO.getFinesByBorrowId(selectedRecord.getBorrowId());
        Fine fine;
        
        if (existingFines.isEmpty()) {
            // Create new fine
            fine = new Fine();
            fine.setBorrowId(selectedRecord.getBorrowId());
            fine.setUserId(selectedRecord.getUserId());
            long daysOverdue = ChronoUnit.DAYS.between(
                selectedRecord.getDueDate().toLocalDate(),
                LocalDate.now()
            );
            fine.setAmount(daysOverdue * DAILY_FINE_RATE);
            fine.setStatus("PAID");
            fine.setReason("Overdue book fine");
            fine.setIssuedDate(new Timestamp(System.currentTimeMillis()));
            fine.setPaidDate(new Timestamp(System.currentTimeMillis()));
            
            if (!fineDAO.addFine(fine)) {
                showAlert("Error", "Failed to create fine record");
                return;
            }
        } else {
            // Update existing fine
            fine = existingFines.get(0);
            fine.setStatus("PAID");
            fine.setPaidDate(new Timestamp(System.currentTimeMillis()));
            
            if (!fineDAO.updateFine(fine)) {
                showAlert("Error", "Failed to update fine record");
                return;
            }
        }
        
        // Update local maps
        fineStatusMap.put(selectedRecord.getBorrowId(), "PAID");
        fineAmountMap.put(selectedRecord.getBorrowId(), fine.getAmount());
        
        showAlert("Success", "Fine marked as paid successfully");
        overdueTable.refresh();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = txtSearch.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            loadOverdueBooks();
            return;
        }

        List<BorrowRecord> filtered = borrowRecordDAO.getOverdueBooks().stream()
            .filter(record -> 
                record.getBookTitle().toLowerCase().contains(query) || 
                record.getUserName().toLowerCase().contains(query))
            .collect(Collectors.toList());
        
        overdueTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void btnHomeOnAction(ActionEvent event) throws IOException {
        Navigation.switchNavigation("LibrarianDashboard.fxml", event, 
            (LibrarianDashboardController controller) -> controller.setUser(currentUser));
    }

    @FXML
    private void btnLogOutAction(ActionEvent event) throws IOException {
        Navigation.switchNavigation("Login.fxml", event);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}