public class BankService {
    private final CustomerRepository customerRepo;
    private final AccountRepository accountRepo;

    public static final int MAX_CUSTOMERS = 100;
    public static final int MAX_ACCOUNTS = 300;

    private static final double MIN_SAVINGS_OPENING_BALANCE = 50.0;
    private static final double MIN_CURRENT_OPENING_BALANCE = 100.0;
    private static final double MIN_FIXED_OPENING_BALANCE = 500.0;
    private static final String PHONE_NUMBER_REGEX = "\\d{7,15}";

    public BankService(int maxCustomers, int maxAccounts) {
        this.customerRepo = new CustomerRepository(maxCustomers);
        this.accountRepo = new AccountRepository(maxAccounts);
    }

    public void registerCustomer() {
        if (customerRepo.isFull()) {
            System.out.println("Error: Customer database capacity reached.");
            return;
        }

        String name = InputUtil.readString("Enter Full Name: ");
        if (name.isEmpty()) {
            System.out.println("Error: Customer name cannot be empty.");
            return;
        }

        String nationalId = InputUtil.readString("Enter National ID: ");
        if (nationalId.isEmpty()) {
            System.out.println("Error: National ID cannot be empty.");
            return;
        }
        if (customerRepo.findByNationalId(nationalId) != null) {
            System.out.println("Error: A customer with National ID '" + nationalId + "' already exists.");
            return;
        }

        String phone = InputUtil.readString("Enter Phone Number (Optional, Press Enter to skip): ");
        if (!phone.isEmpty() && !phone.matches(PHONE_NUMBER_REGEX)) {
            System.out.println("Error: Phone number must contain only digits and be between 7 to 15 digits.");
            return;
        }

        System.out.println("Select Customer Tier: 1. STANDARD  2. SILVER  3. GOLD");
        int tierChoice = InputUtil.readInt("Choice: ");
        CustomerTier tier = switch (tierChoice) {
            case 2 -> CustomerTier.SILVER;
            case 3 -> CustomerTier.GOLD;
            default -> CustomerTier.STANDARD;
        };

        int id = customerRepo.generateCustomerId();
        Customer customer = new Customer(id, name, nationalId, phone, tier);
        if (!customerRepo.addCustomer(customer)) {
            System.out.println("Error: Failed to add customer to repository (capacity issue).");
            return;
        }

        System.out.println("SUCCESS: Customer registered successfully.");
        System.out.println("Generated Customer ID: " + id);
    }

    public void openAccount() {
        if (accountRepo.isFull()) {
            System.out.println("Error: Account database capacity reached.");
            return;
        }

        int custId = InputUtil.readInt("Enter Customer ID: ");
        Customer owner = customerRepo.findById(custId);
        if (owner == null) {
            System.out.println("Error: Customer ID " + custId + " not found.");
            return;
        }

        System.out.println("Select Account Type: 1. Savings  2. Current  3. Fixed Deposit");
        int typeChoice = InputUtil.readInt("Choice: ");

        double deposit = InputUtil.readDouble("Enter Initial Deposit: $");

        Account newAccount = null;
        switch (typeChoice) {
            case 1 -> {
                if (deposit < MIN_SAVINGS_OPENING_BALANCE) {
                    System.out.println("Error: Minimum opening balance for Savings is $" + MIN_SAVINGS_OPENING_BALANCE);
                    return;
                }
                double rate = InputUtil.readDouble("Enter Annual Interest Rate (%): ");
                String accNum = accountRepo.generateAccountNumber(AccountType.SAVINGS);
                newAccount = new SavingsAccount(accNum, owner, deposit, rate);
            }
            case 2 -> {
                if (deposit < MIN_CURRENT_OPENING_BALANCE) {
                    System.out.println("Error: Minimum opening balance for Current is $" + MIN_CURRENT_OPENING_BALANCE);
                    return;
                }
                double limit = InputUtil.readDouble("Enter Overdraft Limit: $");
                String accNum = accountRepo.generateAccountNumber(AccountType.CURRENT);
                newAccount = new CurrentAccount(accNum, owner, deposit, limit);
            }
            case 3 -> {
                if (deposit < MIN_FIXED_OPENING_BALANCE) {
                    System.out.println(
                            "Error: Minimum opening balance for Fixed Deposit is $" + MIN_FIXED_OPENING_BALANCE);
                    return;
                }
                double rate = InputUtil.readDouble("Enter Annual Interest Rate (%): ");
                int duration = InputUtil.readInt("Enter Duration (in months): ");
                String accNum = accountRepo.generateAccountNumber(AccountType.FIXED_DEPOSIT);
                newAccount = new FixedDepositAccount(accNum, owner, deposit, rate, duration);
            }
            default -> {
                System.out.println("Error: Invalid account type selected.");
                return;
            }
        }

        if (!accountRepo.addAccount(newAccount)) {
            System.out.println("Error: Failed to add account to repository (capacity issue).");
            return;
        }
        owner.incrementAccountCount();
        System.out.println("SUCCESS: Account opened successfully!");
        System.out.println("Account Number: " + newAccount.getAccountNumber());
    }

