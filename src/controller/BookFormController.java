package controller;

import DAO.BookDAO;
import Model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BookFormController {
    // Form Fields
    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField publisherField;
    @FXML private ComboBox<String> genreCombo;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Spinner<Integer> yearSpinner;
    @FXML private TextArea descriptionArea;
    @FXML private Label isbnError;
    @FXML private Label titleError;
    @FXML private Label authorError;
    @FXML private Label genreError;
    @FXML private Label publisherError;

    private Book currentBook;
    private Runnable onSaveCallback;
    private final BookDAO bookDAO = new BookDAO();

    @FXML
    public void initialize() {
        initializeGenreCombo();
        initializeSpinners();
    }

    private void initializeGenreCombo() {
        genreCombo.getItems().addAll(
            "Fiction", "Non-Fiction", "Science Fiction", "Fantasy",
            "Mystery", "Romance", "Biography", "History",
            "Science", "Technology", "Self-Help", "Other"
        );
    }

    private void initializeSpinners() {
        quantitySpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));
        yearSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1800, 2100, 2023));
    }

    public void setBook(Book book) {
        this.currentBook = book;
        if (book != null) {
            populateFormFields(book);
        }
    }

    private void populateFormFields(Book book) {
        isbnField.setText(book.getIsbn());
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        genreCombo.setValue(book.getGenre());
        quantitySpinner.getValueFactory().setValue(book.getQuantity());
        yearSpinner.getValueFactory().setValue(book.getPublicationYear());
        publisherField.setText(book.getPublisher());
        descriptionArea.setText(book.getDescription());
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSave() {
        if (validateForm()) {
            Book book = currentBook != null ? currentBook : new Book();
            updateBookFromFields(book);
            
            boolean success = saveBook(book);
            if (success && onSaveCallback != null) {
                onSaveCallback.run();
            }
        }
    }

    private void updateBookFromFields(Book book) {
        book.setIsbn(isbnField.getText().trim());
        book.setTitle(titleField.getText().trim());
        book.setAuthor(authorField.getText().trim());
        book.setGenre(genreCombo.getValue());
        book.setQuantity(quantitySpinner.getValue());
        book.setPublicationYear(yearSpinner.getValue());
        book.setPublisher(publisherField.getText().trim());
        book.setDescription(descriptionArea.getText().trim());
        
        if (currentBook == null) {
            book.setAvailableQuantity(quantitySpinner.getValue());
        }
    }

    private boolean saveBook(Book book) {
        try {
            closeWindow();
            return currentBook != null ? 
                   bookDAO.updateBook(book) : 
                   bookDAO.addBook(book);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to save book: " + e.getMessage());
            return false;
        }
    }

    private boolean validateForm() {
        clearErrorMessages();
        boolean isValid = true;

        isValid &= validateRequiredField(isbnField, isbnError, "ISBN is required");
        isValid &= validateRequiredField(titleField, titleError, "Title is required");
        isValid &= validateRequiredField(authorField, authorError, "Author is required");
        isValid &= validateComboBox(genreCombo, genreError, "Please select a genre");
        isValid &= validateRequiredField(publisherField, publisherError, "Publisher is required");

        return isValid;
    }

    private boolean validateRequiredField(TextField field, Label errorLabel, String errorMessage) {
        if (field.getText().trim().isEmpty()) {
            errorLabel.setText(errorMessage);
            return false;
        }
        return true;
    }

    private boolean validateComboBox(ComboBox<?> comboBox, Label errorLabel, String errorMessage) {
        if (comboBox.getValue() == null) {
            errorLabel.setText(errorMessage);
            return false;
        }
        return true;
    }

    private void clearErrorMessages() {
        isbnError.setText("");
        titleError.setText("");
        authorError.setText("");
        genreError.setText("");
        publisherError.setText("");
    }
    
    @FXML
    private void handleCancel() {
        closeWindow();
    }
    
    private void closeWindow() {
        isbnField.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}