# Library Management System

## **Installation & Setup Guide**  
*(Step-by-Step Instructions)*  
**For:** NetBeans IDE 17 or 25, JavaFX 17 or 23, JDK 21, MySQL Connector/J 9.0+

## Test Login details
*Admin(username: admin, password: admin123)*  
*Librarian(username: lib1, password: lib123)*


## Table of Contents
What You Need to Install  
Installing Java (JDK 21)  
Installing NetBeans IDE 17 (and above)  
Installing MySQL Database  
Installing JavaFX 17  
Setting Up the Project  
Connecting to MySQL in NetBeans  
Running the Project  
Troubleshooting  

## What You Need to Install
Before we start, you need these:  
Java Development Kit (JDK) 21 (This lets Java programs run)  
NetBeans IDE 17 (This is where we write and run the code.)  
MySQL Server 8.0+ (This stores all the library data.)  
JavaFX 17 SDK (Makes the program look pretty with buttons and windows)  
MySQL Connector/J 9.0 (Helps Java talk to MySQL)  

## Step 1: Install Java (JDK 21)
### Download JDK 21  
Go to: https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html  
Click on the Windows/macOS/Linux version for your computer.  
Run the downloaded file and follow the steps (just keep clicking "Next").  

### Set the JAVA_HOME environment variable:  
**Windows**  
setx JAVA_HOME "C:\Program Files\Java\jdk-21"
**macOS/Linux:** 
export JAVA_HOME=/usr/lib/jvm/jdk-21


### Check if Java is Installed  
Open Command Prompt (Windows) or Terminal (Mac/Linux).  
Type:  java -version
If it says 21.x.x, you did it right!  

## Step 2: Install NetBeans IDE 17 (and above)
### Download NetBeans 17 (and above)  
Go to: https://netbeans.apache.org/download/nb17/  
Download the "Java SE" version.  
Run the installer and click "Next" until it finishes.  

### Install JavaFX Plugin  
Open NetBeans.  
Go to Tools => Plugins.  
Click the "Available Plugins" tab.  
Search for "JavaFX" and check the box.  
Click "Install" and follow the steps.  

## Step 3: Install MySQL Database
### Download MySQL  
Go to: https://dev.mysql.com/downloads/mysql/  
Download MySQL Community Server 8.0+.  
Run the installer and choose "Developer Default".  
Set a password (e.g., root123) and remember it!  

### Check if MySQL is Working  
Open Command Prompt/Terminal.  
Type:  mysql -u root -p
Enter your password (e.g., root123).  
If you see mysql>, it works!  

## Step 4: Install JavaFX 17
### Download JavaFX 17 SDK  
Go to: https://gluonhq.com/products/javafx/  
Download JavaFX 17 SDK for your OS.  
Extract the ZIP file to a folder (e.g., C:\javafx-sdk-17).  

## Step 5: Setting Up the Project
### Download the Project  
Go to the GitHub page:  
https://github.com/KosyAbone/Library-Management-System  
Click "Code => Download ZIP".  
Extract the ZIP file to a folder (e.g., C:\library-project).  

### Open in NetBeans  
Open NetBeans.  
Click File => Open Project.  
Find the library project folder and click Open.  

## Step 6.1: Connecting to MySQL in NetBeans
### Set Up MySQL Connection  
In NetBeans, go to Window => Services.  
Right-click Databases => New Connection.  
Choose MySQL Connector/J.  
Enter:  
Host: localhost  
Port: 3306  
Database: library_db  
Username: root  
Password: root123 (or your password)  
Click Test Connection => If it says "Success", click Finish.  

### Import the Database  
Right-click the new connection => Execute Command.  
Open the library_db.sql file from the project.  
Copy all the text and paste it into NetBeans.  
Click the Run (▶) button to create tables.  

### Step 6.2: Alternative Way to Set Up MySQL Database  
Create the database with cmd:  CREATE DATABASE library_db;
and then run: USE library_db;
Import the SQL script:  
Locate the library_db.sql file in the project.  
Run it in MySQL:  mysql -u root -p library_db < library_db.sql
(Or use MySQL Workbench to import it.)  

## Step 7: Running the Project
### Add JavaFX Libraries  
From the IDE top menu, Click on Tools => Libraries.  
Select New Library, choose a name for it(JavaFX_17) and click OK.  
Click Add JAR/Folder  
Browse to the extracted JavaFX 17 SDK folder (from step 4).  
Go to C:\javafx-sdk-17\lib and select:  
javafx.controls.jar  
javafx.fxml.jar  
javafx-swt.jar  
javafx.base.jar  
javafx.graphics.jar  
javafx.media.jar  
javafx.swing.jar  
javafx.web.jar  
Click OK  

### Add to COMPILE  
Right-click your project name => Click Properties  
Go to Libraries => Compile tab  
Find Classpath and Click the Add Library... button (+)  
Find JavaFX_17 (the newly created library)  
Click Add Library  
Click OK  

### Add to RUN (Like Putting on Running Shoes)  
Still in Project Properties  
Go to Run tab (on the left)  
Find Modulepath (click the + sign to open)  
Click Add Library...  
Again choose JavaFX_17  
Click Add Library  
Apply and Close  

### Set VM Options  
Right-click project => Properties => Run.  
In VM Options, paste:  --module-path "C:\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml
(Change the path if you installed JavaFX somewhere else.)  

### Add MySQL Connector (MySQL Connector/J 9.0)  
Download: https://dev.mysql.com/downloads/connector/j/  
Right-click project => Libraries => Add JAR/Folder.  
Select the downloaded mysql-connector-j-9.0.x.jar.  

### Run the Project!  
Click the Green Play (▶) Button in NetBeans.  
The Login Screen should appear!  

## Troubleshooting
| Problem                     | Solution                                      |
|-----------------------------|-----------------------------------------------|
| JavaFX not working          | Check VM Options path is correct.             |
| MySQL connection error      | Make sure MySQL is running (mysql -u root -p).|
| NetBeans won't start        | Reinstall JDK 21 and set JAVA_HOME.           |

## Final Checks
Java version: java -version => 21.x.x  
MySQL running: mysql -u root -p => Can log in  
Project runs: No errors  

## Summary of what you should have:
- Java 21  
- NetBeans 17 with JavaFX  
- MySQL Database  
- Library System Running!  

## REFERENCES
- **MySQL Workbench Setup Tutorial**:  
  [https://www.youtube.com/watch?v=nng8hDbUpnc](https://www.youtube.com/watch?v=nng8hDbUpnc)  
  *(Video guide for setting up MySQL Workbench)*

- **JavaFX 17 SDK Download**:  
  [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)  
  *(Download the JavaFX SDK based on your system architecture)*

- **MySQL Community Server Download**:  
  [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)  
  *(Select the appropriate MySQL version for your operating system)*

- **GitHub Repository**:  
  [https://github.com/KosyAbone/Library-Management-System](https://github.com/KosyAbone/Library-Management-System)  
  *(Source code and project files)*

- **JavaFX on NetBeans Setup Guide**:  
  [https://www.youtube.com/watch?v=6E4IkTuvUCI](https://www.youtube.com/watch?v=6E4IkTuvUCI)  
  *(Video tutorial for configuring JavaFX with NetBeans)*
