package DAO;

import Model.Book;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Model.BorrowRecord;
import util.DatabaseConnection;

public class BorrowRecordDAO {
    private final Connection connection;
    private final BookDAO bookDAO;
    private static final int STANDARD_LOAN_DAYS = 14;
    
    private interface TransactionOperation {
        String execute() throws SQLException;
    }
    
    public BorrowRecordDAO() {
        connection = DatabaseConnection.getConnection();
        bookDAO = new BookDAO();
    }

    // ========== CRUD OPERATIONS ==========
    public String borrowBook(int bookId, int userId) {
        return executeTransaction(() -> {
            String validationError = validateBorrowPreconditions(bookId, userId);
            if (validationError != null) return validationError;

            Date borrowDate = getCurrentDate();
            Date dueDate = getStandardDueDate();

            if (!createBorrowRecord(bookId, userId, borrowDate, dueDate, BorrowRecord.BORROWED)) {
                return "Error: Failed to create borrow record";
            }

            String updateError = executeBookUpdate(bookId, -1);
            if (updateError != null) return updateError;

            Book book = bookDAO.getBookById(bookId);
            return "Success: Book '" + book.getTitle() + "' borrowed until " + dueDate;
        });
    }

    public String issueBook(BorrowRecord record) {
        return executeTransaction(() -> {
            if (record.getBookId() <= 0 || record.getUserId() <= 0) {
                return "Error: Invalid book or user ID";
            }

            Date borrowDate = record.getBorrowDate() != null ? 
                record.getBorrowDate() : getCurrentDate();
            Date dueDate = record.getDueDate() != null ? 
                record.getDueDate() : getStandardDueDate();

            if (dueDate.before(borrowDate)) {
                return "Error: Due date cannot be before borrow date";
            }

            Book book = bookDAO.getBookById(record.getBookId());
            if (book == null) return "Error: Book not found";

            String status = record.getStatus() != null ? record.getStatus() : BorrowRecord.BORROWED;
            if (!createBorrowRecord(record.getBookId(), record.getUserId(), borrowDate, dueDate, status)) {
                return "Error: Failed to create borrow record";
            }

            if (isBorrowedStatus(status)) {
                String updateError = executeBookUpdate(record.getBookId(), -1);
                if (updateError != null) return updateError;
            }

            return "Success: Book '" + book.getTitle() + "' issued until " + dueDate;
        });
    }

