import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Order {
    private int orderId;
    private String customerName;
    private List<CartItem> items;
    private double total;
    private OrderStatus status;

    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.status = OrderStatus.PENDING;
    }

    public void addItem(Product product, int quantity) {
        Optional<CartItem> existing = items.stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            items.add(new CartItem(product, quantity));
        }
        calculateTotal();
    }

    public boolean removeItem(int productId) {
        boolean removed = items.removeIf(item -> item.getProduct().getId() == productId);
        if (removed) {
            calculateTotal();
        }
        return removed;
    }

    public void calculateTotal() {
        total = items.stream()
                .mapToDouble(CartItem::calculateSubtotal)
                .sum();
    }

    public void displayOrder() {
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customerName);
        System.out.println("Status: " + status);
        System.out.println("Items:");
        items.forEach(item -> System.out.println("  " + item));
        System.out.printf("Total: %.2f%n", total);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}