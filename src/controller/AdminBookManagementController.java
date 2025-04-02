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
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Cursor;
import util.Navigation;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class AdminBookManagementController {

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> colIsbn;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, String> colGenre;
    @FXML
    private TableColumn<Book, String> colStatus;
    @FXML
    private TableColumn<Book, Void> colActions;


    @FXML
    private TextField txtSearch;
    @FXML
    private Label lblSearchAlert;

    private final BookDAO bookDAO = new BookDAO();
    private ObservableList<Book> allBooks;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionColumn();
        loadBooks();
    }

    private void setupTableColumns() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colActions.setCellValueFactory(new PropertyValueFactory<>("actions"));
        
    }
    
    private void setupActionColumn() {
        colActions.setCellFactory(col -> new TableCell<>() {
            private final HBox hBox = new HBox(10);
            private final ImageView viewIcon = new ImageView(new Image("/assets/viewIconBlack.png"));
            private final ImageView editIcon = new ImageView(new Image("/assets/editIconBlack.png"));
            private final ImageView deleteIcon = new ImageView(new Image("/assets/deleteIconBlack.png"));
            
            {
            Stream.of(viewIcon, editIcon, deleteIcon).forEach(icon -> {
                icon.setFitWidth(20);
                icon.setFitHeight(20);
                icon.setCursor(Cursor.HAND);
            });
            viewIcon.setOnMouseClicked(e -> openViewPopup(getTableView().getItems().get(getIndex())));
            editIcon.setOnMouseClicked(e -> openUpdatePopup(getTableView().getItems().get(getIndex())));
            deleteIcon.setOnMouseClicked(e -> openDeletePopup(getTableView().getItems().get(getIndex())));

            hBox.getChildren().addAll(viewIcon, editIcon, deleteIcon);
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(hBox);
            }
        }
    });
}


    private void loadBooks() {
        List<Book> books = bookDAO.getAllBooks();
        books.forEach(Book::calculateStatus);
        allBooks = FXCollections.observableArrayList(books);
        bookTable.setItems(allBooks);
    }

    @FXML
    private void txtSearchOnAction() {
        String keyword = txtSearch.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            lblSearchAlert.setText("Search field cannot be empty.");
            bookTable.setItems(allBooks);
            return;
        }

        lblSearchAlert.setText("");

        List<Book> filtered = allBooks.stream()
                .filter(book ->
                        book.getIsbn().toLowerCase().contains(keyword) ||
                        book.getTitle().toLowerCase().contains(keyword) ||
                        book.getAuthor().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        bookTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void btnAddBookOnAction(ActionEvent event) {
        try {
            Navigation.openPopup("AdminAddBook.fxml", (AdminAddBookController controller) -> {
                controller.setUser(currentUser);
                controller.setOnBookAdded(this::loadBooks);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void openViewPopup(Book book) {
        try {
            Navigation.openPopup("AdminViewBook.fxml", (AdminViewBookController controller) -> {
                controller.setBook(book);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUpdatePopup(Book book) {
        try {
            Navigation.openPopup("AdminUpdateBook.fxml", (AdminUpdateBookController controller) -> {
                controller.setBook(book);
                controller.setOnBookUpdated(this::loadBooks);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDeletePopup(Book book) {
        try {
            Navigation.openPopup("AdminDeleteBook.fxml", (AdminDeleteBookController controller) -> {
                controller.setBook(book);
                controller.setOnBookDeleted(this::loadBooks);
            });
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
    
    @FXML
    private void btnHomeOnAction(ActionEvent event) {
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
