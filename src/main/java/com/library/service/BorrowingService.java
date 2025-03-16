package com.library.service;

import com.library.dao.BorrowingDAO;
import com.library.model.BorrowingRecord;
import java.util.HashMap;
import java.util.Map;

public class BorrowingService {
    private final BorrowingDAO borrowingDAO;
    private final Map<Integer, Integer> borrowedBooks = new HashMap<>(); // Key: book_id, Value: member_id

    public BorrowingService(BorrowingDAO borrowingDAO) {
        this.borrowingDAO = borrowingDAO;
        initializeBorrowedBooks(); // Sync with database on startup
    }

    // Load active borrowings into the map
    private void initializeBorrowedBooks() {
        borrowingDAO.getActiveBorrowings().forEach(record ->
                borrowedBooks.put(record.getBookId(), record.getMemberId())
        );
    }

    // Borrow a book (update DB and map)
    public void borrowBook(int bookId, int memberId) {
        borrowingDAO.addBorrowingRecord(new BorrowingRecord(0, bookId, memberId, null, null));
        borrowedBooks.put(bookId, memberId);
    }

    // Return a book (update DB and map)
    public void returnBook(int bookId) {
        borrowingDAO.updateReturnDateByBookId(bookId); // Assume this method exists
        borrowedBooks.remove(bookId);
    }

    // Check if a book is borrowed
    public boolean isBookBorrowed(int bookId) {
        return borrowedBooks.containsKey(bookId);
    }
}