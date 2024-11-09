package me.electronicsboy.missionvayuputr.sensor.app.util;

public class TimeUtil {
    public static void delayMs(long millseconds) {
        long stime = System.currentTimeMillis();
        long ctime = System.currentTimeMillis();
        while((ctime - stime) >= millseconds)
            ctime = System.currentTimeMillis();
    }
}
