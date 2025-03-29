package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Model.BorrowRecord;
import util.DatabaseConnection;

public class BorrowRecordDAO {
    private final Connection connection;
    
    public BorrowRecordDAO() {
        connection = DatabaseConnection.getConnection();
    }

    // ========== CRUD OPERATIONS ==========
    public boolean addBorrowRecord(BorrowRecord record) {
        String sql = "INSERT INTO borrow_records (book_id, user_id, borrow_date, due_date, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, record.getBookId());
            ps.setInt(2, record.getUserId());
            ps.setDate(3, record.getBorrowDate());
            ps.setDate(4, record.getDueDate());
            ps.setString(5, record.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    public boolean updateBorrowRecord(BorrowRecord record) {
        String sql = "UPDATE borrow_records SET return_date=?, status=? WHERE borrow_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, record.getReturnDate());
            ps.setString(2, record.getStatus());
            ps.setInt(3, record.getBorrowId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
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
                              "WHERE br.user_id=? AND br.status='BORROWED' " +
                              "ORDER BY br.due_date", userId);
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
                     "WHERE user_id=? AND due_date < ? AND status='BORROWED'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    public boolean createBorrowRequest(int userId, int bookId) {
        String sql = "INSERT INTO borrow_records (book_id, user_id, borrow_date, due_date, status) " +
                     "VALUES (?, ?, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY), 'PENDING')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
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
        
        // These fields might not be present in all queries
        try {
            record.setBookTitle(rs.getString("title"));
            record.setUserName(rs.getString("username"));
        } catch (SQLException e) {
            // Ignore if these columns don't exist in the result set
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
}