package com.example.game.testing.Systems;

public class RubySystem {
    private static int rubyCount = 0;
    public static int getRubies(){
        return rubyCount;
    }
    public static boolean canPay(int amount){
        return rubyCount >= amount;
    }
    public static void pay(int amount){
        if(!canPay(amount))
            throw new RuntimeException("You need to ensure that it can be paid before calling this function");
        rubyCount -= amount;
    }
    public static void gainRubies(int amount){
        rubyCount += amount;
    }
}
