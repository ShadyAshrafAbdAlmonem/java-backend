public class CustomerRepository {
    private final Customer[] customers;
    private int count;
    private int nextCustomerId;

    public CustomerRepository(int capacity) {
        this.customers = new Customer[capacity];
        this.count = 0;
        this.nextCustomerId = 1001;
    }

    public boolean isFull() {
        return count >= customers.length;
    }

    public int generateCustomerId() {
        return nextCustomerId++;
    }

    public boolean addCustomer(Customer customer) {
        if (isFull()) {
            return false;
        }
        customers[count++] = customer;
        return true;
    }

    public Customer findById(int customerId) {
        for (int i = 0; i < count; i++) {
            if (customers[i].getCustomerId() == customerId) {
                return customers[i];
            }
        }
        return null;
    }

    public Customer findByNationalId(String nationalId) {
        for (int i = 0; i < count; i++) {
            if (customers[i].getNationalId().equalsIgnoreCase(nationalId)) {
                return customers[i];
            }
        }
        return null;
    }
}