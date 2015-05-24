package com.juanma.drunkgames.utils;

import java.util.Random;

public class Misc {

    public static int randInt(int top) {
        Random rnd = new Random();
        return rnd.nextInt(top);
    }

}
