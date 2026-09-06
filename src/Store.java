import java.util.*;
import java.util.stream.Collectors;

public class Store {
    private List<Product> productList;
    private Map<Integer, Product> productMap;
    private Map<Integer, Order> orderMap;
    private Set<String> categories;
    private Queue<Order> shippingQueue; 
    private Map<Integer, Order> deliveredOrders; 
    private List<Review> reviews; 

    public Store() {
        productList = new ArrayList<>();
        productMap = new HashMap<>();
        orderMap = new HashMap<>();
        categories = new HashSet<>();
        shippingQueue = new LinkedList<>();
        deliveredOrders = new LinkedHashMap<>();
        reviews = new ArrayList<>();
    }

    public void addProduct(int id, String name, double price, String category, int stockQuantity) {
        if (productMap.containsKey(id)) {
            System.out.println("Error: Product with ID " + id + " already exists.");
            return;
        }

        Product product = new Product(id, name, price, category, stockQuantity);
        productList.add(product);
        productMap.put(id, product);
        categories.add(category);
        System.out.println("Product added successfully: " + product);
    }

    public void removeProduct(int id) {
        deleteProductEverywhere(id);
    }

    private void deleteProductEverywhere(int id) {
        Product product = productMap.get(id);

        if (product == null) {
            System.out.println("Error: Product with ID " + id + " does not exist.");
            return;
        }

        productMap.remove(id);
        productList.remove(product);

        updateCategoryAfterProductRemoval(product);

        System.out.println("Product removed successfully: " + product.getName());
    }

    private void updateCategoryAfterProductRemoval(Product product) {
        boolean categoryStillExists = productList.stream()
                .anyMatch(p -> p.getCategory().equals(product.getCategory()));

        if (!categoryStillExists) {
            categories.remove(product.getCategory());
        }
    }

    public void displayAllProducts() {
        if (productList.isEmpty()) {
            System.out.println("No products in the store.");
            return;
        }
        System.out.println("All Products:");
        productList.forEach(System.out::println);
    }

    public void searchProductById(int id) {
        Optional<Product> product = Optional.ofNullable(productMap.get(id));
        if (product.isPresent()) {
            System.out.println("Product found: " + product.get());
        } else {
            System.out.println("Error: Product with ID " + id + " not found.");
        }
    }

    public void showAllCategories() {
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }
        System.out.println("All Categories:");
        categories.forEach(System.out::println);
    }

    public void displayProductsByPrice() {
        System.out.println("Products by Price (Ascending):");
        productList.stream()
                .sorted()
                .forEach(System.out::println);
    }

    public void createOrder(int orderId, String customerName) {
        if (orderMap.containsKey(orderId)) {
            System.out.println("Error: Order with ID " + orderId + " already exists.");
            return;
        }

        Order order = new Order(orderId, customerName);
        orderMap.put(orderId, order);
        System.out.println("Order created successfully: ID " + orderId + " for " + customerName);
    }

    public void addItemToOrder(int orderId, int productId, int quantity) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            System.out.println("Error: Order with ID " + orderId + " not found.");
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Error: Cannot add items to an order that is " + order.getStatus());
            return;
        }

        Product product = productMap.get(productId);
        if (product == null) {
            System.out.println("Error: Product with ID " + productId + " not found.");
            return;
        }

        order.addItem(product, quantity);
        System.out.println("Item added to order " + orderId + ": " + product.getName() + " x " + quantity);
    }

    public void removeItemFromOrder(int orderId, int productId) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            System.out.println("Error: Order with ID " + orderId + " not found.");
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Error: Cannot remove items from an order that is " + order.getStatus());
            return;
        }

        if (order.removeItem(productId)) {
            System.out.println("Item removed from order " + orderId);
        } else {
            System.out.println("Error: Product with ID " + productId + " not found in order " + orderId);
        }
    }

    public void displayOrder(int orderId) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            System.out.println("Error: Order with ID " + orderId + " not found.");
            return;
        }
        order.displayOrder();
    }

    public void addOrderToShipping(int orderId) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            System.out.println("Error: Order with ID " + orderId + " not found.");
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Error: Only PENDING orders can be added to shipping.");
            return;
        }

        if (order.getItems().isEmpty()) {
            System.out.println("Error: Cannot ship an empty order.");
            return;
        }

        if (shippingQueue.contains(order)) {
            System.out.println("Error: Order is already in the shipping queue.");
            return;
        }

        shippingQueue.offer(order);
        order.setStatus(OrderStatus.SHIPPED);
        System.out.println("Order " + orderId + " added to shipping list and marked as SHIPPED.");
    }

    public void shipNextOrder() {
        if (shippingQueue.isEmpty()) {
            System.out.println("Error: No orders waiting to be shipped.");
            return;
        }

        Order order = shippingQueue.peek();

        if (order.getItems().isEmpty()) {
            System.out.println("This order has no items and cannot be shipped.");
            return;
        }

        shippingQueue.poll();
        order.setStatus(OrderStatus.DELIVERED);
        deliveredOrders.put(order.getOrderId(), order);
        System.out.println("Order " + order.getOrderId() + " shipped and delivered.");
    }

    public void cancelOrder(int orderId) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            System.out.println("Error: Order with ID " + orderId + " not found.");
            return;
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            System.out.println("Error: Cannot cancel a delivered order.");
            return;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Error: Order is already cancelled.");
            return;
        }

        if (order.getStatus() == OrderStatus.SHIPPED) {
            shippingQueue.remove(order);
        }

        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("Order " + orderId + " cancelled successfully.");
    }

    public void searchOrderById(int orderId) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            System.out.println("Error: Order with ID " + orderId + " not found.");
            return;
        }
        order.displayOrder();
    }

    public void addReview(int productId, String customerName, String comment) {
        if (!productMap.containsKey(productId)) {
            System.out.println("Error: Product with ID " + productId + " not found.");
            return;
        }

        Review review = new Review(productId, customerName, comment);
        reviews.add(review);
        System.out.println("Review added successfully.");
    }

    public void showReviewsForProduct(int productId) {
        if (!productMap.containsKey(productId)) {
            System.out.println("Error: Product with ID " + productId + " not found.");
            return;
        }

        System.out.println("Reviews for Product " + productId + ":");
        List<Review> productReviews = reviews.stream()
                .filter(review -> review.getProductId() == productId)
                .collect(Collectors.toList());

        if (productReviews.isEmpty()) {
            System.out.println("No reviews found for this product.");
        } else {
            productReviews.forEach(System.out::println);
        }
    }

    public void removeOutOfStockProducts() {
        List<Product> removed = productList.stream()
                .filter(product -> product.getStockQuantity() == 0)
                .collect(Collectors.toList());

        productList.removeAll(removed);

        removed.forEach(product -> {
            productMap.remove(product.getId());
            updateCategoryAfterProductRemoval(product);
        });

        System.out.println(removed.size() + " out-of-stock products removed.");
    }

    public void displayOrdersByTotal() {
        System.out.println("Orders by Total (Ascending):");
        orderMap.values().stream()
                .sorted(new OrderTotalComparator())
                .forEach(order -> System.out.printf("Order ID: %d, Customer: %s, Total: %.2f%n",
                        order.getOrderId(), order.getCustomerName(), order.getTotal()));
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Map<Integer, Product> getProductMap() {
        return productMap;
    }

    public Map<Integer, Order> getOrderMap() {
        return orderMap;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public Queue<Order> getShippingQueue() {
        return shippingQueue;
    }

    public Map<Integer, Order> getDeliveredOrders() {
        return deliveredOrders;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}