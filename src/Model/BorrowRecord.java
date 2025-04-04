package Model;

import java.sql.Date;

public class BorrowRecord {
    public static final String BORROWED = "BORROWED";
    public static final String RETURNED = "RETURNED";
    public static final String OVERDUE = "OVERDUE";
    
    private int borrowId;
    private int bookId;
    private int userId;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private String status;
    private String bookTitle;
    private String userName;

    public BorrowRecord() {}
    
    public BorrowRecord(int borrowId, int bookId, int userId, Date borrowDate, 
                       Date dueDate, Date returnDate, String status) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getBorrowId() { 
        return borrowId; 
    }
    public void setBorrowId(int borrowId) { 
        this.borrowId = borrowId; 
    }
    public int getBookId() { 
        return bookId; 
    }
    public void setBookId(int bookId) { 
        this.bookId = bookId; 
    }
    public int getUserId() { 
        return userId; 
    }
    public void setUserId(int userId) { 
        this.userId = userId; 
    }
    public Date getBorrowDate() { 
        return borrowDate; 
    }
    public void setBorrowDate(Date borrowDate) { 
        this.borrowDate = borrowDate; 
    }
    public Date getDueDate() { 
        return dueDate; 
    }
    public void setDueDate(Date dueDate) { 
        this.dueDate = dueDate; 
    }
    public Date getReturnDate() { 
        return returnDate; 
    }
    public void setReturnDate(Date returnDate) { 
        this.returnDate = returnDate; 
    }
    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        if (!status.equals(BORROWED) && 
            !status.equals(RETURNED) && 
            !status.equals(OVERDUE)) {
            throw new IllegalArgumentException("Invalid status value");
        }
        this.status = status; 
    }
    public String getBookTitle() { 
        return bookTitle; 
    }
    public void setBookTitle(String bookTitle) { 
        this.bookTitle = bookTitle; 
    }
    public String getUserName() { 
        return userName; 
    }
    public void setUserName(String userName) { 
        this.userName = userName; 
    }
}
