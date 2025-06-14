# Library Management System

A Java-based application for managing books, members, and borrowing records in a library. Built with JDBC (PostgreSQL), DAO pattern, file handling, and Java Collections.

## Features
- **Book Management**: Add, update, delete, and search books.
- **Member Management**: Add, update, and delete members.
- **Borrowing Management**: Track book borrow/return activities.
- **Logging**: Log system activities to `library_log.txt`.
- **CSV Export**: Export book/member details to `books.csv` or `members.csv`.

## Technologies
- **Java 17**
- **PostgreSQL**
- **Maven**
- **JDBC**
- **Java Collections Framework**

## Getting Started

### Prerequisites
- Java 17+
- Maven
- PostgreSQL

### Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/owoeye-babatunde/dreamdev_assessment2.git
   cd dreamdev_assessment2
   ```

2. **Database Setup**:
    - Create a PostgreSQL database named `library`
    - The application will automatically create the required tables (`books`, `members`, `borrowing_records`) on first run
    - Ensure PostgreSQL is running and accessible

3. **Configuration**:
    - The application uses default PostgreSQL settings (localhost:5432)
    - Update database credentials in `src/main/resources/config.properties` if needed:
      ```
      db.url=jdbc:postgresql://localhost:5432/library
      db.username=your_username
      db.password=your_password
      ```

4. **Build and Run**:
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.maxiflexy.dreamdevs.Main"
   ```

5. **Usage**:
   - Follow the menu prompts to manage books, members, and borrowing records
   - Logs are written to `library_log.txt`
   - CSV exports are saved in the project root directory
