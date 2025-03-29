package Model;

/**
 *
 * @author kossy
 */

import java.sql.Timestamp;

public class Fine {
    private int fineId;
    private int borrowId;
    private int userId;
    private double amount;
    private String status;
    private String reason;
    private Timestamp issuedDate;
    private Timestamp paidDate;
    private String userName;  
    private String bookTitle; 

    // Constructors
    public Fine() {}
    
    public Fine(int fineId, int borrowId, int userId, double amount, 
                String status, Timestamp issuedDate, Timestamp paidDate) {
        this.fineId = fineId;
        this.borrowId = borrowId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.issuedDate = issuedDate;
        this.paidDate = paidDate;
    }

    // Getters and Setters
    public int getFineId() { 
        return fineId; 
    }
    public void setFineId(int fineId) { 
        this.fineId = fineId; 
    }
    public int getBorrowId() { 
        return borrowId; 
    }
    public void setBorrowId(int borrowId) { 
        this.borrowId = borrowId; 
    }
    public int getUserId() { 
        return userId; 
    }
    public void setUserId(int userId) { 
        this.userId = userId; 
    }
    public double getAmount() { 
        return amount; 
    }
    public void setAmount(double amount) { 
        this.amount = amount; 
    }
    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
    public String getReason() { 
        return reason; 
    }
    public void setReason(String reason) { 
        this.reason = reason; 
    }
    public Timestamp getIssuedDate() { 
        return issuedDate; 
    }
    public void setIssuedDate(Timestamp issuedDate) { 
        this.issuedDate = issuedDate; 
    }
    public Timestamp getPaidDate() { 
        return paidDate; 
    }
    public void setPaidDate(Timestamp paidDate) { 
        this.paidDate = paidDate; 
    }
    public String getUserName() { 
        return userName; 
    }
    public void setUserName(String userName) { 
        this.userName = userName; 
    }
    public String getBookTitle() { 
        return bookTitle; 
    }
    public void setBookTitle(String bookTitle) { 
        this.bookTitle = bookTitle; 
    }
}