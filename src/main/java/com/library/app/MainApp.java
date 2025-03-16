package com.library.app;
import com.library.dao.BookDAO;
import com.library.dao.BookDAOImpl;
import com.library.model.Book;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAOImpl();

        // Add a book
        Book book = new Book();
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setGenre("Programming");
        book.setAvailableCopies(10);
        bookDAO.addBook(book);

        // Retrieve all books
        System.out.println("All Books:");
        bookDAO.getAllBooks().forEach(System.out::println);

        // Update a book
        Book bookToUpdate = bookDAO.getBookById(1);
        bookToUpdate.setTitle("My chosen Title");
        bookDAO.updateBook(bookToUpdate);
        bookDAO.deleteBook(2);

        // Fetch a book
        Book bookToFetch = bookDAO.getBookById(3);
        if (bookToFetch != null) {
            System.out.println("Found book: " + bookToFetch.getTitle());
        } else {
            System.out.println("Book not found");
        }
        //fetch all books
        List<Book> allBooks = bookDAO.getAllBooks();
        System.out.println("All Books:" + allBooks);
    }

}