package com.library.dao;
import com.library.model.Member;
import java.util.List;

public interface MemberDAO {
    void addMember(Member member);
    void updateMember(Member member);
    void deleteMember(int memberId);
    Member getMemberById(int memberId);
    List<Member> getAllMembers();
}