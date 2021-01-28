package dev.mattrm.mc.zparkour.util;

public class TimeUtils {
    public static String format(long millis) {
        return String.format("%d:%02d.%03d", millis / 1000 / 60, millis / 1000 % 60, millis % 1000);
    }
}
