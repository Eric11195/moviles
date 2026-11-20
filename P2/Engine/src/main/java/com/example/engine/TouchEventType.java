package com.example.engine;

/**
 * Bitmask representation for touch event categories (DOWN, UP, MOVE, NONE, ALL).
 */
public class TouchEventType {

    /** Numeric value for NONE. */
    public final static int NONE_VALUE = 0;

    /** Numeric value for DOWN. */
    public final static int DOWN_VALUE = 1;

    /** Numeric value for UP. */
    public final static int UP_VALUE = 1 << 1;

    /** Numeric value for MOVE. */
    public final static int MOVE_VALUE = 1 << 2;

    /** Numeric value for UNKNOWN. */
    public final static int UNKNOWN_VALUE = 1 << 3;

    /** Numeric value for ALL. */
    public final static int ALL_VALUE = 0xFFFFFFFF;

    /** TouchEventType instance for None. */
    public final static TouchEventType None = new TouchEventType(NONE_VALUE);

    /** TouchEventType instance for DOWN. */
    public final static TouchEventType DOWN = new TouchEventType(DOWN_VALUE);

    /** TouchEventType instance for UP. */
    public final static TouchEventType UP = new TouchEventType(UP_VALUE);

    /** TouchEventType instance for MOVE. */
    public final static TouchEventType MOVE = new TouchEventType(MOVE_VALUE);

    /** TouchEventType instance for UNKNOWN. */
    public final static TouchEventType UNKNOWN = new TouchEventType(UNKNOWN_VALUE);

    /** TouchEventType instance for ALL. */
    public final static TouchEventType ALL = new TouchEventType(ALL_VALUE);

    /** Internal bitmask value. */
    private int numericValue;

    /**
     * Returns the underlying integer bitmask value.
     *
     * @return numeric bitmask value
     */
    public int numVal() { return numericValue; }

    /**
     * Constructs a TouchEventType with specified bitmask integer.
     *
     * @param i bitmask integer
     */
    public TouchEventType(int i) {
        numericValue = i;
    }

    /**
     * Copy constructor.
     *
     * @param i TouchEventType to copy from
     */
    public TouchEventType(TouchEventType i) {
        numericValue = i.numericValue;
    }

    /**
     * Checks if this bitmask contains the flags of type t.
     *
     * @param t target TouchEventType flag
     * @return true if flag is present, false otherwise
     */
    public boolean contains(TouchEventType t) {
        return (t.numericValue & this.numericValue) != 0;
    }

    /**
     * Checks if this bitmask equals type t.
     *
     * @param t target TouchEventType
     * @return true if exactly equal, false otherwise
     */
    public boolean is(TouchEventType t) {
        return t.numericValue == this.numericValue;
    }

    /**
     * Factory method constructing a composite TouchEventType from multiple types.
     *
     * @param t array of TouchEventType flags
     * @return new composite TouchEventType
     */
    public static TouchEventType construct(TouchEventType... t) {
        TouchEventType tet = new TouchEventType(None);
        tet.set(t);
        return tet;
    }

    /**
     * Sets the bitmask to the OR combination of the provided types.
     *
     * @param t array of TouchEventType flags
     */
    public void set(TouchEventType... t) {
        this.numericValue = 0;
        this.or(t);
    }

    /**
     * Unsets (clears) specified flags from this bitmask.
     *
     * @param t flag type to clear
     */
    public void unset(TouchEventType t) {
        this.numericValue &= ALL.numericValue ^ t.numericValue;
    }

    /**
     * Performs a bitwise XOR with another TouchEventType.
     *
     * @param tet type to XOR with
     * @return this instance for chaining
     */
    public TouchEventType xor(TouchEventType tet) {
        this.numericValue ^= tet.numericValue;
        return this;
    }

    /**
     * Performs a bitwise OR with multiple TouchEventType flags.
     *
     * @param tet flags to OR with
     * @return this instance for chaining
     */
    public TouchEventType or(TouchEventType... tet) {
        for (TouchEventType t : tet) {
            this.numericValue |= t.numericValue;
        }
        return this;
    }

    /**
     * Performs a bitwise AND with multiple TouchEventType flags.
     *
     * @param tet flags to AND with
     * @return this instance for chaining
     */
    public TouchEventType and(TouchEventType... tet) {
        for (TouchEventType t : tet) {
            this.numericValue &= t.numericValue;
        }
        return this;
    }

    /**
     * Resets this bitmask to NONE.
     */
    public void setNone() {
        this.numericValue = None.numericValue;
    }
}
