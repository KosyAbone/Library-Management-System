package controller;

import DAO.BookDAO;
import Model.Book;
import Model.User;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class AdminDeleteBookController {

    private Book book;
    private Runnable onBookDeleted;

    @FXML
    private ImageView imgClose;
    
    @FXML
    private Text txtConfirmation;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setBook(Book book) {
    this.book = book;
    
    if (txtConfirmation != null && book != null) {
        txtConfirmation.setText("Are you sure you want to delete the book \"" + book.getTitle() + "\" (ISBN: " + book.getIsbn() + ")?");
    }
    }


    public void setOnBookDeleted(Runnable onBookDeleted) {
        this.onBookDeleted = onBookDeleted;
    }

    @FXML
    private void btnConfirmOnAction(ActionEvent event) {
        if (book != null) {
            boolean deleted = new BookDAO().deleteBook(book.getBookId());
            if (deleted) {
                if (onBookDeleted != null) {
                    onBookDeleted.run();
                }
            }
        }
        closeWindow();
    }

    @FXML
    private void btnCloseOnAction(ActionEvent event) {
        closeWindow();
    }
    
    private void closeWindow() {
        Stage stage = (Stage) imgClose.getScene().getWindow();
        stage.close();
    }
}

