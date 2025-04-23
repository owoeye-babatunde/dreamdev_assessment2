package com.babsman.dreamdevs.ui;

import com.babsman.dreamdevs.model.Member;
import com.babsman.dreamdevs.service.MemberService;
import com.babsman.dreamdevs.util.Logger;

import java.util.List;
import java.util.Scanner;

public class MemberManagementUI {
    private final MemberService memberService;
    private final Scanner scanner;
    private final Logger logger;

    public MemberManagementUI(MemberService memberService, Scanner scanner) {
        this.memberService = memberService;
        this.scanner = scanner;
        this.logger = new Logger();
    }

    public void showMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== Member Management =====");
            System.out.println("1. Add a new member");
            System.out.println("2. Update member details");
            System.out.println("3. Delete a member");
            System.out.println("4. Display all members");
            System.out.println("5. Export members to CSV");
            System.out.println("0. Back to main menu");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addMember();
                    break;
                case 2:
                    updateMember();
                    break;
                case 3:
                    deleteMember();
                    break;
                case 4:
                    displayAllMembers();
                    break;
                case 5:
                    exportMembersToCSV();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addMember() {
        System.out.println("\n----- Add a New Member -----");

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        Member member = new Member(name, email, phone);
        memberService.addMember(member);

        System.out.println("Member added successfully!");
    }

    private void updateMember() {
        System.out.println("\n----- Update Member Details -----");

        System.out.print("Enter member ID to update: ");
        int memberId = getIntInput();

        Member member = memberService.getMemberById(memberId);

        if (member != null) {
            System.out.println("Current member details:");
            System.out.println(member);

            System.out.print("Enter new name (or press Enter to keep current): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                member.setName(name);
            }

            System.out.print("Enter new email (or press Enter to keep current): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                member.setEmail(email);
            }

            System.out.print("Enter new phone (or press Enter to keep current): ");
            String phone = scanner.nextLine();
            if (!phone.isEmpty()) {
                member.setPhone(phone);
            }

            memberService.updateMember(member);
            System.out.println("Member updated successfully!");
        } else {
            System.out.println("Member not found with ID: " + memberId);
        }
    }

    private void deleteMember() {
        System.out.println("\n----- Delete a Member -----");

        System.out.print("Enter member ID to delete: ");
        int memberId = getIntInput();

        Member member = memberService.getMemberById(memberId);

        if (member != null) {
            System.out.println("Member to delete:");
            System.out.println(member);

            System.out.print("Are you sure you want to delete this member? (y/n): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("y")) {
                memberService.deleteMember(memberId);
                System.out.println("Member deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("Member not found with ID: " + memberId);
        }
    }

    private void displayAllMembers() {
        List<Member> members = memberService.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        System.out.println("\n----- Members -----");
        System.out.printf("%-5s %-20s %-30s %-15s%n",
                "ID", "Name", "Email", "Phone");
        System.out.println("-".repeat(75));

        for (Member member : members) {
            System.out.printf("%-5d %-20s %-30s %-15s%n",
                    member.getMemberId(),
                    truncate(member.getName(), 20),
                    truncate(member.getEmail(), 30),
                    truncate(member.getPhone(), 15));
        }
    }

    private void exportMembersToCSV() {
        memberService.exportMembersToCSV();
        System.out.println("Members exported to CSV successfully!");
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    private int getIntInput() {
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