    public void depositMoney() {
        String accNum = InputUtil.readString("Enter Account Number: ");
        Account acc = accountRepo.findByNumber(accNum);
        if (acc == null) {
            System.out.println("Error: Account not found.");
            return;
        }

        double amount = InputUtil.readDouble("Enter Deposit Amount: $");

        double oldBal = acc.getBalance();
        TransactionResult result = acc.deposit(amount);
        if (result == TransactionResult.SUCCESS) {
            System.out.printf("SUCCESS: Current balance = $%.2f. Deposit = $%.2f. New balance = $%.2f%n",
                    oldBal, amount, acc.getBalance());
        } else {
            System.out.print("Error: Deposit failed. ");
            switch (result) {
                case INVALID_AMOUNT ->
                    System.out.println("Deposit amount must be at least $" + Account.MIN_TRANSACTION_AMOUNT + ".");
                case ACCOUNT_FROZEN -> System.out.println("Account " + acc.getAccountNumber() + " is FROZEN.");
                case ACCOUNT_CLOSED -> System.out.println("Account " + acc.getAccountNumber() + " is CLOSED.");
                default -> System.out.println("An unexpected error occurred.");
            }
        }
    }

    public void withdrawMoney() {
        String accNum = InputUtil.readString("Enter Account Number: ");
        Account acc = accountRepo.findByNumber(accNum);
        if (acc == null) {
            System.out.println("Error: Account not found.");
            return;
        }

        double amount = InputUtil.readDouble("Enter Withdrawal Amount: $");

        double oldBal = acc.getBalance();
        TransactionResult result = acc.withdraw(amount);
        if (result == TransactionResult.SUCCESS) {
            System.out.printf("SUCCESS: Previous balance = $%.2f. Withdrawn = $%.2f. New balance = $%.2f%n",
                    oldBal, amount, acc.getBalance());
        } else {
            System.out.print("Error: Withdrawal failed. ");
            switch (result) {
                case INVALID_AMOUNT ->
                    System.out.println("Withdrawal amount must be at least $" + Account.MIN_TRANSACTION_AMOUNT + ".");
                case INSUFFICIENT_FUNDS -> System.out.println("Insufficient funds. Available: $" + acc.getBalance());
                case OVERDRAFT_LIMIT_EXCEEDED -> {
                    CurrentAccount ca = (CurrentAccount) acc;
                    System.out.printf("Overdraft limit exceeded. Limit: -$%.2f, Potential balance: $%.2f%n",
                            ca.getOverdraftLimit(), ca.getBalance() - amount);
                }
                case NOT_MATURED -> {
                    FixedDepositAccount fd = (FixedDepositAccount) acc;
                    int remaining = fd.getDurationMonths() - fd.getElapsedMonths();
                    System.out.println("Fixed Deposit account has not matured yet! Remaining months: " + remaining);
                }
                case ACCOUNT_FROZEN -> System.out.println("Account " + acc.getAccountNumber() + " is FROZEN.");
                case ACCOUNT_CLOSED -> System.out.println("Account " + acc.getAccountNumber() + " is CLOSED.");
                default -> System.out.println("An unexpected error occurred.");
            }
        }
    }

