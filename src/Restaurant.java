import java.util.*;

public class Restaurant {
    private ArrayList<MenuItem> menu;
    private LinkedList<Order> kitchenQueue;
    private HashMap<Integer, Order> orders;
    private LinkedHashMap<Integer, Order> completedOrders;
    private Scanner scanner;

    public Restaurant() {
        menu = new ArrayList<>();
        kitchenQueue = new LinkedList<>();
        orders = new HashMap<>();
        completedOrders = new LinkedHashMap<>();
        scanner = new Scanner(System.in);

        loadExampleData();
    }

    private void loadExampleData() {
        menu.add(new MenuItem(1, "Burger", 150, "Main Course"));
        menu.add(new MenuItem(2, "Pizza", 200, "Main Course"));
        menu.add(new MenuItem(3, "Pasta", 180, "Main Course"));
        menu.add(new MenuItem(4, "Cola", 40, "Drinks"));

        Order exampleOrder = new Order(101, "Ahmed");
        MenuItem burger = findMenuItemById(1);
        MenuItem cola = findMenuItemById(4);
        exampleOrder.addItem(new OrderItem(burger, 2));
        exampleOrder.addItem(new OrderItem(cola, 1));
        orders.put(exampleOrder.getOrderId(), exampleOrder);

        System.out.println("Example data loaded successfully!");
    }

    public void start() {
        boolean running = true;

        while (running) {
            displayMainMenu();
            System.out.print("\nEnter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addMenuItem();
                        break;
                    case 2:
                        removeMenuItem();
                        break;
                    case 3:
                        displayRestaurantMenu();
                        break;
                    case 4:
                        searchMenuItem();
                        break;
                    case 5:
                        createOrder();
                        break;
                    case 6:
                        addItemToOrder();
                        break;
                    case 7:
                        removeItemFromOrder();
                        break;
                    case 8:
                        displayOrder();
                        break;
                    case 9:
                        addOrderToKitchenQueue();
                        break;
                    case 10:
                        processNextOrder();
                        break;
                    case 11:
                        searchOrder();
                        break;
                    case 12:
                        checkOrderStatus();
                        break;
                    case 13:
                        displayCompletedOrders();
                        break;
                    case 14:
                        cancelOrder();
                        break;
                    case 15:
                        running = false;
                        System.out.println("Thank you for using Restaurant Order Manager!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== RESTAURANT ORDER MANAGER ===");
        System.out.println("1. Add Menu Item");
        System.out.println("2. Remove Menu Item");
        System.out.println("3. Display Menu");
        System.out.println("4. Search Menu Item");
        System.out.println("5. Create Order");
        System.out.println("6. Add Item to Order");
        System.out.println("7. Remove Item from Order");
        System.out.println("8. Display Order");
        System.out.println("9. Add Order to Kitchen Queue");
        System.out.println("10. Process Next Order");
        System.out.println("11. Search Order");
        System.out.println("12. Check Order Status");
        System.out.println("13. Display Completed Orders");
        System.out.println("14. Cancel Order");
        System.out.println("15. Exit");
        System.out.println("================================");
    }

    private int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private double readDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private void addMenuItem() {
        System.out.println("\n=== Add Menu Item ===");

        int id = readIntInput("Enter item ID: ");

        if (findMenuItemById(id) != null) {
            System.out.println("Item with ID " + id + " already exists!");
            return;
        }

        if (id <= 0) {
            System.out.println("Item ID must be greater than 0!");
            return;
        }

        System.out.print("Enter item name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Item name cannot be empty!");
            return;
        }

        double price = readDoubleInput("Enter item price: ");

        if (price <= 0) {
            System.out.println("Price must be greater than 0!");
            return;
        }

        System.out.print("Enter item category: ");
        String category = scanner.nextLine().trim();

        if (category.isEmpty()) {
            System.out.println("Category cannot be empty!");
            return;
        }

        menu.add(new MenuItem(id, name, price, category));
        System.out.println("Menu item added successfully!");
    }

    private void removeMenuItem() {
        System.out.println("\n=== Remove Menu Item ===");
        int id = readIntInput("Enter item ID to remove: ");

        MenuItem item = findMenuItemById(id);
        if (item != null) {
            menu.remove(item);
            System.out.println("Menu item removed successfully!");
        } else {
            System.out.println("Menu item not found!");
        }
    }

    private void displayRestaurantMenu() {
        System.out.println("\n=== Restaurant Menu ===");
        if (menu.isEmpty()) {
            System.out.println("Menu is empty.");
        } else {
            for (MenuItem item : menu) {
                System.out.println(item);
            }
        }
    }

    private void searchMenuItem() {
        System.out.println("\n=== Search Menu Item ===");
        int id = readIntInput("Enter item ID to search: ");

        MenuItem item = findMenuItemById(id);
        if (item != null) {
            System.out.println("Found: " + item);
        } else {
            System.out.println("Menu item not found!");
        }
    }

    private void createOrder() {
        System.out.println("\n=== Create Order ===");
        int orderId = readIntInput("Enter order ID: ");

        if (orders.containsKey(orderId)) {
            System.out.println("Order with ID " + orderId + " already exists!");
            return;
        }

        if (orderId <= 0) {
            System.out.println("Order ID must be greater than 0!");
            return;
        }

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine().trim();

        if (customerName.isEmpty()) {
            System.out.println("Customer name cannot be empty!");
            return;
        }

        Order order = new Order(orderId, customerName);
        orders.put(orderId, order);
        System.out.println("Order created successfully! Status: PENDING");
    }

    private void addItemToOrder() {
        System.out.println("\n=== Add Item to Order ===");
        int orderId = readIntInput("Enter order ID: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Cannot add items to " + order.getStatus() + " order!");
            return;
        }

        int menuItemId = readIntInput("Enter menu item ID: ");

        MenuItem menuItem = findMenuItemById(menuItemId);
        if (menuItem == null) {
            System.out.println("Menu item not found!");
            return;
        }

        int quantity = readIntInput("Enter quantity: ");

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0!");
            return;
        }

