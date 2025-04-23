-- Create database
create database library;


-- Create books table
CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    available_copies INT NOT NULL
);


-- Create members table
CREATE TABLE members (
    member_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50) NOT NULL
);


-- Create borrowing_records table
CREATE TABLE borrowing_records (
    record_id SERIAL PRIMARY KEY,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    borrow_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES books (book_id),
    FOREIGN KEY (member_id) REFERENCES members (member_id)
);


-- Create indexes for performance
CREATE INDEX idx_books_title ON books (title);
CREATE INDEX idx_books_author ON books (author);
CREATE INDEX idx_books_genre ON books (genre);
CREATE INDEX idx_borrowing_book_id ON borrowing_records (book_id);
CREATE INDEX idx_borrowing_member_id ON borrowing_records (member_id);
CREATE INDEX idx_borrowing_return_date ON borrowing_records (return_date);


-- Insert sample data
INSERT INTO books (title, author, genre, available_copies)
VALUES
    ('The Great Gatsby', 'F. Scott Fitzgerald', 'Fiction', 5),
    ('To Kill a Mockingbird', 'Harper Lee', 'Fiction', 3),
    ('1984', 'George Orwell', 'Science Fiction', 4),
    ('Pride and Prejudice', 'Jane Austen', 'Romance', 2),
    ('The Hobbit', 'J.R.R. Tolkien', 'Fantasy', 3);

INSERT INTO members (name, email, phone)
VALUES
    ('John Doe', 'john.doe@example.com', '555-1234'),
    ('Jane Smith', 'jane.smith@example.com', '555-5678'),
    ('Alice Johnson', 'alice.johnson@example.com', '555-9012');