    public String returnBook(int borrowId) {
        return executeTransaction(() -> {
            BorrowRecord record = getBorrowRecordById(borrowId);
            if (record == null) return "Error: Borrow record not found";
            if (isReturnedStatus(record.getStatus())) 
                return "Error: Book already returned";

            Book book = bookDAO.getBookById(record.getBookId());
            if (book == null) return "Error: Associated book not found";

            if (!updateBorrowRecordAsReturned(borrowId)) {
                return "Error: Failed to update borrow record";
            }

            if (isBorrowedStatus(record.getStatus())) {
                String updateError = executeBookUpdate(record.getBookId(), 1);
                if (updateError != null) return updateError;
            }

            String overdueMessage = record.getDueDate().before(getCurrentDate()) ? 
                " (This book was overdue)" : "";
            return "Success: Book '" + book.getTitle() + "' returned" + overdueMessage;
        });
    }
    
    
    // ========== GETTER METHODS ==========
    public BorrowRecord getBorrowRecordById(int borrowId) {
        String sql = "SELECT br.*, b.title, u.username FROM borrow_records br " +
                     "JOIN books b ON br.book_id = b.book_id " +
                     "JOIN users u ON br.user_id = u.user_id " +
                     "WHERE br.borrow_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, borrowId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractBorrowRecordFromResultSet(rs) : null;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return null;
        }
    }
    
    public BorrowRecord getActiveBorrow(int bookId, int userId) {
        String sql = "SELECT br.*, b.title, u.username FROM borrow_records br " +
                    "JOIN books b ON br.book_id = b.book_id " +
                    "JOIN users u ON br.user_id = u.user_id " +
                    "WHERE br.book_id=? AND br.user_id=? AND br.status=? AND br.return_date IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, userId);
            ps.setString(3, BorrowRecord.BORROWED);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractBorrowRecordFromResultSet(rs) : null;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return null;
        }
    }


    // ========== LIST METHODS ==========
    public List<BorrowRecord> getAllBorrowRecords() {
        return getBorrowRecords("SELECT br.*, b.title, u.username FROM borrow_records br " +
                              "JOIN books b ON br.book_id = b.book_id " +
                              "JOIN users u ON br.user_id = u.user_id " +
                              "ORDER BY br.borrow_date DESC");
    }

    public List<BorrowRecord> getBorrowRecordsByUser(int userId) {
        return getBorrowRecords("SELECT br.*, b.title FROM borrow_records br " +
                              "JOIN books b ON br.book_id = b.book_id " +
                              "WHERE br.user_id=? ORDER BY br.borrow_date DESC", userId);
    }

    public List<BorrowRecord> getCurrentBorrowsByUser(int userId) {
        return getBorrowRecords("SELECT br.*, b.title FROM borrow_records br " +
                              "JOIN books b ON br.book_id = b.book_id " +
                              "WHERE br.user_id=? AND br.status=? " +
                              "ORDER BY br.due_date", userId, BorrowRecord.BORROWED);
    }

    public List<BorrowRecord> getOverdueBooks() {
        return getBorrowRecords("SELECT br.*, b.title, u.username FROM borrow_records br " +
                              "JOIN books b ON br.book_id = b.book_id " +
                              "JOIN users u ON br.user_id = u.user_id " +
                              "WHERE br.due_date < ? AND br.status='BORROWED' " +
                              "ORDER BY br.due_date", Date.valueOf(LocalDate.now()));
    }

    public List<BorrowRecord> getBorrowHistoryForBook(int bookId) {
        return getBorrowRecords("SELECT br.*, u.username FROM borrow_records br " +
                              "JOIN users u ON br.user_id = u.user_id " +
                              "WHERE br.book_id=? ORDER BY br.borrow_date DESC", bookId);
    }

    public List<BorrowRecord> getRecentTransactions(int limit) {
        return getBorrowRecords("SELECT br.*, b.title, u.username FROM borrow_records br " +
                              "JOIN books b ON br.book_id = b.book_id " +
                              "JOIN users u ON br.user_id = u.user_id " +
                              "ORDER BY br.borrow_date DESC LIMIT ?", limit);
    }

    public List<BorrowRecord> getTodaysCheckouts() {
        return getBorrowRecords("SELECT br.*, b.title, u.username FROM borrow_records br " +
                              "JOIN books b ON br.book_id = b.book_id " +
                              "JOIN users u ON br.user_id = u.user_id " +
                              "WHERE DATE(br.borrow_date) = CURRENT_DATE " +
                              "ORDER BY br.borrow_date DESC");
    }

    public List<BorrowRecord> getUserActiveLoans(int userId) {
        return getBorrowRecords("SELECT br.*, b.title, u.username FROM borrow_records br " +
                              "JOIN books b ON br.book_id = b.book_id " +
                              "JOIN users u ON br.user_id = u.user_id " +
                              "WHERE br.user_id = ? AND br.return_date IS NULL " +
                              "ORDER BY br.due_date", userId);
    }

    // ========== STATUS CHECKS ==========
    public boolean hasOverdueBooks(int userId) {
        String sql = "SELECT COUNT(*) FROM borrow_records " +
                    "WHERE user_id=? AND due_date < CURRENT_DATE AND status=?";
        return getCount(sql, userId, BorrowRecord.BORROWED) > 0;
    }

    public boolean hasActiveBorrow(int bookId, int userId) {
        String sql = "SELECT COUNT(*) FROM borrow_records " +
                    "WHERE book_id=? AND user_id=? AND status=?";
        return getCount(sql, bookId, userId, BorrowRecord.BORROWED) > 0;
    }
    
    private boolean isBorrowedStatus(String status) {
        return status != null && status.equals(BorrowRecord.BORROWED);
    }

    private boolean isReturnedStatus(String status) {
        return status != null && status.equals(BorrowRecord.RETURNED);
    }

     
    // ========== COUNT METHODS ==========
    public int getOverdueBooksCount() {
        return getCount("SELECT COUNT(*) FROM borrow_records " +
                      "WHERE due_date < ? AND status='BORROWED'", 
                      Date.valueOf(LocalDate.now()));
    }

    public int getBorrowedBooksCount(int userId) {
        return getCount("SELECT COUNT(*) FROM borrow_records " +
                       "WHERE user_id=? AND status='BORROWED'", userId);
    }

    public int getPendingReturnsCount() {
        return getCount("SELECT COUNT(*) FROM borrow_records WHERE return_date IS NULL");
    }

    public int getBooksDueSoonCount(int userId) {
        return getCount("SELECT COUNT(*) FROM borrow_records " +
                       "WHERE user_id=? AND due_date BETWEEN ? AND ?", 
                       userId, Date.valueOf(LocalDate.now()), 
                       Date.valueOf(LocalDate.now().plusDays(3)));
    }

    // ========== HELPER METHODS ==========
    private Date getCurrentDate() {
        return Date.valueOf(LocalDate.now());
    }

    private Date getStandardDueDate() {
        return Date.valueOf(LocalDate.now().plusDays(STANDARD_LOAN_DAYS));
    }

    private String validateBorrowPreconditions(int bookId, int userId) throws SQLException {
        Book book = bookDAO.getBookById(bookId);
        if (book == null) return "Error: Book not found";
        if (book.getAvailableQuantity() <= 0) 
            return "Error: Book '" + book.getTitle() + "' is not available";
        if (hasActiveBorrow(bookId, userId)) 
            return "Error: You already have this book checked out";
        if (hasOverdueBooks(userId)) 
            return "Error: You have overdue books. Please return them first";
        return null;
    }
    
    private String executeBookUpdate(int bookId, int quantityChange) throws SQLException {
        if (!bookDAO.updateAvailableQuantity(bookId, quantityChange)) {
            return "Error: Failed to update book quantity";
        }
        return null;
    }
    
    // ========== EXTRACTED HELPER METHODS ==========
    private boolean createBorrowRecord(int bookId, int userId, Date borrowDate, Date dueDate, String status) {
        String sql = "INSERT INTO borrow_records (book_id, user_id, borrow_date, due_date, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, userId);
            ps.setDate(3, borrowDate);
            ps.setDate(4, dueDate);
            ps.setString(5, status);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    private boolean updateBorrowRecordAsReturned(int borrowId) {
        String sql = "UPDATE borrow_records SET return_date=?, status=? WHERE borrow_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, getCurrentDate());
            ps.setString(2, BorrowRecord.RETURNED);
            ps.setInt(3, borrowId);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }
    
    private List<BorrowRecord> getBorrowRecords(String sql, Object... params) {
        List<BorrowRecord> records = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(extractBorrowRecordFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
        }
        return records;
    }

    private int getCount(String sql, Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return 0;
        }
    }

    private BorrowRecord extractBorrowRecordFromResultSet(ResultSet rs) throws SQLException {
        BorrowRecord record = new BorrowRecord();
        record.setBorrowId(rs.getInt("borrow_id"));
        record.setBookId(rs.getInt("book_id"));
        record.setUserId(rs.getInt("user_id"));
        record.setBorrowDate(rs.getDate("borrow_date"));
        record.setDueDate(rs.getDate("due_date"));
        record.setReturnDate(rs.getDate("return_date"));
        record.setStatus(rs.getString("status"));
        
        try {
            record.setBookTitle(rs.getString("title"));
            record.setUserName(rs.getString("username"));
        } catch (SQLException e) {
            
        }
        
        return record;
    }

    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    private void handleSQLException(SQLException ex) {
        System.err.println("SQL Error: " + ex.getMessage());
        ex.printStackTrace();
    }
    
    // ========== TRANSACTION HELPERS ==========
    private String executeTransaction(TransactionOperation operation) {
        try {
            connection.setAutoCommit(false);
            String result = operation.execute();
            connection.commit();
            return result;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                return "Error: Failed to rollback transaction - " + e.getMessage();
            }
            return "Error: " + ex.getMessage();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                return "Error: Failed to reset auto-commit - " + e.getMessage();
            }
        }
    }
}