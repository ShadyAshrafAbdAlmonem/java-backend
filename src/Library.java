public class Library {
    private static final int CATALOGUE_CAPACITY = 100;
    private static final int REGISTER_CAPACITY = 100;

    private final LibraryItem[] catalogue;
    private int catalogueSize;
    private final Member[] memberRegister;
    private final String[] memberIdsInUse;
    private int memberCount;

    public Library() {
        catalogue = new LibraryItem[CATALOGUE_CAPACITY];
        catalogueSize = 0;
        memberRegister = new Member[REGISTER_CAPACITY];
        memberIdsInUse = new String[REGISTER_CAPACITY];
        memberCount = 0;
    }

    public boolean registerItem(LibraryItem item) {
        if (catalogueSize >= CATALOGUE_CAPACITY) {
            return false;
        }
        if (findItem(item.getCatalogueId()) != null) {
            return false;
        }
        catalogue[catalogueSize] = item;
        catalogueSize++;
        return true;
    }

    public boolean registerMember(Member member) {
        if (memberCount >= REGISTER_CAPACITY) {
            return false;
        }
        if (findMember(member.getMembershipId()) != null) {
            return false;
        }
        memberRegister[memberCount] = member;
        memberIdsInUse[memberCount] = member.getMembershipId();
        memberCount++;
        return true;
    }

    public LibraryItem findItem(String catalogueId) {
        for (int i = 0; i < catalogueSize; i++) {
            if (catalogue[i].getCatalogueId().equals(catalogueId)) {
                return catalogue[i];
            }
        }
        return null;
    }

    public Member findMember(String membershipId) {
        for (int i = 0; i < memberCount; i++) {
            if (memberIdsInUse[i].equals(membershipId)) {
                return memberRegister[i];
            }
        }
        return null;
    }

    private Member findMemberByName(String name) {
        for (int i = 0; i < memberCount; i++) {
            if (memberRegister[i].getName().equals(name)) {
                return memberRegister[i];
            }
        }
        return null;
    }

    public void listCatalogue() {
        if (catalogueSize == 0) {
            System.out.println("The catalogue is empty.");
            return;
        }
        for (int i = 0; i < catalogueSize; i++) {
            System.out.print((i + 1) + ". ");
            catalogue[i].display();
        }
    }

    public void listItemsByStatus(ItemStatus status) {
        boolean found = false;
        for (int i = 0; i < catalogueSize; i++) {
            if (catalogue[i].getStatus() == status) {
                System.out.print("- ");
                catalogue[i].display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items are currently " + status.toString().toLowerCase().replace('_', ' ') + ".");
        }
    }

    public void listAllMembers() {
        if (memberCount == 0) {
            System.out.println("The member register is empty.");
            return;
        }
        System.out.printf("%-20s | %-12s | %-10s | %-11s | %-12s%n",
            "Name", "Membership ID", "Category", "Items Held", "Balance Owed");
        for (int i = 0; i < memberCount; i++) {
            Member m = memberRegister[i];
            System.out.printf("%-20s | %-12s | %-10s | %-11d | %.2f EGP%n",
                m.getName(), m.getMembershipId(), m.getCategory(),
                m.getItemsHeld(), m.getBalanceOwed());
        }
    }

    public int getItemsOnLoan() {
        int count = 0;
        for (int i = 0; i < catalogueSize; i++) {
            if (catalogue[i].getStatus() == ItemStatus.ON_LOAN) {
                count++;
            }
        }
        return count;
    }

    public double getLoanRate() {
        if (catalogueSize == 0) {
            return 0.0;
        }
        return (double) getItemsOnLoan() / catalogueSize * 100.0;
    }

    public double getTotalOutstanding() {
        double total = 0.0;
        for (int i = 0; i < memberCount; i++) {
            total += memberRegister[i].getBalanceOwed();
        }
        return total;
    }

    public double getProjectedFines(int daysLate) {
        double total = 0.0;
        for (int i = 0; i < catalogueSize; i++) {
            LibraryItem item = catalogue[i];
            if (item.getStatus() == ItemStatus.ON_LOAN) {
                total += item.calculateFine(daysLate);
            }
        }
        return total;
    }

    public boolean lendItem(String catalogueId, String membershipId) {
        LibraryItem item = findItem(catalogueId);
        if (item == null) {
            System.out.println("Item not found in the catalogue.");
            return false;
        }
        Member member = findMember(membershipId);
        if (member == null) {
            System.out.println("Member not found in the register.");
            return false;
        }
        if (item.getStatus() != ItemStatus.AVAILABLE) {
            System.out.println("This item is not available to borrow. Current status: "
                + item.getStatus().toString().toLowerCase().replace('_', ' '));
            return false;
        }
        if (!member.canBorrow()) {
            System.out.println("This member cannot borrow right now. "
                + "Members may hold at most 3 items and owe no more than 100.00 EGP.");
            return false;
        }
        boolean ok = item.lendTo(member.getName());
        if (ok) {
            member.recordBorrowing();
            System.out.println("Item '" + item.getTitle() + "' lent to " + member.getName()
                + ". Loan period: " + item.getLoanPeriod() + " days.");
        }
        return ok;
    }

    public boolean returnItem(String catalogueId, int daysOverdue) {
        if (daysOverdue < 0) {
            System.out.println("Days overdue cannot be negative.");
            return false;
        }
        LibraryItem item = findItem(catalogueId);
        if (item == null) {
            System.out.println("Item not found in the catalogue.");
            return false;
        }
        if (item.getStatus() != ItemStatus.ON_LOAN) {
            System.out.println("This item is not currently on loan.");
            return false;
        }
        String borrowerName = item.getBorrowerName();
        Member member = findMemberByName(borrowerName);
        if (member == null) {
            System.out.println("Member who borrowed this item is no longer in the register.");
            return false;
        }

        double baseFine = 0.0;
        double waiver = 0.0;
        double adminCharge = 0.0;
        double totalCharge = 0.0;

        if (daysOverdue > 0) {
            baseFine = item.calculateFine(daysOverdue);
            waiver = baseFine * member.getCategory().getWaiverRate();
            adminCharge = LibraryItem.getAdministrativeCharge();
            totalCharge = baseFine - waiver + adminCharge;
            member.chargeFine(totalCharge);
        }

        member.recordReturn();
        item.takeBack();

        System.out.println("=== Return Receipt for '" + item.getTitle() + "' ===");
        System.out.println("Borrower: " + borrowerName);
        System.out.println("Category: " + member.getCategory());
        if (daysOverdue > 0) {
            System.out.printf("Days overdue: %d%n", daysOverdue);
            System.out.printf("Base fine: %.2f EGP%n", baseFine);
            System.out.printf("Waiver (%d%%): -%.2f EGP%n",
                (int)(member.getCategory().getWaiverRate() * 100), waiver);
            System.out.printf("Administrative charge: +%.2f EGP%n", adminCharge);
            System.out.printf("Total charged: %.2f EGP%n", totalCharge);
        } else {
            System.out.println("Returned on time. No fines charged.");
        }
        System.out.printf("New balance for %s: %.2f EGP%n", borrowerName, member.getBalanceOwed());

        return true;
    }

    public boolean renewItem(String catalogueId) {
        LibraryItem item = findItem(catalogueId);
        if (item == null) {
            System.out.println("Item not found in the catalogue.");
            return false;
        }
        if (!(item instanceof Renewable)) {
            System.out.println("This item type (" + item.getCategoryName()
                + ") cannot be renewed.");
            return false;
        }
        Renewable renewable = (Renewable) item;
        boolean ok = renewable.renew();
        if (ok) {
            int remaining = renewable.getRenewalLimit() - item.getRenewalCount();
            System.out.println("Loan renewed. Renewals remaining: " + remaining);
        } else {
            if (item.getStatus() != ItemStatus.ON_LOAN) {
                System.out.println("This item is not currently on loan and cannot be renewed.");
            } else {
                System.out.println("This item has reached its renewal limit.");
            }
        }
        return ok;
    }

    public void printReport() {
        System.out.println("=== Bayt Al Hekma Library Report ===");
        System.out.println("Catalogue capacity: " + CATALOGUE_CAPACITY);
        System.out.println("Catalogue size: " + catalogueSize);
        System.out.println("Items ever catalogued: " + LibraryItem.getItemsEverCatalogued());
        System.out.println("Items currently on loan: " + getItemsOnLoan());
        System.out.printf("Loan rate: %.2f%%%n", getLoanRate());
        System.out.printf("Total outstanding fines: %.2f EGP%n", getTotalOutstanding());
        System.out.printf("Projected fines (5 days late, before waivers): %.2f EGP%n",
            getProjectedFines(5));
    }

}
