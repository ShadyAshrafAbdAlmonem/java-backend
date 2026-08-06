public abstract class Account {
    protected static final double MIN_TRANSACTION_AMOUNT = 1.0;

    private final String accountNumber;
    private final Customer owner;
    private double balance;
    private AccountStatus status;
    private int transactionCount;

    public Account(String accountNumber, Customer owner, double initialBalance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVE;
        this.transactionCount = 0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public abstract AccountType getType();

    public abstract TransactionResult withdraw(double amount);

    public boolean canClose() {
        return Math.abs(balance) <= 0.00001;
    }

    public TransactionResult canTransact() {
        if (status == AccountStatus.FROZEN)
            return TransactionResult.ACCOUNT_FROZEN;
        if (status == AccountStatus.CLOSED)
            return TransactionResult.ACCOUNT_CLOSED;
        return TransactionResult.SUCCESS;
    }

    protected TransactionResult validateTransaction(double amount) {
        TransactionResult transactResult = canTransact();
        if (transactResult != TransactionResult.SUCCESS)
            return transactResult;
        if (amount < MIN_TRANSACTION_AMOUNT)
            return TransactionResult.INVALID_AMOUNT;
        return TransactionResult.SUCCESS;
    }

    public TransactionResult deposit(double amount) {
        TransactionResult validation = validateTransaction(amount);
        if (validation != TransactionResult.SUCCESS)
            return validation;
        this.balance += amount;
        this.transactionCount++;
        return TransactionResult.SUCCESS;
    }

    protected void incrementTransactionCount() {
        this.transactionCount++;
    }

    public String getDetails() {
        return String.format("Acc #: %-10s | Type: %-13s | Owner: %-15s | Balance: $%-10.2f | Status: %-6s | Txns: %d",
                accountNumber, getType(), owner.getFullName(), balance, status, transactionCount);
    }
}