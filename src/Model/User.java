package Model;

import DAO.BorrowRecordDAO;

/**
 *
 * @author kossy
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String userType;    // ADMIN, LIBRARIAN, MEMBER
    private String memberType;  // STUDENT, FACULTY (only for MEMBER type)

    // Constructors
    public User() {}
    
    public User(int userId, String username, String password, String firstName, String lastName, 
               String email, String phone, String userType, String memberType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.memberType = memberType;
    }

    // Getters and Setters
    public int getUserId() { 
        return userId; 
    }
    public void setUserId(int userId) { 
        this.userId = userId; 
    }
    public String getUsername() { 
        return username; 
    }
    public void setUsername(String username) { 
        this.username = username; 
    }
    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }
    public String getFirstName() { 
        return firstName; 
    }
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }
    public String getLastName() { 
        return lastName; 
    }
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }
    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
    public String getUserType() { 
        return userType; 
    }
    public void setUserType(String userType) { 
        this.userType = userType; 
    }
    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public int getBorrowedBooksCount() {
        return new BorrowRecordDAO().getBorrowedBooksCount(this.userId);
    }
    
}