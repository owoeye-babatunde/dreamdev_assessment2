package com.babsman.dreamdevs.service;

import com.babsman.dreamdevs.dao.impl.BookDAOImpl;
import com.babsman.dreamdevs.dao.interfaces.BookDAO;
import com.babsman.dreamdevs.model.Book;
import com.babsman.dreamdevs.util.CSVExporter;
import com.babsman.dreamdevs.util.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private final BookDAO bookDAO;
    private final Logger logger;
    private final CSVExporter csvExporter;

    // In-memory cache for books
    private List<Book> booksCache;

    public BookService() {
        this.bookDAO = new BookDAOImpl();
        this.logger = new Logger();
        this.csvExporter = new CSVExporter();
        this.booksCache = new ArrayList<>();
        refreshCache();
    }

    public void addBook(Book book) {
        bookDAO.addBook(book);
        refreshCache();
    }

    public void updateBook(Book book) {
        bookDAO.updateBook(book);
        refreshCache();
    }

    public void deleteBook(int bookId) {
        bookDAO.deleteBook(bookId);
        refreshCache();
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(booksCache);
    }

    public Book getBookById(int bookId) {
        return booksCache.stream()
                .filter(book -> book.getBookId() == bookId)
                .findFirst()
                .orElseGet(() -> bookDAO.getBookById(bookId));
    }

    public List<Book> searchBooksByTitle(String title) {
        return booksCache.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByAuthor(String author) {
        return booksCache.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByGenre(String genre) {
        return booksCache.stream()
                .filter(book -> book.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> sortBooksByTitle() {
        List<Book> sortedBooks = new ArrayList<>(booksCache);
        Collections.sort(sortedBooks); // Using Comparable implementation
        return sortedBooks;
    }

    public List<Book> sortBooksByGenre() {
        List<Book> sortedBooks = new ArrayList<>(booksCache);
        sortedBooks.sort(Comparator.comparing(Book::getGenre)); // Using Comparator
        return sortedBooks;
    }

    public boolean updateBookAvailability(int bookId, boolean increment) {
        boolean success = bookDAO.updateBookAvailability(bookId, increment);
        if (success) {
            refreshCache();
        }
        return success;
    }

    public void exportBooksToCSV() {
        csvExporter.exportBooksToCSV(booksCache);
    }

    private void refreshCache() {
        booksCache = bookDAO.getAllBooks();
    }
}