    public void transferMoney() {
        String srcNum = InputUtil.readString("Enter Source Account Number: ");
        Account src = accountRepo.findByNumber(srcNum);
        if (src == null) {
            System.out.println("Error: Source account not found.");
            return;
        }

        String destNum = InputUtil.readString("Enter Destination Account Number: ");
        Account dest = accountRepo.findByNumber(destNum);
        if (dest == null) {
            System.out.println("Error: Destination account not found.");
            return;
        }

        if (src.getAccountNumber().equalsIgnoreCase(dest.getAccountNumber())) {
            System.out.println("Error: Source and Destination accounts must be different.");
            return;
        }

        double amount = InputUtil.readDouble("Enter Transfer Amount: $");

        TransactionResult srcWithdrawResult = src.withdraw(amount);
        if (srcWithdrawResult == TransactionResult.SUCCESS) {
            TransactionResult destDepositResult = dest.deposit(amount);
            if (destDepositResult == TransactionResult.SUCCESS) {
                System.out.printf("SUCCESS: Transferred $%.2f from %s to %s.%n", amount, src.getAccountNumber(),
                        dest.getAccountNumber());
            } else {
                TransactionResult rollbackResult = src.deposit(amount);
                if (rollbackResult == TransactionResult.SUCCESS) {
                    System.out.print("CRITICAL ERROR: Destination deposit failed. ");
                    switch (destDepositResult) {
                        case INVALID_AMOUNT -> System.out.println("Deposit amount for destination was invalid.");
                        case ACCOUNT_FROZEN ->
                            System.out.println("Destination account " + dest.getAccountNumber() + " is FROZEN.");
                        case ACCOUNT_CLOSED ->
                            System.out.println("Destination account " + dest.getAccountNumber() + " is CLOSED.");
                        default -> System.out.println("An unexpected error occurred during destination deposit.");
                    }
                    System.out.println("Transfer cancelled and funds restored to source.");
                } else {
                    System.out.println(
                            "CRITICAL ERROR: Destination deposit failed AND rollback to source failed. Funds may be inconsistent.");
                    System.out.println("Rollback failure reason: " + rollbackResult);
                }
            }
        } else {
            System.out.print("Error: Transfer failed from source account. ");
            switch (srcWithdrawResult) {
                case INVALID_AMOUNT -> System.out.println(
                        "Withdrawal amount from source must be at least $" + Account.MIN_TRANSACTION_AMOUNT + ".");
                case INSUFFICIENT_FUNDS ->
                    System.out.println("Insufficient funds in source account. Available: $" + src.getBalance());
                case OVERDRAFT_LIMIT_EXCEEDED -> {
                    CurrentAccount ca = (CurrentAccount) src;
                    System.out.printf("Source overdraft limit exceeded. Limit: -$%.2f, Potential balance: $%.2f%n",
                            ca.getOverdraftLimit(), ca.getBalance() - amount);
                }
                case NOT_MATURED -> {
                    FixedDepositAccount fd = (FixedDepositAccount) src;
                    int remaining = fd.getDurationMonths() - fd.getElapsedMonths();
                    System.out.println(
                            "Source Fixed Deposit account has not matured yet! Remaining months: " + remaining);
                }
                case ACCOUNT_FROZEN -> System.out.println("Source account " + src.getAccountNumber() + " is FROZEN.");
                case ACCOUNT_CLOSED -> System.out.println("Source account " + src.getAccountNumber() + " is CLOSED.");
                default -> System.out.println("An unexpected error occurred during source withdrawal.");
            }
        }
    }

