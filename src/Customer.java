public class Customer {
    private int id;
    private String name;
    private String phone;
    private int rentedCarId;
    private int numberOfRentedDays;
    private double totalPaid;

    private static int customerCount = 0;

    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.rentedCarId = -1; 
        this.numberOfRentedDays = 0;
        this.totalPaid = 0.0;
        customerCount++;
    }

    public void rentCar(int carId, int days, double amountPaid) {
        this.rentedCarId = carId;
        this.numberOfRentedDays = days;
        this.totalPaid += amountPaid;
    }

    public void returnCar() {
        this.rentedCarId = -1;
        this.numberOfRentedDays = 0;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public int getRentedCarId() {
        return this.rentedCarId;
    }

    public int getNumberOfRentedDays() {
        return this.numberOfRentedDays;
    }

    public double getTotalPaid() {
        return this.totalPaid;
    }

    public static int getCustomerCount() {
        return customerCount;
    }

    public String getDetails() {
        String status = (rentedCarId == -1) ? "None" : "Car ID " + rentedCarId + " (" + numberOfRentedDays + " days)";
        return String.format("Customer ID: %d | Name: %s | Phone: %s | Rented Car: %s | Total Paid: $%.2f",
                this.id, this.name, this.phone, status, this.totalPaid);
    }
}