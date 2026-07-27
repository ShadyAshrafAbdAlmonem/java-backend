public class Car {
    private int id;
    private String brand;
    private String model;
    private int year;
    private double pricePerDay;
    private boolean available;

    private static int carCount = 0;
    public static final double TAX_RATE = 0.14;

    public Car(int id, String brand, String model, int year, double pricePerDay) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.pricePerDay = pricePerDay;
        this.available = true; 
        carCount++;
    }

    public Car(int id, String brand, String model, int year) {
        this(id, brand, model, year, 50.0);
    }

    public double calculateRentalCost(int days) {
        double baseCost = days * this.pricePerDay;
        return baseCost + (baseCost * TAX_RATE);
    }

    public int getId() {
        return this.id;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public int getYear() {
        return this.year;
    }

    public double getPricePerDay() {
        return this.pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public static int getCarCount() {
        return carCount;
    }

    public String getDetails() {
        return String.format("ID: %d | %d %s %s | Price/Day: $%.2f | Status: %s",
                this.id, this.year, this.brand, this.model, this.pricePerDay,
                (this.available ? "AVAILABLE" : "RENTED"));
    }
}