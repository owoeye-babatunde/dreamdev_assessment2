package com.babsman.dreamdevs.ui;

import com.babsman.dreamdevs.model.Book;
import com.babsman.dreamdevs.service.BookService;
import com.babsman.dreamdevs.util.Logger;
import java.util.List;
import java.util.Scanner;

public class BookManagementUI {
    private final BookService bookService;
    private final Scanner scanner;
    private final Logger logger;

    public BookManagementUI(BookService bookService, Scanner scanner) {
        this.bookService = bookService;
        this.scanner = scanner;
        this.logger = new Logger();
    }

    public void showMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== Book Management =====");
            System.out.println("1. Add a new book");
            System.out.println("2. Update book details");
            System.out.println("3. Delete a book");
            System.out.println("4. Search books");
            System.out.println("5. Display all books");
            System.out.println("6. Display books sorted by title");
            System.out.println("7. Display books sorted by genre");
            System.out.println("8. Export books to CSV");
            System.out.println("0. Back to main menu");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    updateBook();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    searchBooks();
                    break;
                case 5:
                    displayAllBooks();
                    break;
                case 6:
                    displayBooksSortedByTitle();
                    break;
                case 7:
                    displayBooksSortedByGenre();
                    break;
                case 8:
                    exportBooksToCSV();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addBook() {
        System.out.println("\n----- Add a New Book -----");

        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter number of copies available: ");
        int availableCopies = getIntInput();

        Book book = new Book(title, author, genre, availableCopies);
        bookService.addBook(book);

        System.out.println("Book added successfully!");
    }

    private void updateBook() {
        System.out.println("\n----- Update Book Details -----");

        System.out.print("Enter book ID to update: ");
        int bookId = getIntInput();

        Book book = bookService.getBookById(bookId);

        if (book != null) {
            System.out.println("Current book details:");
            System.out.println(book);

            System.out.print("Enter new title (or press Enter to keep current): ");
            String title = scanner.nextLine();
            if (!title.isEmpty()) {
                book.setTitle(title);
            }

            System.out.print("Enter new author (or press Enter to keep current): ");
            String author = scanner.nextLine();
            if (!author.isEmpty()) {
                book.setAuthor(author);
            }

            System.out.print("Enter new genre (or press Enter to keep current): ");
            String genre = scanner.nextLine();
            if (!genre.isEmpty()) {
                book.setGenre(genre);
            }

            System.out.print("Enter new number of copies available (or 0 to keep current): ");
            int availableCopies = getIntInput();
            if (availableCopies > 0) {
                book.setAvailableCopies(availableCopies);
            }

            bookService.updateBook(book);
            System.out.println("Book updated successfully!");
        } else {
            System.out.println("Book not found with ID: " + bookId);
        }
    }

    private void deleteBook() {
        System.out.println("\n----- Delete a Book -----");

        System.out.print("Enter book ID to delete: ");
        int bookId = getIntInput();

        Book book = bookService.getBookById(bookId);

        if (book != null) {
            System.out.println("Book to delete:");
            System.out.println(book);

            System.out.print("Are you sure you want to delete this book? (y/n): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("y")) {
                bookService.deleteBook(bookId);
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("Book not found with ID: " + bookId);
        }
    }

    private void searchBooks() {
        System.out.println("\n----- Search Books -----");
        System.out.println("1. Search by title");
        System.out.println("2. Search by author");
        System.out.println("3. Search by genre");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                searchBooksByTitle();
                break;
            case 2:
                searchBooksByAuthor();
                break;
            case 3:
                searchBooksByGenre();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void searchBooksByTitle() {
        System.out.print("Enter title to search: ");
        String title = scanner.nextLine();

        List<Book> books = bookService.searchBooksByTitle(title);
        displayBooks(books);
    }

    private void searchBooksByAuthor() {
        System.out.print("Enter author to search: ");
        String author = scanner.nextLine();

        List<Book> books = bookService.searchBooksByAuthor(author);
        displayBooks(books);
    }

    private void searchBooksByGenre() {
        System.out.print("Enter genre to search: ");
        String genre = scanner.nextLine();

        List<Book> books = bookService.searchBooksByGenre(genre);
        displayBooks(books);
    }

    private void displayAllBooks() {
        List<Book> books = bookService.getAllBooks();
        displayBooks(books);
    }

    private void displayBooksSortedByTitle() {
        List<Book> books = bookService.sortBooksByTitle();
        displayBooks(books);
    }

    private void displayBooksSortedByGenre() {
        List<Book> books = bookService.sortBooksByGenre();
        displayBooks(books);
    }

    private void exportBooksToCSV() {
        bookService.exportBooksToCSV();
        System.out.println("Books exported to CSV successfully!");
    }

    private void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println("\n----- Books -----");
        System.out.printf("%-5s %-30s %-20s %-15s %-10s%n",
                "ID", "Title", "Author", "Genre", "Available");
        System.out.println("-".repeat(85));

        for (Book book : books) {
            System.out.printf("%-5d %-30s %-20s %-15s %-10d%n",
                    book.getBookId(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthor(), 20),
                    truncate(book.getGenre(), 15),
                    book.getAvailableCopies());
        }
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    private int getIntInput() {
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
