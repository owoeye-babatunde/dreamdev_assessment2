package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;
import java.util.*;
import java.util.stream.Collectors;

public class BookService {
    private final BookDAO bookDAO;
    private List<Book> cachedBooks = new ArrayList<>(); // In-memory cache
    private final Map<Integer, Book> bookMap = new HashMap<>(); // For quick lookup

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
        refreshCache(); // Load data from DB on initialization
    }

    // Refresh cache from database
    public void refreshCache() {
        cachedBooks = bookDAO.getAllBooks();
        bookMap.clear();
        cachedBooks.forEach(book -> bookMap.put(book.getBookId(), book));
    }

    // Search by title (case-insensitive)
    public List<Book> searchBooksByTitle(String title) {
        return cachedBooks.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Search by author (case-insensitive)
    public List<Book> searchBooksByAuthor(String author) {
        return cachedBooks.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Sort by title (using Comparator)
    public List<Book> sortBooksByTitle() {
        cachedBooks.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
        return cachedBooks;
    }

    // Sort by genre (using Comparator)
    public List<Book> sortBooksByGenre() {
        cachedBooks.sort(Comparator.comparing(Book::getGenre, String.CASE_INSENSITIVE_ORDER));
        return cachedBooks;
    }

    // Add a book (update cache after DB operation)
    public void addBook(Book book) {
        bookDAO.addBook(book);
        refreshCache(); // Or cachedBooks.add(book) if optimistic
    }
}