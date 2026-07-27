public class LuxuryCar extends Car {
    private double insuranceFee;
    public static final int MIN_RENTAL_DAYS = 3;

    public LuxuryCar(int id, String brand, String model, int year, double pricePerDay, double insuranceFee) {
        super(id, brand, model, year, pricePerDay);
        this.insuranceFee = insuranceFee;
    }

    @Override
    public double calculateRentalCost(int days) {
        double baseCost = (days * getPricePerDay()) + this.insuranceFee;
        return baseCost + (baseCost * TAX_RATE);
    }

    public double getInsuranceFee() {
        return this.insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    @Override
    public String getDetails() {
        return super.getDetails() + String.format(" | [LUXURY - Insurance: $%.2f | Min Days: %d]", 
                this.insuranceFee, MIN_RENTAL_DAYS);
    }
}