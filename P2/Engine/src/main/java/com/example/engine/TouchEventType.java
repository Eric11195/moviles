package com.example.engine;

public class TouchEventType {
    public final static TouchEventType None = new TouchEventType(0);
    public final static TouchEventType DOWN = new TouchEventType(1<<0);
    public final static TouchEventType UP = new TouchEventType(1<<1);
    public final static TouchEventType MOVE = new TouchEventType(1<<2);
    public final static TouchEventType UNKNOWN = new TouchEventType(1<<3);
    public final static TouchEventType ALL = new TouchEventType(0xFFFFFFFF);
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
