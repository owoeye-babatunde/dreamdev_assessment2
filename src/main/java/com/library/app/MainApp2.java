package com.library.app;
import com.library.dao.BookDAO;
import com.library.dao.BookDAOImpl;
import com.library.dao.MemberDAOImpl;
import com.library.util.CSVExporter;
import java.util.Scanner;

public class MainApp2 {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAOImpl();
        MemberDAOImpl memberDAO = new MemberDAOImpl();

        boolean running = true;
        while (running) {
            System.out.println("\n1. Export Books to CSV");
            System.out.println("2. Export Members to CSV");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    CSVExporter.exportBooksToCSV(bookDAO.getAllBooks());
                    break;
                case 2:
                    CSVExporter.exportMembersToCSV(memberDAO.getAllMembers());
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}