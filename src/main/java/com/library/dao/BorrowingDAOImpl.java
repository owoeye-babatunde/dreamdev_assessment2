package com.library.dao;
import com.library.model.BorrowingRecord;
import com.library.util.DatabaseConnection;
import com.library.util.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDAOImpl implements BorrowingDAO {

    @Override
    public void addBorrowingRecord(BorrowingRecord record) {
        String sql = "INSERT INTO borrowing_records (book_id, member_id, borrow_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, record.getBookId());
            stmt.setInt(2, record.getMemberId());
            stmt.setDate(3, new Date(System.currentTimeMillis())); // Current date

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating borrowing record failed");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record.setRecordId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            Logger.log("ADD_BORROWING_ERROR: " + e.getMessage());
            if (e.getSQLState().equals("23503")) {
                throw new RuntimeException("Invalid book/member ID");
            }
            throw new RuntimeException("Failed to create borrowing record", e);
        }
    }

    @Override
    public void updateReturnDate(int recordId) {
        String sql = "UPDATE borrowing_records SET return_date = ? WHERE record_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new Date(System.currentTimeMillis()));
            stmt.setInt(2, recordId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No borrowing record found with ID: " + recordId);
            }
        } catch (SQLException e) {
            Logger.log("UPDATE_RETURN_DATE_ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to update return date", e);
        }
    }
    @Override
    public void deleteBorrowingRecord(int recordId) {
        String sql = "DELETE FROM borrowing_records WHERE record_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recordId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No borrowing record found with ID: " + recordId);
            }
        } catch (SQLException e) {
            Logger.log("DELETE_BORROWING_ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to delete borrowing record", e);
        }
    }
    @Override
    public BorrowingRecord getBorrowingRecordById(int recordId) {
        String sql = "SELECT * FROM borrowing_records WHERE record_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recordId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new BorrowingRecord(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            Logger.log("FETCH_BORROWING_ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to fetch borrowing record", e);
        }
    }
    @Override
    public List<BorrowingRecord> getAllBorrowingRecords() {
        List<BorrowingRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrowing_records";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(new BorrowingRecord(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                ));
            }
            return records;
        } catch (SQLException e) {
            Logger.log("FETCH_ALL_BORROWINGS_ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to fetch borrowing records", e);
        }
    }
    @Override
    public void updateReturnDateByBookId(int bookId) {
        String sql = "UPDATE borrowing_records SET return_date = CURRENT_DATE WHERE book_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No active borrowing record found for book ID: " + bookId);
            }
        } catch (SQLException e) {
            Logger.log("UPDATE_RETURN_DATE_BY_BOOK_ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to update return date", e);
        }
    }

    @Override
    public List<BorrowingRecord> getActiveBorrowings() {
        List<BorrowingRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrowing_records WHERE return_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(new BorrowingRecord(
                        rs.getInt("record_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("borrow_date"),
                        null // Return date is NULL for active borrowings
                ));
            }
        } catch (SQLException e) {
            Logger.log("FETCH_ACTIVE_BORROWINGS_ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to fetch active borrowings", e);
        }
        return records;
    }

    // Other methods (deleteBorrowingRecord, getBorrowingRecordById, getAllBorrowingRecords)
    // follow similar patterns

}