public class AccountRepository {
    private final Account[] accounts;
    private int count;
    private int nextAccountSeq;

    public AccountRepository(int capacity) {
        this.accounts = new Account[capacity];
        this.count = 0;
        this.nextAccountSeq = 5001;
    }

    public boolean isFull() {
        return count >= accounts.length;
    }

    public String generateAccountNumber(AccountType type) {
        String prefix = switch (type) {
            case SAVINGS -> "SAV";
            case CURRENT -> "CUR";
            case FIXED_DEPOSIT -> "FXD";
        };
        return prefix + "-" + (nextAccountSeq++);
    }

    public boolean addAccount(Account account) {
        if (isFull()) {
            return false;
        }
        accounts[count++] = account;
        return true;
    }

    public Account findByNumber(String accountNumber) {
        if (accountNumber == null)
            return null;
        for (int i = 0; i < count; i++) {
            if (accounts[i].getAccountNumber().equalsIgnoreCase(accountNumber.trim())) {
                return accounts[i];
            }
        }
        return null;
    }

    public Account[] findByCustomer(int customerId) {
        int matchCount = 0;
        for (int i = 0; i < count; i++) {
            if (accounts[i].getOwner().getCustomerId() == customerId) {
                matchCount++;
            }
        }

        Account[] result = new Account[matchCount];
        int idx = 0;
        for (int i = 0; i < count; i++) {
            if (accounts[i].getOwner().getCustomerId() == customerId) {
                result[idx++] = accounts[i];
            }
        }
        return result;
    }

    public Account[] findByType(AccountType type) {
        int matchCount = 0;
        for (int i = 0; i < count; i++) {
            if (accounts[i].getType() == type) {
                matchCount++;
            }
        }

        Account[] result = new Account[matchCount];
        int idx = 0;
        for (int i = 0; i < count; i++) {
            if (accounts[i].getType() == type) {
                result[idx++] = accounts[i];
            }
        }
        return result;
    }

    public Account[] getAll() {
        Account[] copy = new Account[count];
        System.arraycopy(accounts, 0, copy, 0, count);
        return copy;
    }
}