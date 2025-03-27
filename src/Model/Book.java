package Model;

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

    // Constructors
    public Book() {}
    
    public Book(int bookId, String isbn, String title, String author, String genre, 
                int quantity, int availableQuantity, int publicationYear) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.publicationYear = publicationYear;
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
    }
    public int getPublicationYear() { 
        return publicationYear; 
    }
    public void setPublicationYear(int publicationYear) { 
        this.publicationYear = publicationYear; 
    }
}