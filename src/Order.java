import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private int orderId;
    private String customerName;
    private ArrayList<OrderItem> items;
    private double total;
    private OrderStatus status;

    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.status = OrderStatus.PENDING;
    }

    public void addItem(OrderItem item) {
        if (item != null && item.getQuantity() > 0) {
            items.add(item);
            calculateTotal();
        }
    }

    public boolean removeItem(int menuItemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItem().getId() == menuItemId) {
                items.remove(i);
                calculateTotal();
                return true;
            }
        }
        return false;
    }

    public void calculateTotal() {
        total = 0.0;
        for (OrderItem item : items) {
            total += item.calculateSubtotal();
        }
    }

    public void displayOrder() {
        System.out.println("\n=== Order Details ===");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customerName);
        System.out.println("Status: " + status);
        System.out.println("\nItems:");
        if (items.isEmpty()) {
            System.out.println("No items in this order.");
        } else {
            for (OrderItem item : items) {
                System.out.println("  " + item);
            }
        }
        System.out.printf("Total: %.2f%n", total);
        System.out.println("=====================");
    }

    public void updateStatus(OrderStatus newStatus) {
        if (isValidTransition(this.status, newStatus)) {
            this.status = newStatus;
        } else {
            System.out.println("Invalid status transition from " + this.status + " to " + newStatus);
        }
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case PENDING:
                return next == OrderStatus.IN_KITCHEN || next == OrderStatus.CANCELLED;
            case IN_KITCHEN:
                return next == OrderStatus.COMPLETED || next == OrderStatus.CANCELLED;
            case COMPLETED:
                return false;
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setOrderId(int orderId) {
        if (orderId > 0) {
            this.orderId = orderId;
        }
    }

    public void setCustomerName(String customerName) {
        if (customerName != null && !customerName.trim().isEmpty()) {
            this.customerName = customerName;
        }
    }
}