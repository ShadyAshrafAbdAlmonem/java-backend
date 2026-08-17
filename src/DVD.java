public class DVD extends LibraryItem {
    private static final int LOAN_PERIOD = 3;
    private static final double DAILY_FINE = 15.00;

    private final int runtimeMinutes;

    public DVD(String catalogueId, String title, int runtimeMinutes) {
        super(catalogueId, title);
        this.runtimeMinutes = runtimeMinutes;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
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
        return "DVD";
    }
}