public abstract class LibraryItem {
    private static final String LIBRARY_NAME = "Bayt Al Hekma";
    private static final double ADMINISTRATIVE_CHARGE = 10.0;
    private static int itemsEverCatalogued = 0;

    private final String catalogueId;
    private final String title;
    private ItemStatus status;
    private String borrowerName;
    private int renewalCount;

    public LibraryItem(String catalogueId, String title) {
        this.catalogueId = catalogueId;
        this.title = title;
        this.status = ItemStatus.AVAILABLE;
        this.borrowerName = null;
        this.renewalCount = 0;
        itemsEverCatalogued++;
    }

    public static String getLibraryName() {
        return LIBRARY_NAME;
    }

    public static double getAdministrativeCharge() {
        return ADMINISTRATIVE_CHARGE;
    }

    public static int getItemsEverCatalogued() {
        return itemsEverCatalogued;
    }

    public String getCatalogueId() {
        return catalogueId;
    }

    public String getTitle() {
        return title;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public boolean markReserved() {
        if (status != ItemStatus.AVAILABLE) {
            return false;
        }
        status = ItemStatus.RESERVED;
        return true;
    }

    public boolean markLost() {
        if (status == ItemStatus.ON_LOAN) {
            return false;
        }
        status = ItemStatus.LOST;
        return true;
    }

    public boolean bringBack() {
        if (status != ItemStatus.RESERVED && status != ItemStatus.LOST) {
            return false;
        }
        status = ItemStatus.AVAILABLE;
        borrowerName = null;
        renewalCount = 0;
        return true;
    }

    public boolean lendTo(String memberName) {
        if (status != ItemStatus.AVAILABLE) {
            return false;
        }
        status = ItemStatus.ON_LOAN;
        borrowerName = memberName;
        renewalCount = 0;
        return true;
    }

    public final void takeBack() {
        status = ItemStatus.AVAILABLE;
        borrowerName = null;
        renewalCount = 0;
    }

    protected boolean recordRenewal() {
        if (status != ItemStatus.ON_LOAN) {
            return false;
        }
        renewalCount++;
        return true;
    }

    public abstract double calculateFine(int daysLate);

    public abstract int getLoanPeriod();

    public abstract String getCategoryName();

    public void display() {
        System.out.printf("%s | %s | %s | %s | %s | %d days | %.2f EGP%n",
            catalogueId, getCategoryName(), title, status,
            borrowerName == null ? "N/A" : borrowerName,
            getLoanPeriod(), calculateFine(1));
    }
}