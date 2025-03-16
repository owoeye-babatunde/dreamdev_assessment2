package com.library.util;

import com.library.model.Book;
import com.library.model.Member;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    // Export books to books.csv
    public static void exportBooksToCSV(List<Book> books) {
        String filename = "books.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            writer.write("book_id,title,author,genre,available_copies");
            writer.newLine();

            // Write data
            for (Book book : books) {
                String line = String.format("%d,%s,%s,%s,%d",
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre(),
                        book.getAvailableCopies()
                );
                writer.write(line);
                writer.newLine();
            }
            Logger.log("Exported books to " + filename);
        } catch (IOException e) {
            Logger.log("CSV_EXPORT_ERROR: " + e.getMessage());
        }
    }

    // Export members to members.csv
    public static void exportMembersToCSV(List<Member> members) {
        String filename = "members.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            writer.write("member_id,name,email,phone");
            writer.newLine();

            // Write data
            for (Member member : members) {
                String line = String.format("%d,%s,%s,%s",
                        member.getMemberId(),
                        member.getName(),
                        member.getEmail(),
                        member.getPhone()
                );
                writer.write(line);
                writer.newLine();
            }
            Logger.log("Exported members to " + filename);
        } catch (IOException e) {
            Logger.log("CSV_EXPORT_ERROR: " + e.getMessage());
        }
    }
}