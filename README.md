# Football Score Information System

A comprehensive JavaFX desktop application for managing football scores, teams, players, and matches with MySQL database backend.

## 🎯 Project Overview

**Course:** CIS096-1 – Principles of Programming and Data Structures  
**Assessment:** Assessment 2 – Individual Project  
**Type:** Client-Server Desktop Application

## 📋 Features

### Customer Features
- View all teams and their statistics
- View match results and schedules
- View league standings
- View player statistics
- Search teams and matches

### Admin Features
- Manage teams (Add, Edit, Delete)
- Manage matches (Schedule, Update scores)
- Manage players (Add, Edit, Delete)
- Manage users (View, Block/Unblock)
- Generate reports

## 🛠️ Technology Stack

- **Frontend:** JavaFX 17+  
- **Backend:** Java 17+  
- **Database:** MySQL 8.0+  
- **Build Tool:** Maven  
- **IDE:** Eclipse/IntelliJ IDEA

## 📁 Project Structure

```
football-score-system/
├── src/
│   ├── main/
│   │   ├── java/com/football/
│   │   │   ├── models/
│   │   │   ├── dao/
│   │   │   ├── services/
│   │   │   ├── controllers/
│   │   │   └── utils/
│   │   └── resources/
│   │       ├── css/
├── database/
│   ├── schema.sql
│   ├── triggers.sql
│   └── sample_data.sql
```

## 🚀 Setup Instructions

### Prerequisites
- Java JDK 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+
- JavaFX SDK 17+

### Database Setup

1. Install MySQL and start the service
2. Open MySQL Workbench or command line
3. Execute the following scripts in order:
   ```bash
   mysql -u root -p < database/schema.sql
   mysql -u root -p < database/triggers.sql
   mysql -u root -p < database/sample_data.sql
   ```

4. Update database credentials in `DatabaseConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/football_score_db";
   private static final String USER = "your_username";
   private static final String PASSWORD = "your_password";
   ```

### Application Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/Nirhang10/football-score-system.git
   cd football-score-system
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn javafx:run
   ```

   Or run the Main class from your IDE.

## 👤 Default Login Credentials

### Admin Account
- **Username:** admin
- **Password:** admin123

### Customer Account
- **Username:** john_doe
- **Password:** admin123

## 📊 Database Schema

The system uses 4 main tables:
- **users** - Store user accounts (customers and admins)
- **teams** - Store team information and statistics
- **players** - Store player details and statistics
- **matches** - Store match schedules and results

## 🎨 UI Design

Design reference: [Figma Design](https://figma.com/community/file/1605092829862039471/football-score-information-system)

## 📝 License

This is a college project for educational purposes.

## 👨‍💻 Author

**Nirhang10**  
- CIS096-1 – Principles of Programming and Data Structures
