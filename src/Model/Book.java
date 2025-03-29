package Model;

import DAO.BookDAO;

/**
 *
 * @author kossy
 */

public class Book {
    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private int quantity;
    private int availableQuantity;
    private int publicationYear;
    private String publisher; 
    private String description;
    private String status;

    // Constructors
    public Book() {}
    
    public Book(int bookId, String isbn, String title, String author, String genre, 
                int quantity, int availableQuantity, int publicationYear, String publisher, String description) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.description = description;
    }

    // Getters and Setters
    public int getBookId() { 
        return bookId; 
    }
    public void setBookId(int bookId) { 
        this.bookId = bookId; 
    }
    public String getIsbn() { 
        return isbn; 
    }
    public void setIsbn(String isbn) { 
        this.isbn = isbn; 
    }
    public String getTitle() { 
        return title; 
    }
    public void setTitle(String title) { 
        this.title = title; 
    }
    public String getAuthor() { 
        return author; 
    }
    public void setAuthor(String author) { 
        this.author = author; 
    }
    public String getGenre() { 
        return genre; 
    }
    public void setGenre(String genre) { 
        this.genre = genre; 
    }
    public int getQuantity() { 
        return quantity; 
    }
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }
    public int getAvailableQuantity() { 
        return availableQuantity; 
    }
    public void setAvailableQuantity(int availableQuantity) { 
        this.availableQuantity = availableQuantity;
        calculateStatus();
    }
    public int getPublicationYear() { 
        return publicationYear; 
    }
    public void setPublicationYear(int publicationYear) { 
        this.publicationYear = publicationYear; 
    }
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void calculateStatus() {
        this.status = (availableQuantity <= 0) ? "UNAVAILABLE" :
                     (availableQuantity <= 2) ? "LOW_IN_STOCK" :
                     "AVAILABLE";
    }
}
