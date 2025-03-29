
package DAO;

import Model.User;
import java.sql.*;
import java.util.*;
import Model.User;
import util.DatabaseConnection;

public class UserDAO {
    private Connection connection;
    
    public UserDAO() {
        connection = DatabaseConnection.getConnection();
    }

    // ========== CRUD OPERATIONS ==========
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name, email, phone, " +
                     "user_type, member_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setUserParameters(ps, user);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username=?, password=?, first_name=?, last_name=?, email=?, " +
                     "phone=?, user_type=?, member_type=? WHERE user_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setUserParameters(ps, user);
            ps.setInt(9, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    // ========== GETTER METHODS ==========
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractUserFromResultSet(rs) : null;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return null;
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractUserFromResultSet(rs) : null;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return null;
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractUserFromResultSet(rs) : null;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return null;
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractUserFromResultSet(rs) : null;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return null;
        }
    }

    // ========== LIST METHODS ==========
    public List<User> getAllUsers() {
        return getUsers("SELECT * FROM users ORDER BY last_name, first_name");
    }

    public List<User> getAllMembers() {
        return getUsers("SELECT * FROM users WHERE user_type='MEMBER' ORDER BY last_name, first_name");
    }

    public List<User> getAllStudents() {
        return getUsers("SELECT * FROM users WHERE user_type='MEMBER' AND member_type='STUDENT' " +
                       "ORDER BY last_name, first_name");
    }

    public List<User> getAllFaculty() {
        return getUsers("SELECT * FROM users WHERE user_type='MEMBER' AND member_type='FACULTY' " +
                       "ORDER BY last_name, first_name");
    }

    public List<User> getAllLibrarians() {
        return getUsers("SELECT * FROM users WHERE user_type='LIBRARIAN' ORDER BY last_name, first_name");
    }

    public List<User> getAllAdmins() {
        return getUsers("SELECT * FROM users WHERE user_type='ADMIN' ORDER BY last_name, first_name");
    }

    public List<User> searchUsers(String searchTerm) {
        String likeTerm = "%" + searchTerm + "%";
        return getUsers("SELECT * FROM users WHERE username LIKE ? OR first_name LIKE ? OR " +
                       "last_name LIKE ? OR email LIKE ? ORDER BY last_name, first_name",
                       likeTerm, likeTerm, likeTerm, likeTerm);
    }

    
    // ========== STATISTICS ==========
    public int getUserCount() {
        return getCount("SELECT COUNT(*) FROM users");
    }

    public int getMemberCount() {
        return getCount("SELECT COUNT(*) FROM users WHERE user_type='MEMBER'");
    }

    public int getStudentCount() {
        return getCount("SELECT COUNT(*) FROM users WHERE user_type='MEMBER' AND member_type='STUDENT'");
    }

    public int getFacultyCount() {
        return getCount("SELECT COUNT(*) FROM users WHERE user_type='MEMBER' AND member_type='FACULTY'");
    }

    public int getLibrarianCount() {
        return getCount("SELECT COUNT(*) FROM users WHERE user_type='LIBRARIAN'");
    }

    public int getAdminCount() {
        return getCount("SELECT COUNT(*) FROM users WHERE user_type='ADMIN'");
    }

    // ========== PERMISSION METHODS ==========
    public boolean canCreateUserType(String currentUserType, String newUserType) {
        if ("ADMIN".equals(currentUserType)) {
            return true;
        }
        return "LIBRARIAN".equals(currentUserType) && "MEMBER".equals(newUserType);
    }

    // ========== HELPER METHODS ==========
    private List<User> getUsers(String sql, Object... params) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUserFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
        }
        return users;
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

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setUserType(rs.getString("user_type"));
        user.setMemberType(rs.getString("member_type"));
        return user;
    }

    private void setUserParameters(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getFirstName());
        ps.setString(4, user.getLastName());
        ps.setString(5, user.getEmail());
        ps.setString(6, user.getPhone());
        ps.setString(7, user.getUserType());
        ps.setString(8, user.getMemberType());
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