import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        final int CORRECT_PIN = 1234;
        int pinAttempts = 3;
        boolean isAuthenticated = false;
        
        double balance = 2500.75;
        int successfulTransactions = 0; 

        System.out.println("=== Welcome to the ATM ===");
        do {
            System.out.print("Please enter your 4-digit PIN: ");
            int enteredPin = scanner.nextInt();
            
            if (enteredPin == CORRECT_PIN) {
                isAuthenticated = true;
                System.out.println("Login successful!\n");
            } else {
                pinAttempts--;
                if (pinAttempts > 0) {
                    System.out.println("Incorrect PIN. You have " + pinAttempts + " attempts remaining.");
                } else {
                    System.out.println("Your account has been locked.");
                    return; 
                }
            }
        } while (!isAuthenticated && pinAttempts > 0);

        while (isAuthenticated) {
            System.out.println("========= ATM =========");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Show Account Status");
            System.out.println("5. Exit");
            System.out.println("=======================");
            System.out.print("Choose an option (1-5): ");
            
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.printf("Your current balance is: $%.2f%n\n", balance);
                    break;
                    
                case 2:
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    
                    if (depositAmount > 0) {
                        balance += depositAmount;
                        successfulTransactions++;
                        System.out.printf("Successfully deposited: $%.2f%n", depositAmount);
                        System.out.printf("Updated balance: $%.2f%n\n", balance); 
                    } else {
                        System.out.println("Invalid amount.\n");
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    
                    if (withdrawAmount == 0) {
                        System.out.println("Transaction cancelled.\n");
                    } else if (withdrawAmount < 0) {
                        System.out.println("Invalid amount.\n");
                    } else if (withdrawAmount > balance) {
                        System.out.println("Insufficient balance.\n");
                    } else {
                        balance -= withdrawAmount;
                        successfulTransactions++;
                        System.out.printf("Successfully withdrew: $%.2f%n", withdrawAmount);
                        System.out.printf("Updated balance: $%.2f%n", balance); 
                        
                        if (balance == 0) {
                            System.out.println("Warning: Your account is empty.");
                        }
                        System.out.println();
                    }
                    break;
                    
                case 4:
                    System.out.print("Account Status: ");
                    if (balance >= 5000) {
                        System.out.println("VIP Customer");
                    } else if (balance >= 1000) {
                        System.out.println("Regular Customer");
                    } else {
                        System.out.println("Low Balance");
                    }
                    System.out.println(); 
                    break;
                    
                case 5:
                    System.out.println("Thank you for using our ATM.");
                    System.out.println("Total successful transactions: " + successfulTransactions); 
                    break; 
                    
                default:
                    System.out.println("Invalid option.\n");
            }
            
            if (choice == 5) {
                break;
            }
        }
        
        scanner.close();
    }
}