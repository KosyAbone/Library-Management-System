package controller;

import DAO.BookDAO;
import Model.Book;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import util.Navigation;

import java.io.IOException;
import java.util.List;

public class LibrarianBookManagementController implements UserAwareController {

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colTotalQty;
    @FXML private TableColumn<Book, Integer> colAvailableQty;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TableColumn<Book, Void> colAction;
    @FXML private TextField txtSearch;
    
    private final BookDAO bookDAO = new BookDAO();
    private User currentUser;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @Override
    public void setUser(User user) {
        this.currentUser = user;
        initialize();
        loadBooks();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
    }

    private void setupTableColumns() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colTotalQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colAvailableQty.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Book, Void> call(TableColumn<Book, Void> param) {
                return new TableCell<>() {
                    private final Button actionButton = new Button();
                    
                    {
                        actionButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            handleBookAction(book);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Book book = getTableView().getItems().get(getIndex());
                            if (book.getAvailableQuantity() > 0) {
                                actionButton.setText("Issue");
                                actionButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                            } else {
                                actionButton.setText("Return");
                                actionButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                            }
                            setGraphic(actionButton);
                        }
                    }
                };
            }
        });
    }

    private void loadBooks() {
        List<Book> books = bookDAO.getAllBooks();
        bookList.setAll(books);
        booksTable.setItems(bookList);
    }

    @FXML
    private void handleSearch() {
        String query = txtSearch.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            booksTable.setItems(bookList);
            return;
        }

        ObservableList<Book> filteredList = bookList.filtered(book ->
            book.getTitle().toLowerCase().contains(query) ||
            book.getAuthor().toLowerCase().contains(query) ||
            book.getIsbn().toLowerCase().contains(query)
        );
        booksTable.setItems(filteredList);
    }

    private void handleBookAction(Book book) {
        try {
            if (book.getAvailableQuantity() > 0) {
                Navigation.openPopup("LibrarianIssueBookPopup.fxml", controller -> {
                    LibrarianIssueBookPopupController popupController = (LibrarianIssueBookPopupController) controller;
                    popupController.setBook(book);
                    popupController.setCurrentUser(currentUser);
                    popupController.setOnSuccess(this::loadBooks);
                });
            } else {
                // Return book implementation would go here
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to open dialog: " + e.getMessage());
        }
    }

    @FXML
    private void btnHomeOnAction(ActionEvent event) throws IOException {
        Navigation.switchNavigation("LibrarianDashboard.fxml", event, 
            controller -> ((UserAwareController)controller).setUser(currentUser));
    }

    @FXML
    private void btnLogOutAction(ActionEvent event) {
        try {
            Navigation.switchNavigation("Login.fxml", event);
        } catch (IOException e) {
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}