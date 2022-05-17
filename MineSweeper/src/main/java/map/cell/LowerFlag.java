package map.cell;

/**
 *
 * @author Lampirg
 */
public enum LowerFlag {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    BOMB,
    NONE;
    
    private final static LowerFlag[] flags = values();
    public static LowerFlag[] getFlags() {
        return flags;
    }
}
