package com.library.dao;
import com.library.model.Member;
import com.library.util.DatabaseConnection;
import com.library.util.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {

    @Override
    public void addMember(Member member) {
        String sql = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.executeUpdate();

        } catch (SQLException e) {
            Logger.log("ADD_MEMBER_ERROR: " + e.getMessage());
            if (e.getSQLState().equals("23505")) { // Handle duplicate email
                throw new RuntimeException("Email already exists: " + member.getEmail());
            }
            throw new RuntimeException("Failed to add member", e);
        }
    }

    @Override
    public void updateMember(Member member) {
        String sql = "UPDATE members SET name = ?, email = ?, phone = ? WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.setInt(4, member.getMemberId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed: No member found with ID " + member.getMemberId());
            }

        } catch (SQLException e) {
            Logger.log("UPDATE_MEMBER_ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to update member", e);
        }
    }

    @Override
    public void deleteMember(int memberId) {
        // Check if member exists
        Member member = getMemberById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Delete failed: Member ID " + memberId + " does not exist");
        }

        String sql = "DELETE FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            Logger.log("DELETE_MEMBER_ERROR: " + e.getMessage());
            if (e.getSQLState().equals("23503")) { // Handle foreign key constraint (borrowing records)
                throw new RuntimeException("Cannot delete member: Associated borrowing records exist", e);
            }
            throw new RuntimeException("Failed to delete member", e);
        }
    }

    @Override
    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            } else {
                return null;
            }

        } catch (SQLException e) {
            Logger.log("FETCH_MEMBER_ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to fetch member", e);
        }
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Member member = new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                members.add(member);
            }
            return members;

        } catch (SQLException e) {
            Logger.log("FETCH_ALL_MEMBERS_ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to fetch members", e);
        }
    }
}