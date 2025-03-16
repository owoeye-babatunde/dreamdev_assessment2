package com.library.dao;
import com.library.model.BorrowingRecord;
import java.util.List;

public interface BorrowingDAO {
    void addBorrowingRecord(BorrowingRecord record);
    void updateReturnDate(int recordId);
    void deleteBorrowingRecord(int recordId);
    BorrowingRecord getBorrowingRecordById(int recordId);
    List<BorrowingRecord> getAllBorrowingRecords();
    List<BorrowingRecord> getActiveBorrowings();
    void updateReturnDateByBookId(int bookId);
}