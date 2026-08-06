public class Customer {
    private final int customerId;
    private final String fullName;
    private final String nationalId;
    private String phoneNumber;
    private CustomerTier tier;
    private int accountCount;

    public Customer(int customerId, String fullName, String nationalId, String phoneNumber, CustomerTier tier) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
        this.tier = tier;
        this.accountCount = 0;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CustomerTier getTier() {
        return tier;
    }

    public int getAccountCount() {
        return accountCount;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTier(CustomerTier tier) {
        this.tier = tier;
    }

    public void incrementAccountCount() {
        this.accountCount++;
    }

    public void decrementAccountCount() {
        if (this.accountCount > 0)
            this.accountCount--;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | National ID: %s | Phone: %s | Tier: %s | Accounts Owned: %d",
                customerId, fullName, nationalId, (phoneNumber.isEmpty() ? "N/A" : phoneNumber), tier, accountCount);
    }
}