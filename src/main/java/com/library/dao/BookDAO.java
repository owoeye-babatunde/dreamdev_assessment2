package com.library.dao;
import com.library.model.Book;
import java.util.List;

public interface BookDAO {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    Book getBookById(int bookId);
    List<Book> getAllBooks();
}