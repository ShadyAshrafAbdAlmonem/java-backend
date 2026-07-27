import java.util.Scanner;

public class Main {
    private static final int MAX_CARS = 20;
    private static final int MAX_CUSTOMERS = 20;

    private static Car[] cars = new Car[MAX_CARS];
    private static int carCount = 0;

    private static Customer[] customers = new Customer[MAX_CUSTOMERS];
    private static int customerCount = 0;

    private static double totalOfficeIncome = 0.0;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printWelcomeBanner();

        int choice = -1;
        while (choice != 0) {
            displayMenu();
            System.out.print("Enter your choice: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("\n[ERROR] Invalid input. Please enter a valid menu number.\n");
                scanner.next(); 
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            System.out.println();
            switch (choice) {
                case 1:
                    addCar(false); 
                    break;
                case 2:
                    addCar(true);  
                    break;
                case 3:
                    addCustomer();
                    break;
                case 4:
                    displayAllCars();
                    break;
                case 5:
                    displayAvailableCars();
                    break;
                case 6:
                    rentCar();
                    break;
                case 7:
                    returnCar();
                    break;
                case 8:
                    searchCarById();
                    break;
                case 9:
                    searchCarByBrand();
                    break;
                case 10:
                    displayAllCustomers();
                    break;
                case 0:
                    exitSystem();
                    break;
                default:
                    System.out.println("[ERROR] Choice outside of menu scope. Please try again.");
            }
            System.out.println();
        }
    }

    private static void printWelcomeBanner() {
        System.out.println("==================================================");
        System.out.println("   WELCOME TO SPEEDWAY RENTALS MANAGEMENT SYSTEM ");
        System.out.println("==================================================");
    }

    private static void displayMenu() {
        System.out.println("========================================");
        System.out.println("        SPEEDWAY RENTALS SYSTEM        ");
        System.out.println("========================================");
        System.out.println("1.  Add Regular Car");
        System.out.println("2.  Add Luxury Car");
        System.out.println("3.  Add Customer");
        System.out.println("4.  Display All Cars");
        System.out.println("5.  Display Available Cars");
        System.out.println("6.  Rent a Car");
        System.out.println("7.  Return a Car");
        System.out.println("8.  Search Car by ID");
        System.out.println("9.  Search Car by Brand");
        System.out.println("10. Display All Customers");
        System.out.println("0.  Exit");
        System.out.println("========================================");
    }

    private static void addCar(boolean isLuxury) {
        if (carCount >= MAX_CARS) {
            System.out.println("[ERROR] Fleet is full. Cannot add more cars (Max: " + MAX_CARS + ").");
            return;
        }

        System.out.print("Enter Car ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (findCarById(id) != null) {
            System.out.println("[ERROR] A car with ID " + id + " already exists.");
            return;
        }

        System.out.print("Enter Brand: ");
        String brand = scanner.nextLine().trim();

        System.out.print("Enter Model: ");
        String model = scanner.nextLine().trim();

        System.out.print("Enter Manufacturing Year (1990 - 2026): ");
        int year = scanner.nextInt();
        if (year < 1990 || year > 2026) {
            System.out.println("[ERROR] Manufacturing year must be between 1990 and 2026.");
            return;
        }

        System.out.print("Enter Daily Rental Price ($): ");
        double price = scanner.nextDouble();
        if (price <= 0) {
            System.out.println("[ERROR] Daily rental price must be greater than zero.");
            return;
        }

        if (isLuxury) {
            System.out.print("Enter One-Time Insurance Fee ($): ");
            double insurance = scanner.nextDouble();
            if (insurance < 0) {
                System.out.println("[ERROR] Insurance fee cannot be negative.");
                return;
            }
            cars[carCount++] = new LuxuryCar(id, brand, model, year, price, insurance);
            System.out.println("[SUCCESS] Luxury Car added successfully! (ID: " + id + ")");
        } else {
            cars[carCount++] = new Car(id, brand, model, year, price);
            System.out.println("[SUCCESS] Regular Car added successfully! (ID: " + id + ")");
        }
    }

    private static void addCustomer() {
        if (customerCount >= MAX_CUSTOMERS) {
            System.out.println("[ERROR] Customer records full (Max: " + MAX_CUSTOMERS + ").");
            return;
        }

        System.out.print("Enter Customer ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (findCustomerById(id) != null) {
            System.out.println("[ERROR] A customer with ID " + id + " already exists.");
            return;
        }

        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();

        customers[customerCount++] = new Customer(id, name, phone);
        System.out.println("[SUCCESS] Customer registered successfully! Name: " + name + " (ID: " + id + ")");
    }

    private static void displayAllCars() {
        if (carCount == 0) {
            System.out.println("No cars registered in the fleet yet.");
            return;
        }
        System.out.println("--- FLEET REGISTER (" + carCount + " total) ---");
        for (int i = 0; i < carCount; i++) {
            System.out.println((i + 1) + ". " + cars[i].getDetails());
        }
    }

    private static void displayAvailableCars() {
        int availableCount = 0;
        System.out.println("--- AVAILABLE CARS ---");
        for (int i = 0; i < carCount; i++) {
            if (cars[i].isAvailable()) {
                availableCount++;
                System.out.println(availableCount + ". " + cars[i].getDetails());
            }
        }
        if (availableCount == 0) {
            System.out.println("No cars are currently available for rent.");
        } else {
            System.out.println("Total Available: " + availableCount);
        }
    }

