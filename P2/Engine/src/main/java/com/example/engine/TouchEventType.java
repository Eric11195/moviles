package com.example.engine;

public class TouchEventType {
    public final static int NONE_VALUE = 0;
    public final static int DOWN_VALUE = 1;
    public final static int UP_VALUE = 1<<1;
    public final static int MOVE_VALUE = 1<<2;
    public final static int UNKNOWN_VALUE = 1<<3;
    public final static int ALL_VALUE = 0xFFFFFFFF;
    public final static TouchEventType None = new TouchEventType(NONE_VALUE);
    public final static TouchEventType DOWN = new TouchEventType(DOWN_VALUE);
    public final static TouchEventType UP = new TouchEventType(UP_VALUE);
    public final static TouchEventType MOVE = new TouchEventType(MOVE_VALUE);
    public final static TouchEventType UNKNOWN = new TouchEventType(UNKNOWN_VALUE);
    public final static TouchEventType ALL = new TouchEventType(ALL_VALUE);
    private int numericValue;
    public int numVal(){return numericValue;}

    public TouchEventType(int i){
        numericValue = i;
    }
    public TouchEventType(TouchEventType i) {
        numericValue = i.numericValue;
    }
    public boolean contains(TouchEventType t){
        return (t.numericValue & this.numericValue) != 0;
    }
    public boolean is(TouchEventType t){
        return t.numericValue == this.numericValue;
    }
    public static TouchEventType construct(TouchEventType... t){
        TouchEventType tet = new TouchEventType(None);
        tet.set(t);
        return tet;
    }
    public void set(TouchEventType... t){
        this.numericValue = 0;
        this.or(t);
    }
    public void unset(TouchEventType t){
        this.numericValue &= ALL.numericValue ^ t.numericValue;
    }
    public TouchEventType xor(TouchEventType tet){
        this.numericValue ^= tet.numericValue;
        return this;
    }
    public TouchEventType or(TouchEventType... tet){
        for(TouchEventType t : tet){
            this.numericValue |= t.numericValue;
        }
        return this;
    }
    public TouchEventType and(TouchEventType... tet){
        for(TouchEventType t : tet){
            this.numericValue &= t.numericValue;
        }
        return this;
    }
    public void setNone(){
        this.numericValue = None.numericValue;
    }
}
