package com.babsman.dreamdevs.ui;

import com.babsman.dreamdevs.model.Book;
import com.babsman.dreamdevs.model.BorrowingRecord;
import com.babsman.dreamdevs.model.Member;
import com.babsman.dreamdevs.service.BookService;
import com.babsman.dreamdevs.service.BorrowingService;
import com.babsman.dreamdevs.service.MemberService;
import com.babsman.dreamdevs.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BorrowingManagementUI {
    private final BorrowingService borrowingService;
    private final BookService bookService;
    private final MemberService memberService;
    private final Scanner scanner;
    private final Logger logger;

    public BorrowingManagementUI(BorrowingService borrowingService, BookService bookService,
                                 MemberService memberService, Scanner scanner) {
        this.borrowingService = borrowingService;
        this.bookService = bookService;
        this.memberService = memberService;
        this.scanner = scanner;
        this.logger = new Logger();
    }

    public void showMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== Borrowing Management =====");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("3. Display all borrowing records");
            System.out.println("4. Display active borrowing records");
            System.out.println("5. Display borrowing records by member");
            System.out.println("6. Display borrowed books map");
            System.out.println("0. Back to main menu");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    displayAllBorrowingRecords();
                    break;
                case 4:
                    displayActiveBorrowingRecords();
                    break;
                case 5:
                    displayBorrowingRecordsByMember();
                    break;
                case 6:
                    displayBorrowedBooksMap();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void borrowBook() {
        System.out.println("\n----- Borrow a Book -----");

        // Display available books
        List<Book> books = bookService.getAllBooks();
        System.out.println("\nAvailable Books:");
        System.out.printf("%-5s %-30s %-20s %-10s%n",
                "ID", "Title", "Author", "Available");
        System.out.println("-".repeat(70));

        for (Book book : books) {
            if (book.getAvailableCopies() > 0) {
                System.out.printf("%-5d %-30s %-20s %-10d%n",
                        book.getBookId(),
                        truncate(book.getTitle(), 30),
                        truncate(book.getAuthor(), 20),
                        book.getAvailableCopies());
            }
        }

        System.out.print("\nEnter book ID to borrow: ");
        int bookId = getIntInput();

        Book book = bookService.getBookById(bookId);

        if (book == null) {
            System.out.println("Book not found with ID: " + bookId);
            return;
        }

        if (book.getAvailableCopies() <= 0) {
            System.out.println("No copies available for this book.");
            return;
        }

        // Display members
        List<Member> members = memberService.getAllMembers();
        System.out.println("\nMembers:");
        System.out.printf("%-5s %-20s%n", "ID", "Name");
        System.out.println("-".repeat(30));

        for (Member member : members) {
            System.out.printf("%-5d %-20s%n",
                    member.getMemberId(),
                    truncate(member.getName(), 20));
        }

        System.out.print("\nEnter member ID: ");
        int memberId = getIntInput();

        Member member = memberService.getMemberById(memberId);

        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }

        boolean success = borrowingService.borrowBook(bookId, memberId);

        if (success) {
            System.out.println("Book borrowed successfully!");
            System.out.println("Book: " + book.getTitle());
            System.out.println("Member: " + member.getName());
        } else {
            System.out.println("Failed to borrow book. No copies available.");
        }
    }

    private void returnBook() {
        System.out.println("\n----- Return a Book -----");

        // Display active borrowing records
        List<BorrowingRecord> activeRecords = borrowingService.getActiveBorrowingRecords();

        if (activeRecords.isEmpty()) {
            System.out.println("No active borrowing records found.");
            return;
        }

        System.out.println("\nActive Borrowing Records:");
        System.out.printf("%-5s %-30s %-20s %-15s%n",
                "ID", "Book Title", "Member", "Borrow Date");
        System.out.println("-".repeat(75));

        for (BorrowingRecord record : activeRecords) {
            System.out.printf("%-5d %-30s %-20s %-15s%n",
                    record.getRecordId(),
                    truncate(record.getBookTitle(), 30),
                    truncate(record.getMemberName(), 20),
                    record.getBorrowDate());
        }

        System.out.print("\nEnter record ID to return: ");
        int recordId = getIntInput();

        boolean success = borrowingService.returnBook(recordId);

        if (success) {
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Failed to return book. Record not found or already returned.");
        }
    }

    private void displayAllBorrowingRecords() {
        List<BorrowingRecord> records = borrowingService.getAllBorrowingRecords();
        displayBorrowingRecords(records, "All Borrowing Records");
    }

    private void displayActiveBorrowingRecords() {
        List<BorrowingRecord> records = borrowingService.getActiveBorrowingRecords();
        displayBorrowingRecords(records, "Active Borrowing Records");
    }

    private void displayBorrowingRecordsByMember() {
        System.out.print("Enter member ID: ");
        int memberId = getIntInput();

        Member member = memberService.getMemberById(memberId);

        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }

        List<BorrowingRecord> records = borrowingService.getBorrowingRecordsByMember(memberId);
        displayBorrowingRecords(records, "Borrowing Records for " + member.getName());
    }

    private void displayBorrowedBooksMap() {
        Map<Integer, Integer> borrowedBooks = borrowingService.getBorrowedBooksMap();

        if (borrowedBooks.isEmpty()) {
            System.out.println("No books currently borrowed.");
            return;
        }

        System.out.println("\n----- Borrowed Books Map -----");
        System.out.printf("%-30s %-20s%n", "Book", "Borrowed By");
        System.out.println("-".repeat(55));

        for (Map.Entry<Integer, Integer> entry : borrowedBooks.entrySet()) {
            Book book = bookService.getBookById(entry.getKey());
            Member member = memberService.getMemberById(entry.getValue());

            if (book != null && member != null) {
                System.out.printf("%-30s %-20s%n",
                        truncate(book.getTitle(), 30),
                        truncate(member.getName(), 20));
            }
        }
    }

    private void displayBorrowingRecords(List<BorrowingRecord> records, String title) {
        if (records.isEmpty()) {
            System.out.println("No borrowing records found.");
            return;
        }

        System.out.println("\n----- " + title + " -----");
        System.out.printf("%-5s %-30s %-20s %-15s %-15s%n",
                "ID", "Book Title", "Member", "Borrow Date", "Return Date");
        System.out.println("-".repeat(90));

        for (BorrowingRecord record : records) {
            System.out.printf("%-5d %-30s %-20s %-15s %-15s%n",
                    record.getRecordId(),
                    truncate(record.getBookTitle(), 30),
                    truncate(record.getMemberName(), 20),
                    record.getBorrowDate(),
                    record.getReturnDate() == null ? "Not returned" : record.getReturnDate());
        }
    }

    private String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }

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
