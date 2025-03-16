package com.library.app;
import com.library.dao.BookDAO;
import com.library.dao.BorrowingDAO;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.service.BorrowingService;
import com.library.dao.BookDAOImpl;
import com.library.dao.BorrowingDAOImpl;
import java.util.List;
import java.util.Scanner;

public class MainBorrow {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAOImpl();
        BorrowingDAO borrowingDAO = new BorrowingDAOImpl();

        BookService bookService = new BookService(bookDAO);
        BorrowingService borrowingService = new BorrowingService(borrowingDAO);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n1. Search Books by Title");
            System.out.println("2. Sort Books by Genre");
            System.out.println("3. Borrow a Book");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter title: ");
                    String title = scanner.next();
                    List<Book> results = bookService.searchBooksByTitle(title);
                    results.forEach(b -> System.out.println(b.getTitle()));
                    break;
                case 2:
                    List<Book> sorted = bookService.sortBooksByGenre();
                    sorted.forEach(b -> System.out.println(b.getGenre() + ": " + b.getTitle()));
                    break;
                case 3:
                    System.out.print("Enter book ID: ");
                    int bookId = scanner.nextInt();
                    System.out.print("Enter member ID: ");
                    int memberId = scanner.nextInt();
                    borrowingService.borrowBook(bookId, memberId);
                    System.out.println("Book borrowed!");
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}