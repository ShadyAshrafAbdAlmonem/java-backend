import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Library library = new Library();
        seedLibrary(library);

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    library.listCatalogue();
                    break;
                case "2":
                    registerMember(library);
                    break;
                case "3":
                    borrowItem(library);
                    break;
                case "4":
                    returnItem(library);
                    break;
                case "5":
                    renewLoan(library);
                    break;
                case "6":
                    searchItem(library);
                    break;
                case "7":
                    viewItemsByStatus(library);
                    break;
                case "8":
                    payFines(library);
                    break;
                case "9":
                    library.listAllMembers();
                    break;
                case "10":
                    library.printReport();
                    break;
                case "0":
                    System.out.println("Thank you for using the Bayt Al Hekma Library Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 0 to 10.");
                    break;
            }
            if (running) {
                System.out.println();
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("========================================");
        System.out.println("  Bayt Al Hekma Library Management System");
        System.out.println("========================================");
        System.out.println("1. View catalogue");
        System.out.println("2. Register member");
        System.out.println("3. Borrow item");
        System.out.println("4. Return item");
        System.out.println("5. Renew loan");
        System.out.println("6. Search item by ID");
        System.out.println("7. View items by status");
        System.out.println("8. Pay outstanding fines");
        System.out.println("9. View all members");
        System.out.println("10. Library report");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void seedLibrary(Library library) {
        library.registerItem(new Book("B001", "The Old Man and the Sea", "Ernest Hemingway", 127));
        library.registerItem(new Book("B002", "One Hundred Years of Solitude", "Gabriel Garcia Marquez", 417));
        library.registerItem(new Magazine("M001", "National Geographic", "2024-07"));
        library.registerItem(new Magazine("M002", "Scientific American", "2024-06"));
        library.registerItem(new DVD("D001", "The Godfather", 175));
        library.registerItem(new DVD("D002", "Casablanca", 102));

        library.registerMember(new Member("Ahmed Hassan", "MEM001", MembershipType.STUDENT));
        library.registerMember(new Member("Dr. Sara Mostafa", "MEM002", MembershipType.FACULTY));
        library.registerMember(new Member("Omar Khalil", "MEM003", MembershipType.COMMUNITY, 45.00));
    }

    private static void registerMember(Library library) {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter membership ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter category (STUDENT / FACULTY / COMMUNITY): ");
        String categoryInput = scanner.nextLine().trim().toUpperCase();

        MembershipType category;
        try {
            category = MembershipType.valueOf(categoryInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category. Use STUDENT, FACULTY, or COMMUNITY.");
            return;
        }

        boolean ok = library.registerMember(new Member(name, id, category));
        if (ok) {
            System.out.println("Member '" + name + "' registered successfully with ID " + id + ".");
        } else {
            System.out.println("Registration failed. The membership ID may already be taken or the register is full.");
        }
    }

    private static void borrowItem(Library library) {
        System.out.print("Enter item catalogue ID: ");
        String itemId = scanner.nextLine().trim();
        System.out.print("Enter membership ID: ");
        String memberId = scanner.nextLine().trim();

        library.lendItem(itemId, memberId);
    }

    private static void returnItem(Library library) {
        System.out.print("Enter item catalogue ID: ");
        String itemId = scanner.nextLine().trim();
        System.out.print("Enter days overdue (0 for on time): ");
        String daysInput = scanner.nextLine().trim();

        try {
            int days = Integer.parseInt(daysInput);
            library.returnItem(itemId, days);
        } catch (NumberFormatException e) {
            System.out.println("Days overdue must be a whole number.");
        }
    }

    private static void renewLoan(Library library) {
        System.out.print("Enter item catalogue ID: ");
        String itemId = scanner.nextLine().trim();

        library.renewItem(itemId);
    }

    private static void searchItem(Library library) {
        System.out.print("Enter item catalogue ID: ");
        String itemId = scanner.nextLine().trim();

        LibraryItem item = library.findItem(itemId);
        if (item == null) {
            System.out.println("Item with ID '" + itemId + "' was not found in the catalogue.");
            return;
        }
        item.display();
    }

    private static void viewItemsByStatus(Library library) {
        System.out.print("Enter status (AVAILABLE / ON_LOAN / RESERVED / LOST): ");
        String statusInput = scanner.nextLine().trim().toUpperCase();

        ItemStatus status;
        try {
            status = ItemStatus.valueOf(statusInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Use AVAILABLE, ON_LOAN, RESERVED, or LOST.");
            return;
        }

        library.listItemsByStatus(status);
    }

    private static void payFines(Library library) {
        System.out.print("Enter membership ID: ");
        String memberId = scanner.nextLine().trim();
        System.out.print("Enter payment amount (EGP): ");
        String amountInput = scanner.nextLine().trim();

        Member member = library.findMember(memberId);
        if (member == null) {
            System.out.println("Member not found in the register.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountInput);
            boolean ok = member.makePayment(amount);
            if (ok) {
                System.out.printf("Payment of %.2f EGP accepted. New balance: %.2f EGP.%n",
                    amount, member.getBalanceOwed());
            } else {
                System.out.println("Payment rejected. The amount must be positive and no more than the balance owed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Payment amount must be a number.");
        }
    }
}