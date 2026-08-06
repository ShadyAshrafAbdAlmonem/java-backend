

public class Main {
    public static void main(String[] args) {
        BankService bankService = new BankService(BankService.MAX_CUSTOMERS, BankService.MAX_ACCOUNTS); 

        boolean running = true;
        while (running) {
            printMenu();
            int option = InputUtil.readInt("Select an option [0-10]: ");
            System.out.println();

            switch (option) {
                case 1 -> bankService.registerCustomer();
                case 2 -> bankService.openAccount();
                case 3 -> bankService.depositMoney();
                case 4 -> bankService.withdrawMoney();
                case 5 -> bankService.transferMoney();
                case 6 -> bankService.displayCustomerAccounts();
                case 7 -> bankService.displayAllBranchAccounts();
                case 8 -> bankService.searchAccountByNumber();
                case 9 -> bankService.searchAccountsByType();
                case 10 -> bankService.closeAccount();
                case 0 -> {
                    System.out.println("Thank you for using Al Manara Bank Management System. Goodbye!");
                InputUtil.closeScanner(); 
                    running = false;
                }
                default -> System.out.println("Error: Invalid choice. Please select an option between 0 and 10.");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("==================================================");
        System.out.println("             AL MANARA BANK - MAIN MENU            ");
        System.out.println("==================================================");
        System.out.println("Option  Operation");
        System.out.println("  1     Register New Customer");
        System.out.println("  2     Open New Account");
        System.out.println("  3     Deposit Money");
        System.out.println("  4     Withdraw Money");
        System.out.println("  5     Transfer Between Accounts");
        System.out.println("  6     Display Customer Accounts");
        System.out.println("  7     Display All Branch Accounts");
        System.out.println("  8     Search Account by Number");
        System.out.println("  9     Search Accounts by Type");
        System.out.println("  10    Close an Account");
        System.out.println("  0     Exit");
        System.out.println("==================================================");
    }
}