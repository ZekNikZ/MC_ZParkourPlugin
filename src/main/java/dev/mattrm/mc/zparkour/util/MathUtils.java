package dev.mattrm.mc.zparkour.util;

public final class MathUtils {
    public static boolean between(int c, int c1, int c2) {
        if (c1 < c2) {
            return c1 <= c && c <= c2;
        } else {
            return c2 <= c && c <= c1;
        }
    }

    public static int clamp(int c, int low, int high) {
        if (c < low) return low;
        else if (c > high) return high;
        return c;
    }
}