    public void displayCustomerAccounts() {
        int custId = InputUtil.readInt("Enter Customer ID: ");
        Customer customer = customerRepo.findById(custId);
        if (customer == null) {
            System.out.println("Error: Customer ID not found.");
            return;
        }

        System.out.println("\n---------------- CUSTOMER DETAILS ----------------");
        System.out.println(customer);
        System.out.println("--------------------------------------------------");

        Account[] custAccounts = accountRepo.findByCustomer(custId);
        if (custAccounts.length == 0) {
            System.out.println("No accounts owned by this customer.");
            return;
        }

        double totalCombinedBalance = 0.0;
        for (Account acc : custAccounts) {
            System.out.println(acc.getDetails());
            totalCombinedBalance += acc.getBalance();
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Combined Total Balance across all accounts: $%.2f%n", totalCombinedBalance);
    }

    public void displayAllBranchAccounts() {
        Account[] all = accountRepo.getAll();
        if (all.length == 0) {
            System.out.println("No accounts registered in the branch.");
            return;
        }

        System.out.println("\n================ BRANCH ACCOUNTS SYSTEM RECORD ================");
        for (Account acc : all) {
            System.out.println(acc.getDetails());
        }
        System.out.println("===============================================================");
    }

    public void searchAccountByNumber() {
        String accNum = InputUtil.readString("Enter Account Number to Search: ");
        Account acc = accountRepo.findByNumber(accNum);
        if (acc == null) {
            System.out.println("Error: Account number not found.");
            return;
        }

        System.out.println("\n---------------- ACCOUNT FOUND ----------------");
        System.out.println(acc.getDetails());
        System.out.println("Owner Info: " + acc.getOwner());
        System.out.println("------------------------------------------------");
    }

    public void searchAccountsByType() {
        System.out.println("Select Type: 1. SAVINGS  2. CURRENT  3. FIXED_DEPOSIT");
        int typeChoice = InputUtil.readInt("Choice: ");
        AccountType type = switch (typeChoice) {
            case 1 -> AccountType.SAVINGS;
            case 2 -> AccountType.CURRENT;
            case 3 -> AccountType.FIXED_DEPOSIT;
            default -> null;
        };

        if (type == null) {
            System.out.println("Error: Invalid Account Type.");
            return;
        }

        Account[] matches = accountRepo.findByType(type);
        if (matches.length == 0) {
            System.out.println("No accounts found for type: " + type);
            return;
        }

        System.out.println("\n---------------- " + type + " ACCOUNTS ----------------");
        double totalBalance = 0;
        for (Account acc : matches) {
            System.out.println(acc.getDetails());
            totalBalance += acc.getBalance();
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Total Accounts Matching: %d | Combined Balance: $%.2f%n", matches.length, totalBalance);
    }

    public void closeAccount() {
        String accNum = InputUtil.readString("Enter Account Number to Close: ");
        Account acc = accountRepo.findByNumber(accNum);
        if (acc == null) {
            System.out.println("Error: Account not found.");
            return;
        }

        if (acc.getStatus() == AccountStatus.CLOSED) {
            System.out.println("Error: Account is already closed.");
            return;
        }

        if (!acc.canClose()) {
            if (acc instanceof FixedDepositAccount fd && !fd.isMatured()) {
                System.out.println("Error: Cannot close an unmatured Fixed Deposit account.");
            } else {
                System.out.printf("Error: Cannot close account. Balance must be $0.00. (Current Balance: $%.2f)%n",
                        acc.getBalance());
            }
            return;
        }

        acc.setStatus(AccountStatus.CLOSED);
        acc.getOwner().decrementAccountCount();
        System.out.println("SUCCESS: Account " + accNum + " has been successfully CLOSED.");
    }
}