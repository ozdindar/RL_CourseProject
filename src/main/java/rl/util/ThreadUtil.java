package rl.util;

public class ThreadUtil {
    public static void pause(long millis)
    {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
