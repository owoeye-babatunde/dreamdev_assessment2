package com.library.app;
import com.library.dao.BorrowingDAO;
import com.library.dao.BorrowingDAOImpl;
import com.library.model.BorrowingRecord;
import java.sql.Date;
import java.util.List;

public class MainBorrowingRecord {
    public static void main(String[] args) {
        BorrowingDAO borrowingDAO = new BorrowingDAOImpl();

        try {
            // 1. Add a borrowing record
            BorrowingRecord newRecord = new BorrowingRecord(
                    0,  // record_id will be auto-generated
                    1,  // book_id (must exist in books table)
                    1,  // member_id (must exist in members table)
                    null, // borrow_date will be set by DAO
                    null
            );
            borrowingDAO.addBorrowingRecord(newRecord);
            System.out.println("Added new borrowing record with ID: " + newRecord.getRecordId());

            // 2. Get record by ID
            BorrowingRecord fetchedRecord = borrowingDAO.getBorrowingRecordById(newRecord.getRecordId());
            System.out.println("\nFetched Record:");
            System.out.println("Book ID: " + fetchedRecord.getBookId());
            System.out.println("Borrow Date: " + fetchedRecord.getBorrowDate());

            // 3. Update return date
            borrowingDAO.updateReturnDate(newRecord.getRecordId());
            System.out.println("\nUpdated return date");

            // 4. Get all records
            List<BorrowingRecord> allRecords = borrowingDAO.getAllBorrowingRecords();
            System.out.println("\nAll Borrowing Records:");
            allRecords.forEach(record ->
                    System.out.println("Record ID: " + record.getRecordId()
                            + " | Returned: " + (record.getReturnDate() != null))
            );

            // 5. Delete the record
            borrowingDAO.deleteBorrowingRecord(newRecord.getRecordId());
            System.out.println("\nDeleted record ID: " + newRecord.getRecordId());

        } catch (RuntimeException e) {
            System.err.println("Operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}