        order.addItem(new OrderItem(menuItem, quantity));
        System.out.println("Item added to order successfully!");
    }

    private void removeItemFromOrder() {
        System.out.println("\n=== Remove Item from Order ===");
        int orderId = readIntInput("Enter order ID: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Cannot modify " + order.getStatus() + " order!");
            return;
        }

        int menuItemId = readIntInput("Enter menu item ID to remove: ");

        if (order.removeItem(menuItemId)) {
            System.out.println("Item removed from order successfully!");
        } else {
            System.out.println("Item not found in this order!");
        }
    }

    private void displayOrder() {
        System.out.println("\n=== Display Order ===");
        int orderId = readIntInput("Enter order ID: ");

        Order order = orders.get(orderId);
        if (order != null) {
            order.displayOrder();
        } else {
            System.out.println("Order not found!");
        }
    }

    private void addOrderToKitchenQueue() {
        System.out.println("\n=== Add Order to Kitchen Queue ===");
        int orderId = readIntInput("Enter order ID: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Order is not in PENDING status! Current status: " + order.getStatus());
            return;
        }

        if (kitchenQueue.contains(order)) {
            System.out.println("Order is already in kitchen queue!");
            return;
        }

        kitchenQueue.add(order);
        order.updateStatus(OrderStatus.IN_KITCHEN);
        System.out.println("Order added to kitchen queue! Status: IN_KITCHEN");
    }

    private void processNextOrder() {
        System.out.println("\n=== Process Next Order ===");

        if (kitchenQueue.isEmpty()) {
            System.out.println("Kitchen queue is empty!");
            return;
        }

        Order order = kitchenQueue.removeFirst();
        order.updateStatus(OrderStatus.COMPLETED);
        completedOrders.put(order.getOrderId(), order);

        System.out.println("Order #" + order.getOrderId() + " processed successfully!");
        System.out.println("Status: COMPLETED");
    }

    private void cancelOrder() {
        System.out.println("\n=== Cancel Order ===");
        int orderId = readIntInput("Enter order ID to cancel: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            System.out.println("Cannot cancel a COMPLETED order!");
            return;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Order is already CANCELLED!");
            return;
        }

        if (order.getStatus() == OrderStatus.IN_KITCHEN) {
            kitchenQueue.remove(order);
        }

        order.updateStatus(OrderStatus.CANCELLED);
        System.out.println("Order #" + orderId + " has been cancelled.");
    }

    private void searchOrder() {
        System.out.println("\n=== Search Order ===");
        int orderId = readIntInput("Enter order ID to search: ");

        Order order = orders.get(orderId);
        if (order != null) {
            System.out.println("Order found!");
            order.displayOrder();
        } else {
            System.out.println("Order not found!");
        }
    }

    private void checkOrderStatus() {
        System.out.println("\n=== Check Order Status ===");
        int orderId = readIntInput("Enter order ID: ");

        Order order = orders.get(orderId);
        if (order != null) {
            System.out.println("Order #" + orderId + " - Customer: " +
                    order.getCustomerName() + " - Status: " + order.getStatus());
        } else {
            System.out.println("Order not found!");
        }
    }

    private void displayCompletedOrders() {
        System.out.println("\n=== Completed Orders ===");
        if (completedOrders.isEmpty()) {
            System.out.println("No completed orders yet.");
        } else {
            for (Map.Entry<Integer, Order> entry : completedOrders.entrySet()) {
                System.out.println("Order #" + entry.getKey() + " - Customer: " +
                        entry.getValue().getCustomerName() + " - Total: " +
                        String.format("%.2f", entry.getValue().getTotal()));
            }
        }
    }

    private MenuItem findMenuItemById(int id) {
        for (MenuItem item : menu) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        restaurant.start();
    }
}