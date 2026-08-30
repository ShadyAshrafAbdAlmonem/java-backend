import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Store store = new Store();

    public static void main(String[] args) {
        loadExampleData();

        while (true) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    removeProduct();
                    break;
                case 3:
                    store.displayAllProducts();
                    break;
                case 4:
                    searchProduct();
                    break;
                case 5:
                    store.showAllCategories();
                    break;
                case 6:
                    store.displayProductsByPrice();
                    break;
                case 7:
                    createOrder();
                    break;
                case 8:
                    addItemToOrder();
                    break;
                case 9:
                    removeItemFromOrder();
                    break;
                case 10:
                    displayOrder();
                    break;
                case 11:
                    addOrderToShipping();
                    break;
                case 12:
                    store.shipNextOrder();
                    break;
                case 13:
                    cancelOrder();
                    break;
                case 14:
                    searchOrder();
                    break;
                case 15:
                    addReview();
                    break;
                case 16:
                    showReviews();
                    break;
                case 17:
                    store.removeOutOfStockProducts();
                    break;
                case 18:
                    store.displayOrdersByTotal();
                    break;
                case 19:
                    System.out.println("Exiting program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }

    private static void displayMenu() {
        System.out.println("=== E-Commerce Order & Inventory Manager ===");
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Display All Products");
        System.out.println("4. Search Product by ID");
        System.out.println("5. Show All Categories");
        System.out.println("6. Display Products Ordered by Price");
        System.out.println("7. Create Order");
        System.out.println("8. Add Item to Order");
        System.out.println("9. Remove Item from Order");
        System.out.println("10. Display Order");
        System.out.println("11. Add Order to the Shipping List");
        System.out.println("12. Ship Next Order");
        System.out.println("13. Cancel Order");
        System.out.println("14. Search Order by ID");
        System.out.println("15. Add Review to a Product");
        System.out.println("16. Show All Reviews for a Product");
        System.out.println("17. Remove Out-of-Stock Products");
        System.out.println("18. Display Orders Ordered by Total");
        System.out.println("19. Exit");
        System.out.println("==========================================");
    }

    private static void addProduct() {
        int id = getIntInput("Enter product ID: ");
        String name = getStringInput("Enter product name: ");
        double price = getDoubleInput("Enter product price: ");
        String category = getStringInput("Enter product category: ");
        int stockQuantity = getIntInput("Enter stock quantity: ");
        store.addProduct(id, name, price, category, stockQuantity);
    }

    private static void removeProduct() {
        int id = getIntInput("Enter product ID to remove: ");
        store.removeProduct(id);
    }

    private static void searchProduct() {
        int id = getIntInput("Enter product ID to search: ");
        store.searchProductById(id);
    }

    private static void createOrder() {
        int orderId = getIntInput("Enter order ID: ");
        String customerName = getStringInput("Enter customer name: ");
        store.createOrder(orderId, customerName);
    }

    private static void addItemToOrder() {
        int orderId = getIntInput("Enter order ID: ");
        int productId = getIntInput("Enter product ID: ");
        int quantity = getIntInput("Enter quantity: ");
        store.addItemToOrder(orderId, productId, quantity);
    }

    private static void removeItemFromOrder() {
        int orderId = getIntInput("Enter order ID: ");
        int productId = getIntInput("Enter product ID to remove: ");
        store.removeItemFromOrder(orderId, productId);
    }

    private static void displayOrder() {
        int orderId = getIntInput("Enter order ID: ");
        store.displayOrder(orderId);
    }

    private static void addOrderToShipping() {
        int orderId = getIntInput("Enter order ID to add to shipping: ");
        store.addOrderToShipping(orderId);
    }

    private static void cancelOrder() {
        int orderId = getIntInput("Enter order ID to cancel: ");
        store.cancelOrder(orderId);
    }

    private static void searchOrder() {
        int orderId = getIntInput("Enter order ID to search: ");
        store.searchOrderById(orderId);
    }

    private static void addReview() {
        int productId = getIntInput("Enter product ID: ");
        String customerName = getStringInput("Enter customer name: ");
        String comment = getStringInput("Enter comment: ");
        store.addReview(productId, customerName, comment);
    }

    private static void showReviews() {
        int productId = getIntInput("Enter product ID: ");
        store.showReviewsForProduct(productId);
    }

    private static int getIntInput(String data) {
        System.out.print(data);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            System.out.print(data);
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static double getDoubleInput(String data) {
        System.out.print(data);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            System.out.print(data);
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    private static String getStringInput(String data) {
        System.out.print(data);
        return scanner.nextLine();
    }

    private static void loadExampleData() {
        System.out.println("Loading example data...");
        store.addProduct(1, "Wireless Mouse", 250, "Electronics", 15);
        store.addProduct(2, "Notebook", 30, "Stationery", 0);
        store.addProduct(3, "Desk Lamp", 180, "Home", 8);
        store.addProduct(4, "USB Cable", 60, "Electronics", 20);
        System.out.println("Example data loaded.\n");
    }
}