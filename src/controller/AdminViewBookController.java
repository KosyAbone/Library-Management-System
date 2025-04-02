package controller;

import Model.Book;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

public class AdminViewBookController {

    @FXML 
    private TableView<RowItem> tblBookDetails;
    
    @FXML 
    private TableColumn<RowItem, String> colField;
    
    @FXML 
    private TableColumn<RowItem, String> colValue;
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setBook(Book book) {
        ObservableList<RowItem> rows = FXCollections.observableArrayList(
            new RowItem("ISBN: ", book.getIsbn()),
            new RowItem("Title: ", book.getTitle()),
            new RowItem("Author: ", book.getAuthor()),
            new RowItem("Genre: ", book.getGenre())
        );

        colField.setCellValueFactory(new PropertyValueFactory<>("field"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        tblBookDetails.setItems(rows);
    }

    @FXML
    private void btnCloseOnAction(ActionEvent event) {
        Stage stage = (Stage) tblBookDetails.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void btnExitOnAction(ActionEvent event) {
        Stage stage = (Stage) tblBookDetails.getScene().getWindow();
        stage.close();
    }

    public static class RowItem {
        private final String field;
        private final String value;

        public RowItem(String field, String value) {
            this.field = field;
            this.value = value;
        }

        public String getField() {
            return field;
        }

        public String getValue() {
            return value;
        }
    }
}
