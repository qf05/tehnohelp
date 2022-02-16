package ru.tehnohelp.wallpost.addfriends;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerAddToFriends {

    private static Timer timer = null;
    public static TimerTask timerTask = null;

    public static void startAddToFriends(Date date) {
        if (timer == null) {
            timer = new Timer(true);
        }
        timerTask = new AddTimerTaskToFriends();
        timer.schedule(timerTask, date);
        System.out.println("start timer");
    }

    public static void stopAddToFriends() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
