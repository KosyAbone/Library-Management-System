package controller;

import DAO.BorrowRecordDAO;
import DAO.UserDAO;
import Model.BorrowRecord;
import Model.User;
import Model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LibrarianIssueBookPopupController {

    @FXML private Label lblBookTitle;
    @FXML private Label lblIsbn;
    @FXML private Label lblAvailable;
    @FXML private TextField txtMemberId;
    @FXML private Label lblError;
    
    private Book currentBook;
    private User user;
    private Runnable onSuccess;
    private final UserDAO userDAO = new UserDAO();

    public void setBook(Book book) {
        this.currentBook = book;
        updateUI();
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    private void updateUI() {
        lblBookTitle.setText(currentBook.getTitle());
        lblIsbn.setText(currentBook.getIsbn());
        lblAvailable.setText(String.valueOf(currentBook.getAvailableQuantity()));
    }

    @FXML
    private void handleIssueBook() {
        String memberIdStr = txtMemberId.getText().trim();
        
        if (memberIdStr.isEmpty()) {
            lblError.setText("Member ID is required");
            return;
        }

        try {
            int memberId = Integer.parseInt(memberIdStr);
            User member = userDAO.getUserById(memberId);
            
            if (member == null || !member.isMember()) {
                lblError.setText("Invalid member ID");
                return;
            }

            // Create and configure the borrow record
            BorrowRecord record = new BorrowRecord();
            record.setBookId(currentBook.getBookId());
            record.setUserId(memberId);

            BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
            String result = borrowRecordDAO.issueBook(record);
            
            if (result.startsWith("Success")) {
                if (onSuccess != null) onSuccess.run();
                closeWindow();
            } else {
                String errorMessage = result.replace("Error: ", "");
                lblError.setText(errorMessage);
            }
        } catch (NumberFormatException e) {
            lblError.setText("Member ID must be a number");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        lblError.getScene().getWindow().hide();
    }
}