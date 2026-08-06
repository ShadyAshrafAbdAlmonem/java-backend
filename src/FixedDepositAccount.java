public class FixedDepositAccount extends Account {
    private final double interestRate;
    private final int durationMonths;
    private int elapsedMonths;

    public FixedDepositAccount(String accountNumber, Customer owner, double initialBalance, double interestRate,
            int durationMonths) {
        super(accountNumber, owner, initialBalance);
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.elapsedMonths = 0;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public int getElapsedMonths() {
        return elapsedMonths;
    }

    public void advanceMonths(int monthsToAdvance) {
        if (monthsToAdvance > 0) {
            this.elapsedMonths = Math.min(this.durationMonths, this.elapsedMonths + monthsToAdvance);
        }
    }

    public boolean isMatured() {
        return elapsedMonths >= durationMonths;
    }

    @Override
    public AccountType getType() {
        return AccountType.FIXED_DEPOSIT;
    }

    @Override
    public TransactionResult withdraw(double amount) {
        TransactionResult validation = validateTransaction(amount);
        if (validation != TransactionResult.SUCCESS)
            return validation;
        if (!isMatured()) {
            return TransactionResult.NOT_MATURED;
        }
        if (getBalance() - amount < 0) {
            return TransactionResult.INSUFFICIENT_FUNDS;
        }

        setBalance(getBalance() - amount);
        incrementTransactionCount();
        return TransactionResult.SUCCESS;
    }

    @Override
    public boolean canClose() {
        if (!isMatured()) {
            return false;
        }
        return super.canClose();
    }

    @Override
    public String getDetails() {
        return super.getDetails() + String.format(" | Term: %d mos | Elapsed: %d mos | Matured: %s",
                durationMonths, elapsedMonths, isMatured() ? "YES" : "NO");
    }
}