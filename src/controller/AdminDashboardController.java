package controller;

import DAO.BookDAO;
import DAO.BorrowRecordDAO;
import DAO.UserDAO;
import Model.BorrowRecord;
import Model.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;




import java.util.List;
import javafx.event.ActionEvent;
import util.Navigation;

public class AdminDashboardController {
    
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML 
    private PieChart pieChart;
    
    @FXML 
    private Label lblTotalUserCount;
    
    @FXML 
    private Label lblTotalBookCount;
    
    @FXML 
    private Label lblTotalLibrarianCount;

    @FXML 
    private TableView<BorrowRecord> overdueTable;
    
    @FXML 
    private TableView<User> librarianTable;
    
    @FXML 
    private TableColumn<BorrowRecord, String> colUserName;
    
    @FXML 
    private TableColumn<BorrowRecord, String> colBookTitle;
    
    @FXML 
    private TableColumn<User, String> colLibrarianName;


    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();
    private final BorrowRecordDAO borrowDAO = new BorrowRecordDAO();

    @FXML
    public void initialize() {
        loadDashboardData();
        setupOverdueTable();
        loadLibrarians();
    }

    private void loadDashboardData() {
        int totalUsers = userDAO.getUserCount();
        int totalBooks = bookDAO.getBookCount();
        int totalLibrarians = userDAO.getLibrarianCount();
        int totalBorrowed = borrowDAO.getAllBorrowRecords().size();
        int totalReturned = borrowDAO.getAllBorrowRecords().stream()
                .filter(record -> record.getReturnDate() != null)
                .toList().size();

        lblTotalUserCount.setText(String.format("%04d", totalUsers));
        lblTotalBookCount.setText(String.format("%04d", totalBooks));
        lblTotalLibrarianCount.setText(String.format("%04d", totalLibrarians));

        pieChart.getData().clear();
        pieChart.getData().add(new PieChart.Data("Borrowed", totalBorrowed));
        pieChart.getData().add(new PieChart.Data("Returned", totalReturned));
    }
    
    public void loadLibrarians() {
    List<User> librarians = userDAO.getAllLibrarians();
    ObservableList<User> data = FXCollections.observableArrayList(librarians);

    colLibrarianName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName()));

    librarianTable.setItems(data);
}


    private void setupOverdueTable() {
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        List<BorrowRecord> overdueList = borrowDAO.getOverdueBooks();
        overdueTable.getItems().setAll(overdueList);
    }
    
    @FXML
    private void btnUserOnAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("AdminUserManagement.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnBookOnAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("AdminBookManagement.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnLibrarianOnAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("AdminDashboard.fxml", event);
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
