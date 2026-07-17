import java.util.Scanner;

public class CinemaBookingSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Welcome to the Cinema Setup Manager ---");

        System.out.print("Enter number of rows in the cinema: ");
        int totalRows = scanner.nextInt();
        System.out.print("Enter number of seats per row: ");
        int totalCols = scanner.nextInt();
        scanner.nextLine();

        char[][] seats = new char[totalRows][totalCols];
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalCols; j++) {
                seats[i][j] = 'O';
            }
        }

        System.out.print("How many movies are screening today? ");
        int numMovies = scanner.nextInt();
        scanner.nextLine();

        String[] movieNames = new String[numMovies];
        for (int i = 0; i < numMovies; i++) {
            System.out.print("Enter name for Movie " + (i + 1) + ": ");
            movieNames[i] = scanner.nextLine();
        }

        int choice;

        do {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Display Seats");
            System.out.println("2. Book Seat");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Show All Movies");
            System.out.println("5. Show Statistics (Available / Booked)");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displaySeats(seats);
                    break;
                case 2:
                    bookSeat(seats, scanner);
                    break;
                case 3:
                    cancelBooking(seats, scanner);
                    break;
                case 4:
                    showMovies(movieNames);
                    break;
                case 5:
                    showStatistics(seats);
                    break;
                case 0:
                    System.out.println("\nThank you for using the Cinema Ticket Booking System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }

    public static void displaySeats(char[][] seats) {
        System.out.println("\n--- Cinema Seating Chart ---");
        System.out.print("   ");
        for (int c = 1; c <= seats[0].length; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int i = 0; i < seats.length; i++) {
            System.out.print("R" + (i + 1) + " ");
            for (int j = 0; j < seats[i].length; j++) {
                System.out.print(seats[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void bookSeat(char[][] seats, Scanner scanner) {
        System.out.print("Enter Row number: ");
        int rowInput = scanner.nextInt();
        System.out.print("Enter Seat number: ");
        int colInput = scanner.nextInt();

        int row = rowInput - 1;
        int col = colInput - 1;

        if (row < 0 || row >= seats.length || col < 0 || col >= seats[0].length) {
            System.out.println("Invalid Seat.");
            return;
        }

        if (seats[row][col] == 'X') {
            System.out.println("This seat is already booked!");
        } else {
            seats[row][col] = 'X';
            System.out.println("Seat successfully booked!");
            checkOccupancyAlert(seats);
        }
    }

    public static void cancelBooking(char[][] seats, Scanner scanner) {
        System.out.print("Enter Row number to cancel: ");
        int rowInput = scanner.nextInt();
        System.out.print("Enter Seat number to cancel: ");
        int colInput = scanner.nextInt();

        int row = rowInput - 1;
        int col = colInput - 1;

        if (row < 0 || row >= seats.length || col < 0 || col >= seats[0].length) {
            System.out.println("Invalid Seat.");
            return;
        }

        if (seats[row][col] == 'O') {
            System.out.println("This seat is already vacant.");
        } else {
            seats[row][col] = 'O';
            System.out.println("Booking successfully canceled.");
        }
    }

    public static void showMovies(String[] movieNames) {
        System.out.println("\n--- Currently Screening Movies ---");
        for (int i = 0; i < movieNames.length; i++) {
            System.out.println((i + 1) + ". " + movieNames[i]);
        }
    }

    public static int countBookedSeats(char[][] seats) {
        int booked = 0;
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 'X') {
                    booked++;
                }
            }
        }
        return booked;
    }

    public static void showStatistics(char[][] seats) {
        int booked = countBookedSeats(seats);
        int totalSeats = seats.length * seats[0].length;
        int available = totalSeats - booked;

        System.out.println("\n--- Seating Statistics ---");
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Booked Seats: " + booked);
        System.out.println("Available Seats: " + available);
    }

    public static void checkOccupancyAlert(char[][] seats) {
        int booked = countBookedSeats(seats);
        int totalSeats = seats.length * seats[0].length;

        double occupancyRate = (double) booked / totalSeats;
        if (occupancyRate > 0.80) {
            System.out.println("> ALERT: Almost Full (Occupancy is over 80%!)");
        }
    }
}