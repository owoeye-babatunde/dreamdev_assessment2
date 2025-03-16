package com.library.app;
import com.library.dao.MemberDAO;
import com.library.dao.MemberDAOImpl;
import com.library.model.Member;

public class MainAppMember {
    public static void main(String[] args) {
        MemberDAO memberDAO = new MemberDAOImpl();

        // Add a member
        Member member = new Member(0, "Alice Smith", "alico@example.com", "123-456-7890");
        memberDAO.addMember(member);

        // Update a member
        Member alice = memberDAO.getMemberById(1);
        alice.setPhone("987-654-3210");
        memberDAO.updateMember(alice);

        // Fetch all members
        System.out.println("All Members:");
        memberDAO.getAllMembers().forEach(m -> System.out.println(m.getName()));

        // Delete a member (with error handling)
        try {
            memberDAO.deleteMember(2);
        } catch (RuntimeException e) {
            System.err.println("Deletion failed: " + e.getMessage());
        }
    }
}