public enum Side {
    BUY,
    SELL;

    public String toLower() {
        return name().toLowerCase();
    }
}
