package com.babsman.dreamdevs.service;

import com.babsman.dreamdevs.dao.impl.BorrowingDAOImpl;
import com.babsman.dreamdevs.dao.interfaces.BorrowingDAO;
import com.babsman.dreamdevs.model.BorrowingRecord;
import com.babsman.dreamdevs.util.Logger;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorrowingService {
    private final BorrowingDAO borrowingDAO;
    private final BookService bookService;
    private final Logger logger;

    // HashMap to track borrowed books (key: bookId, value: memberId)
    private final Map<Integer, Integer> borrowedBooks;

    public BorrowingService(BookService bookService) {
        this.borrowingDAO = new BorrowingDAOImpl();
        this.bookService = bookService;
        this.logger = new Logger();
        this.borrowedBooks = new HashMap<>();
        loadActiveBorrowings();
    }

    public boolean borrowBook(int bookId, int memberId) {
        // Check if book is available
        if (bookService.updateBookAvailability(bookId, false)) {
            // Create borrowing record
            BorrowingRecord record = new BorrowingRecord(
                    bookId,
                    memberId,
                    Date.valueOf(LocalDate.now())
            );

            borrowingDAO.addBorrowingRecord(record);
            borrowedBooks.put(bookId, memberId);
            return true;
        }

        return false;
    }

    public boolean returnBook(int recordId) {
        BorrowingRecord record = borrowingDAO.getBorrowingRecordById(recordId);

        if (record != null && record.getReturnDate() == null) {
            record.setReturnDate(Date.valueOf(LocalDate.now()));
            borrowingDAO.updateReturnDate(recordId, record.getReturnDate());

            // Increment available copies
            bookService.updateBookAvailability(record.getBookId(), true);

            // Remove from tracking map
            borrowedBooks.remove(record.getBookId());

            return true;
        }

        logger.log("Return book failed: Record not found or already returned");
        return false;
    }

    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingDAO.getAllBorrowingRecords();
    }

    public List<BorrowingRecord> getActiveBorrowingRecords() {
        return borrowingDAO.getActiveBorrowingRecords();
    }

    public List<BorrowingRecord> getBorrowingRecordsByMember(int memberId) {
        return borrowingDAO.getBorrowingRecordsByMember(memberId);
    }

    public boolean isBookBorrowed(int bookId) {
        return borrowedBooks.containsKey(bookId);
    }

    public Map<Integer, Integer> getBorrowedBooksMap() {
        return new HashMap<>(borrowedBooks);
    }

    private void loadActiveBorrowings() {
        List<BorrowingRecord> activeRecords = borrowingDAO.getActiveBorrowingRecords();

        for (BorrowingRecord record : activeRecords) {
            borrowedBooks.put(record.getBookId(), record.getMemberId());
        }
    }
}
