public class SavingsAccount extends Account {
    private final double annualInterestRate;
    private int monthlyWithdrawalCount;

    public SavingsAccount(String accountNumber, Customer owner, double initialBalance, double annualInterestRate) {
        super(accountNumber, owner, initialBalance);
        this.annualInterestRate = annualInterestRate;
        this.monthlyWithdrawalCount = 0;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public int getMonthlyWithdrawalCount() {
        return monthlyWithdrawalCount;
    }

    public void resetMonthlyWithdrawalCount() {
        this.monthlyWithdrawalCount = 0;
    }

    @Override
    public AccountType getType() {
        return AccountType.SAVINGS;
    }

    @Override
    public TransactionResult withdraw(double amount) {
        TransactionResult validation = validateTransaction(amount);
        if (validation != TransactionResult.SUCCESS)
            return validation;
        if (getBalance() - amount < 0) {
            return TransactionResult.INSUFFICIENT_FUNDS;
        }

        setBalance(getBalance() - amount);
        monthlyWithdrawalCount++;
        incrementTransactionCount();
        return TransactionResult.SUCCESS;
    }

    @Override
    public String getDetails() {
        return super.getDetails() + String.format(" | Rate: %.2f%% | Monthly Withdrawals: %d",
                annualInterestRate + (getOwner().getTier().getInterestBonus() * 100), monthlyWithdrawalCount);
    }
}