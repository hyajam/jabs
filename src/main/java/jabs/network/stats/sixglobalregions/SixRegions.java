package jabs.network.stats.sixglobalregions;

public enum SixRegions {
    NORTH_AMERICA(0),
    EUROPE(1),
    SOUTH_AMERICA(2),
    ASIA_PACIFIC(3),
    JAPAN(4),
    AUSTRALIA(5);

    public static final SixRegions[] sixRegionsValues = SixRegions.values();

    private final int value;
    SixRegions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
