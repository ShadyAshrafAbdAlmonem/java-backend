public class Magazine extends LibraryItem implements Renewable {
    private static final int LOAN_PERIOD = 7;
    private static final double DAILY_FINE = 3.00;
    private static final double MAX_FINE = 30.00;
    private static final int RENEWAL_LIMIT = 1;

    private final String issueNumber;

    public Magazine(String catalogueId, String title, String issueNumber) {
        super(catalogueId, title);
        this.issueNumber = issueNumber;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    @Override
    public double calculateFine(int daysLate) {
        double fine = daysLate * DAILY_FINE;
        return Math.min(fine, MAX_FINE);
    }

    @Override
    public int getLoanPeriod() {
        return LOAN_PERIOD;
    }

    @Override
    public String getCategoryName() {
        return "Magazine";
    }

    @Override
    public boolean renew() {
        if (getRenewalCount() >= RENEWAL_LIMIT) {
            return false;
        }
        return recordRenewal();
    }

    @Override
    public int getRenewalLimit() {
        return RENEWAL_LIMIT;
    }
}