package DAO;

/**
 *
 * @author kossy
 */

import java.sql.*;
import java.util.*;
import Model.Book;
import util.DatabaseConnection;

public class BookDAO {
    private Connection connection;
    
    public BookDAO() {
        connection = DatabaseConnection.getConnection();
    }

    // ========== CRUD OPERATIONS ==========
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (isbn, title, author, genre, quantity, " +
                    "available_quantity, publication_year, publisher, description, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            book.calculateStatus(); // Ensure status is calculated before saving
            setBookParameters(ps, book);
            ps.setString(10, book.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET isbn=?, title=?, author=?, genre=?, quantity=?, " +
                    "available_quantity=?, publication_year=?, publisher=?, description=?, status=? " +
                    "WHERE book_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            book.calculateStatus(); // To Ensure status is calculated before updating
            setBookParameters(ps, book);
            ps.setString(10, book.getStatus());
            ps.setInt(11, book.getBookId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    // ========== GETTER METHODS ==========
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractBookFromResultSet(rs) : null;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return null;
        }
    }

    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractBookFromResultSet(rs) : null;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return null;
        }
    }

    // ========== LIST METHODS ==========
    public List<Book> getAllBooks() {
        return getBooks("SELECT * FROM books ORDER BY title");
    }

    public List<Book> getAvailableBooks() {
        return getBooks("SELECT * FROM books WHERE available_quantity > 0 ORDER BY title");
    }

    public List<Book> getBooksByAuthor(String author) {
        return getBooks("SELECT * FROM books WHERE author LIKE ? ORDER BY title", "%" + author + "%");
    }

    public List<Book> getBooksByGenre(String genre) {
        return getBooks("SELECT * FROM books WHERE genre LIKE ? ORDER BY title", "%" + genre + "%");
    }

    public List<Book> getBooksByPublisher(String publisher) {
        return getBooks("SELECT * FROM books WHERE publisher LIKE ? ORDER BY title", "%" + publisher + "%");
    }

    // ========== SEARCH METHODS ==========
    public List<Book> searchBooks(String searchTerm) {
        String likeTerm = "%" + searchTerm + "%";
        return getBooks("SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR genre LIKE ? " +
                       "OR publisher LIKE ? OR description LIKE ? ORDER BY title", 
                       likeTerm, likeTerm, likeTerm, likeTerm, likeTerm);
    }

    public List<Book> searchAvailableBooks(String searchTerm) {
        String likeTerm = "%" + searchTerm + "%";
        return getBooks("SELECT * FROM books WHERE available_quantity > 0 AND " +
                      "(title LIKE ? OR author LIKE ? OR genre LIKE ? OR publisher LIKE ?) " +
                      "ORDER BY title", likeTerm, likeTerm, likeTerm, likeTerm);
    }

    // ========== INVENTORY MANAGEMENT ==========
    public boolean updateAvailableQuantity(int bookId, int change) {
        // First get current quantity
        Book book = getBookById(bookId);
        if (book == null) return false;

        int newQuantity = book.getAvailableQuantity() + change;

        String sql = "UPDATE books SET available_quantity = ?, status = ? WHERE book_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setString(2, newQuantity <= 0 ? "UNAVAILABLE" : 
                           newQuantity <= 2 ? "LOW_IN_STOCK" : "AVAILABLE");
            ps.setInt(3, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    public boolean isBookAvailable(int bookId) {
        String sql = "SELECT available_quantity FROM books WHERE book_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("available_quantity") > 0;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return false;
        }
    }

    // ========== STATISTICS ==========
    public int getBookCount() {
        return getCount("SELECT COUNT(*) FROM books");
    }

    public int getAvailableBookCount() {
        return getCount("SELECT COUNT(*) FROM books WHERE available_quantity > 0");
    }

    public int getBookCountByGenre(String genre) {
        return getCount("SELECT COUNT(*) FROM books WHERE genre=?", genre);
    }

    // ========== HELPER METHODS ==========
    private List<Book> getBooks(String sql, Object... params) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(extractBookFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
        }
        return books;
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
    
    public List<Book> getCheckedOutBooks() {
        // Get books where at least one copy is checked out (available_quantity < total quantity)
        return getBooks("SELECT * FROM books WHERE available_quantity < quantity ORDER BY title");
    }
    
    public int getCheckedOutBookCount() {
        // Sum of all checked out copies across all books
        String sql = "SELECT SUM(quantity - available_quantity) FROM books";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
            return 0;
        }
    }

    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setGenre(rs.getString("genre"));
        book.setQuantity(rs.getInt("quantity"));
        book.setAvailableQuantity(rs.getInt("available_quantity"));
        book.setPublicationYear(rs.getInt("publication_year"));
        book.setPublisher(rs.getString("publisher"));
        book.setDescription(rs.getString("description"));
        return book;
    }

    private void setBookParameters(PreparedStatement ps, Book book) throws SQLException {
        ps.setString(1, book.getIsbn());
        ps.setString(2, book.getTitle());
        ps.setString(3, book.getAuthor());
        ps.setString(4, book.getGenre());
        ps.setInt(5, book.getQuantity());
        ps.setInt(6, book.getAvailableQuantity());
        ps.setInt(7, book.getPublicationYear());
        ps.setString(8, book.getPublisher());
        ps.setString(9, book.getDescription());
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