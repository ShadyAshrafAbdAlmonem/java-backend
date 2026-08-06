public class CurrentAccount extends Account {
    private final double overdraftLimit;

    public CurrentAccount(String accountNumber, Customer owner, double initialBalance, double overdraftLimit) {
        super(accountNumber, owner, initialBalance);
        this.overdraftLimit = Math.abs(overdraftLimit);
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public boolean isUsingOverdraft() {
        return getBalance() < 0;
    }

    @Override
    public AccountType getType() {
        return AccountType.CURRENT;
    }

    @Override
    public TransactionResult withdraw(double amount) {
        TransactionResult validation = validateTransaction(amount);
        if (validation != TransactionResult.SUCCESS)
            return validation;
        if (getBalance() - amount < -overdraftLimit) {
            return TransactionResult.OVERDRAFT_LIMIT_EXCEEDED;
        }

        setBalance(getBalance() - amount);
        incrementTransactionCount();
        return TransactionResult.SUCCESS;
    }

    @Override
    public String getDetails() {
        return super.getDetails() + String.format(" | Overdraft Limit: $%.2f | In Overdraft: %s",
                overdraftLimit, isUsingOverdraft() ? "YES" : "NO");
    }
}