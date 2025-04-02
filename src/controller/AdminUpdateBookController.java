package controller;

import DAO.BookDAO;
import Model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AdminUpdateBookController {

    @FXML private TextField txtName;         
    @FXML private TextField txtLanguage;     
    @FXML private TextField txtType;         
    @FXML private TextField txtName1;        

    @FXML private Label lblisbnAlert;
    @FXML private Label lblTitleAlert;
    @FXML private Label lblAuthorAlert;
    @FXML private Label lblGenreAlert1;

    private Book book;
    private Runnable onBookUpdated;


    public void setBook(Book book) {
        this.book = book;

        if (book != null) {
            txtName.setText(book.getIsbn());
            txtLanguage.setText(book.getTitle());
            txtType.setText(book.getAuthor());
            txtName1.setText(book.getGenre());
        }
    }

    public void setOnBookUpdated(Runnable onBookUpdated) {
        this.onBookUpdated = onBookUpdated;
    }


    @FXML
    private void btnUpdateOnAction() {
        clearAlerts();

        String isbn = txtName.getText().trim();
        String title = txtLanguage.getText().trim();
        String author = txtType.getText().trim();
        String genre = txtName1.getText().trim();

        boolean valid = true;

        if (isbn.isEmpty()) {
            lblisbnAlert.setText("ISBN is required.");
            valid = false;
        }
        if (title.isEmpty()) {
            lblTitleAlert.setText("Title is required.");
            valid = false;
        }
        if (author.isEmpty()) {
            lblAuthorAlert.setText("Author is required.");
            valid = false;
        }
        if (genre.isEmpty()) {
            lblGenreAlert1.setText("Genre is required.");
            valid = false;
        }

        if (!valid || book == null) return;

        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        BookDAO bookDAO = new BookDAO();
        boolean success = bookDAO.updateBook(book);

        if (success) {
            if (onBookUpdated != null) {
                onBookUpdated.run();
            }
            closeWindow();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update book.").showAndWait();
        }
    }

    @FXML
    private void btnCancelOnAction() {
        closeWindow();
    }

    @FXML
    private void btnCloseOnAction() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

    private void clearAlerts() {
        lblisbnAlert.setText("");
        lblTitleAlert.setText("");
        lblAuthorAlert.setText("");
        lblGenreAlert1.setText("");
    }
}
