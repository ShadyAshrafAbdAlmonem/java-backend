public class Member {
    private static final int MAX_ITEMS_HELD = 3;
    private static final double MAX_OWED_TO_BORROW = 100.0;

    private String name;
    private final String membershipId;
    private final MembershipType category;
    private double balanceOwed;
    private int itemsHeld;

    public Member(String name, String membershipId, MembershipType category) {
        this(name, membershipId, category, 0.0);
    }

    public Member(String name, String membershipId, MembershipType category, double startingBalance) {
        this.name = name;
        this.membershipId = membershipId;
        this.category = category;
        this.balanceOwed = startingBalance;
        this.itemsHeld = 0;
    }

    public String getName() {
        return name;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public MembershipType getCategory() {
        return category;
    }

    public double getBalanceOwed() {
        return balanceOwed;
    }

    public int getItemsHeld() {
        return itemsHeld;
    }

    public void correctName(String newName) {
        this.name = newName;
    }

    public boolean chargeFine(double amount) {
        if (amount <= 0) {
            return false;
        }
        balanceOwed += amount;
        return true;
    }

    public boolean makePayment(double amount) {
        if (amount <= 0 || amount > balanceOwed) {
            return false;
        }
        balanceOwed -= amount;
        return true;
    }

    public boolean canBorrow() {
        return itemsHeld < MAX_ITEMS_HELD && balanceOwed <= MAX_OWED_TO_BORROW;
    }

    public void recordBorrowing() {
        itemsHeld++;
    }

    public void recordReturn() {
        if (itemsHeld > 0) {
            itemsHeld--;
        }
    }
}