/**
 *
 * @author kossy
 */
package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Fine;
import util.DatabaseConnection;

public class FineDAO {
    private final Connection connection;
    
    public FineDAO() {
        connection = DatabaseConnection.getConnection();
    }

    // ========== CRUD OPERATIONS ==========
    public boolean addFine(Fine fine) {
        String sql = "INSERT INTO fines (borrow_id, user_id, amount, reason, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, fine.getBorrowId());
            ps.setInt(2, fine.getUserId());
            ps.setDouble(3, fine.getAmount());
            ps.setString(4, fine.getReason());
            ps.setString(5, fine.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    public boolean updateFine(Fine fine) {
        String sql = "UPDATE fines SET amount=?, reason=?, status=?, paid_date=? WHERE fine_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, fine.getAmount());
            ps.setString(2, fine.getReason());
            ps.setString(3, fine.getStatus());
            ps.setTimestamp(4, fine.getPaidDate());
            ps.setInt(5, fine.getFineId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    // ========== GETTER METHODS ==========
    public Fine getFineById(int fineId) {
        String sql = "SELECT f.*, u.username, b.title FROM fines f " +
                     "JOIN users u ON f.user_id = u.user_id " +
                     "JOIN borrow_records br ON f.borrow_id = br.borrow_id " +
                     "JOIN books b ON br.book_id = b.book_id " +
                     "WHERE f.fine_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, fineId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractFineFromResultSet(rs) : null;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return null;
        }
    }

    // ========== LIST METHODS ==========
    public List<Fine> getAllFines() {
        return getFines("SELECT f.*, u.username, b.title FROM fines f " +
                       "JOIN users u ON f.user_id = u.user_id " +
                       "JOIN borrow_records br ON f.borrow_id = br.borrow_id " +
                       "JOIN books b ON br.book_id = b.book_id");
    }

    public List<Fine> getFinesByUser(int userId) {
        return getFines("SELECT f.*, b.title FROM fines f " +
                       "JOIN borrow_records br ON f.borrow_id = br.borrow_id " +
                       "JOIN books b ON br.book_id = b.book_id " +
                       "WHERE f.user_id=?", userId);
    }

    public List<Fine> getUnpaidFinesByUser(int userId) {
        return getFines("SELECT f.*, b.title FROM fines f " +
                       "JOIN borrow_records br ON f.borrow_id = br.borrow_id " +
                       "JOIN books b ON br.book_id = b.book_id " +
                       "WHERE f.user_id=? AND f.status='PENDING'", userId);
    }

    public List<Fine> getFinesByStatus(String status) {
        return getFines("SELECT f.*, u.username, b.title FROM fines f " +
                       "JOIN users u ON f.user_id = u.user_id " +
                       "JOIN borrow_records br ON f.borrow_id = br.borrow_id " +
                       "JOIN books b ON br.book_id = b.book_id " +
                       "WHERE f.status=?", status);
    }

    // ========== STATISTICS METHODS ==========
    public double calculateTotalUnpaidFines(int userId) {
        String sql = "SELECT SUM(amount) FROM fines WHERE user_id=? AND status='PENDING'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return 0;
        }
    }

    public boolean hasUnpaidFines(int userId) {
        String sql = "SELECT COUNT(*) FROM fines WHERE user_id=? AND status='PENDING'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    public double getTotalFinesRevenue() {
        String sql = "SELECT SUM(amount) FROM fines WHERE status='PAID'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return 0;
        }
    }

    // ========== HELPER METHODS ==========
    private List<Fine> getFines(String sql, Object... params) {
        List<Fine> fines = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    fines.add(extractFineFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
        }
        return fines;
    }

    private Fine extractFineFromResultSet(ResultSet rs) throws SQLException {
        Fine fine = new Fine();
        fine.setFineId(rs.getInt("fine_id"));
        fine.setBorrowId(rs.getInt("borrow_id"));
        fine.setUserId(rs.getInt("user_id"));
        fine.setAmount(rs.getDouble("amount"));
        fine.setReason(rs.getString("reason"));
        fine.setStatus(rs.getString("status"));
        fine.setIssuedDate(rs.getTimestamp("issued_date"));
        fine.setPaidDate(rs.getTimestamp("paid_date"));
        try {
            fine.setUserName(rs.getString("username"));
            fine.setBookTitle(rs.getString("title"));
        } catch (SQLException e) {
            // These fields might not be in all queries
        }
        return fine;
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