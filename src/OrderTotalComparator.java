import java.util.Comparator;

public class OrderTotalComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return Double.compare(o1.getTotal(), o2.getTotal());
    }
}