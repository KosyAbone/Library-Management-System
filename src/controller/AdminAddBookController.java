package controller;

import DAO.BookDAO;
import Model.Book;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AdminAddBookController {

    @FXML private TextField txtName;  
    @FXML private TextField txtLanguage;  
    @FXML private TextField txtType; 
    @FXML private TextField txtName1;     

    @FXML private Label lblisbnAlert;
    @FXML private Label lblTitleAlert;
    @FXML private Label lblAuthorAlert;
    @FXML private Label lblGenreAlert1;

    private Runnable onBookAdded;
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setOnBookAdded(Runnable onBookAdded) {
        this.onBookAdded = onBookAdded;
    }

    @FXML
    private void btnAddOnAction() {
        clearAlerts();

        String isbn = txtName.getText().trim();
        String title = txtLanguage.getText().trim();
        String author = txtType.getText().trim();
        String genre = txtName1.getText().trim();

        boolean valid = true;

        if (isbn.isEmpty()) {
            lblisbnAlert.setText("ISBN is required");
            valid = false;
        }

        if (title.isEmpty()) {
            lblTitleAlert.setText("Title is required");
            valid = false;
        }

        if (author.isEmpty()) {
            lblAuthorAlert.setText("Author is required");
            valid = false;
        }

        if (genre.isEmpty()) {
            lblGenreAlert1.setText("Genre is required");
            valid = false;
        }

        if (!valid) return;

        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setQuantity(1);
        book.setAvailableQuantity(1);

        boolean success = new BookDAO().addBook(book);

        if (success) {
            if (onBookAdded != null) {
                onBookAdded.run();
            }
            closeStage();
        } else {
            lblisbnAlert.setText("Failed to add book (maybe ISBN exists)");
        }
    }

    @FXML
    private void btnCancelOnAction() {
        closeStage();
    }

    @FXML
    private void btnCloseOnAction() {
        closeStage();
    }

    private void closeStage() {
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
