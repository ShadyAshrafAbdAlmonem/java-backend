import java.util.Comparator;

public class OrderTotalComparator implements Comparator<Order> {
    private static final Comparator<Order> BY_TOTAL =
            Comparator.comparingDouble(Order::getTotal);

    @Override
    public int compare(Order o1, Order o2) {
        return BY_TOTAL.compare(o1, o2);
    }
}