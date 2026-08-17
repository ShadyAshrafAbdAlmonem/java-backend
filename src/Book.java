public class Book extends LibraryItem implements Renewable {
    private static final int LOAN_PERIOD = 14;
    private static final double DAILY_FINE = 5.00;
    private static final int RENEWAL_LIMIT = 2;

    private final String author;
    private final int pageCount;

    public Book(String catalogueId, String title, String author, int pageCount) {
        super(catalogueId, title);
        this.author = author;
        this.pageCount = pageCount;
    }

    public String getAuthor() {
        return author;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * DAILY_FINE;
    }

    @Override
    public int getLoanPeriod() {
        return LOAN_PERIOD;
    }

    @Override
    public String getCategoryName() {
        return "Book";
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