    private static void rentCar() {
        System.out.print("Enter Customer ID: ");
        int custId = scanner.nextInt();
        Customer customer = findCustomerById(custId);

        if (customer == null) {
            System.out.println("[ERROR] Customer with ID " + custId + " not found.");
            return;
        }

        if (customer.getRentedCarId() != -1) {
            System.out.println("[ERROR] Customer " + customer.getName() + " is already holding Car ID " + customer.getRentedCarId() + ".");
            return;
        }

        System.out.print("Enter Car ID to Rent: ");
        int carId = scanner.nextInt();
        Car car = findCarById(carId);

        if (car == null) {
            System.out.println("[ERROR] Car with ID " + carId + " not found.");
            return;
        }

        if (!car.isAvailable()) {
            System.out.println("[ERROR] Car " + car.getBrand() + " " + car.getModel() + " is currently rented.");
            return;
        }

        System.out.print("Enter Number of Rental Days: ");
        int days = scanner.nextInt();

        if (days <= 0) {
            System.out.println("[ERROR] Rental duration must be greater than zero days.");
            return;
        }

        if (car instanceof LuxuryCar && days < LuxuryCar.MIN_RENTAL_DAYS) {
            System.out.println("[ERROR] Luxury cars require a minimum rental period of " + LuxuryCar.MIN_RENTAL_DAYS + " days.");
            return;
        }

        double totalCost = car.calculateRentalCost(days);

        car.setAvailable(false);
        customer.rentCar(car.getId(), days, totalCost);
        totalOfficeIncome += totalCost;

        System.out.println("\n----------------------------------------");
        System.out.println("             RENTAL RECEIPT             ");
        System.out.println("----------------------------------------");
        System.out.println("Customer Name : " + customer.getName());
        System.out.println("Vehicle       : " + car.getBrand() + " " + car.getModel() + " (" + car.getYear() + ")");
        System.out.println("Rental Period : " + days + " Days");
        System.out.printf("Total Charged : $%.2f (14%% Tax Included)\n", totalCost);
        System.out.println("----------------------------------------");
    }

    private static void returnCar() {
        System.out.print("Enter Customer ID: ");
        int custId = scanner.nextInt();
        Customer customer = findCustomerById(custId);

        if (customer == null) {
            System.out.println("[ERROR] Customer ID not found.");
            return;
        }

        int rentedCarId = customer.getRentedCarId();
        if (rentedCarId == -1) {
            System.out.println("[ERROR] Customer " + customer.getName() + " currently has no car to return.");
            return;
        }

        Car car = findCarById(rentedCarId);
        if (car != null) {
            car.setAvailable(true);
        }

        customer.returnCar();
        System.out.println("[SUCCESS] Car ID " + rentedCarId + " successfully returned by " + customer.getName() + ".");
    }

    private static void searchCarById() {
        System.out.print("Enter Car ID to search: ");
        int id = scanner.nextInt();
        Car car = findCarById(id);

        if (car != null) {
            System.out.println("\n[MATCH FOUND]:\n" + car.getDetails());
        } else {
            System.out.println("[ERROR] Car with ID " + id + " does not exist.");
        }
    }

    private static void searchCarByBrand() {
        scanner.nextLine(); 
        System.out.print("Enter Brand Name to search: ");
        String brand = scanner.nextLine().trim();

        int matches = 0;
        System.out.println("\n--- Search Results for '" + brand + "' ---");
        for (int i = 0; i < carCount; i++) {
            if (cars[i].getBrand().equalsIgnoreCase(brand)) {
                matches++;
                System.out.println(cars[i].getDetails());
            }
        }

        if (matches == 0) {
            System.out.println("No vehicles matching brand '" + brand + "' were found.");
        } else {
            System.out.println("Total Matches: " + matches);
        }
    }

    private static void displayAllCustomers() {
        if (customerCount == 0) {
            System.out.println("No registered customers.");
            return;
        }
        System.out.println("--- REGISTERED CUSTOMERS ---");
        for (int i = 0; i < customerCount; i++) {
            System.out.println((i + 1) + ". " + customers[i].getDetails());
        }
    }

    private static void exitSystem() {
        int rentedCarsCount = 0;
        Car mostExpensiveCar = null;
        double priceSum = 0.0;

        for (int i = 0; i < carCount; i++) {
            if (!cars[i].isAvailable()) {
                rentedCarsCount++;
            }
            
            priceSum += cars[i].getPricePerDay();

            if (mostExpensiveCar == null || cars[i].getPricePerDay() > mostExpensiveCar.getPricePerDay()) {
                mostExpensiveCar = cars[i];
            }
        }

        double avgPrice = (carCount > 0) ? (priceSum / carCount) : 0.0;

        System.out.println("==================================================");
        System.out.println("           FINAL SPEEDWAY OFFICE REPORT           ");
        System.out.println("==================================================");
        System.out.println("Total Vehicles Registered : " + Car.getCarCount());
        System.out.println("Total Registered Customers: " + Customer.getCustomerCount());
        System.out.println("Currently Rented Cars     : " + rentedCarsCount);
        System.out.printf("Total Accumulated Income  : $%.2f\n", totalOfficeIncome);
        System.out.printf("Average Daily Rental Rate : $%.2f\n", avgPrice);
        
        if (mostExpensiveCar != null) {
            System.out.println("Most Expensive Vehicle    : " + mostExpensiveCar.getBrand() + " " 
                    + mostExpensiveCar.getModel() + " ($" + mostExpensiveCar.getPricePerDay() + "/day)");
        } else {
            System.out.println("Most Expensive Vehicle    : N/A");
        }
        System.out.println("==================================================");
        System.out.println("  Thank you for using Speedway Rentals. Goodbye! ");
        System.out.println("==================================================");
    }

    private static Car findCarById(int id) {
        for (int i = 0; i < carCount; i++) {
            if (cars[i].getId() == id) {
                return cars[i];
            }
        }
        return null;
    }

    private static Customer findCustomerById(int id) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getId() == id) {
                return customers[i];
            }
        }
        return null;
    }
}