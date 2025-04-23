package com.babsman.dreamdevs.ui;

import com.babsman.dreamdevs.service.BookService;
import com.babsman.dreamdevs.service.BorrowingService;
import com.babsman.dreamdevs.service.MemberService;
import com.babsman.dreamdevs.util.Logger;

import java.util.Scanner;

public class LibraryManagementSystem {
    private final BookService bookService;
    private final MemberService memberService;
    private final BorrowingService borrowingService;
    private final Scanner scanner;
    private final Logger logger;

    public LibraryManagementSystem() {
        this.bookService = new BookService();
        this.memberService = new MemberService();
        this.borrowingService = new BorrowingService(bookService);
        this.scanner = new Scanner(System.in);
        this.logger = new Logger();
    }

    public void start() {
        logger.log("Library Management System started");

        boolean exit = false;

        while (!exit) {
            showMainMenu();

            System.out.print("Enter your choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    BookManagementUI bookUI = new BookManagementUI(bookService, scanner);
                    bookUI.showMenu();
                    break;
                case 2:
                    MemberManagementUI memberUI = new MemberManagementUI(memberService, scanner);
                    memberUI.showMenu();
                    break;
                case 3:
                    BorrowingManagementUI borrowingUI = new BorrowingManagementUI(
                            borrowingService, bookService, memberService, scanner);
                    borrowingUI.showMenu();
                    break;
                case 0:
                    exit = true;
                    logger.log("Library Management System exited");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Borrowing Management");
        System.out.println("0. Exit");
    }

    private int getIntInput() {
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}