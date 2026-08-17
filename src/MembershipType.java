public enum MembershipType {
    STUDENT(0.50),
    FACULTY(0.25),
    COMMUNITY(0.00);

    private final double waiverRate;

    MembershipType(double waiverRate) {
        this.waiverRate = waiverRate;
    }

    public double getWaiverRate() {
        return waiverRate;
    }
}