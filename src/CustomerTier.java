
public enum CustomerTier {
    STANDARD(0.00, 0.00),
    SILVER(5.00, 0.01),
    GOLD(10.00, 0.02);

    private final double monthlyFee;
    private final double interestBonus;

    CustomerTier(double monthlyFee, double interestBonus) {
        this.monthlyFee = monthlyFee;
        this.interestBonus = interestBonus;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public double getInterestBonus() {
        return interestBonus;
    }
}