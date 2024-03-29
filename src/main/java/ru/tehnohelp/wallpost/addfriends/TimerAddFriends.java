package ru.tehnohelp.wallpost.addfriends;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerAddFriends {

    private static Timer timer = null;
    public static TimerTask timerTask = null;

    public static void startAddFriends(Date date) {
        if (timer == null) {
            timer = new Timer(true);
        }
        timerTask = new AddTimerTask();
        timer.schedule(timerTask, date);
        System.out.println("start timer");
    }

    public static void stopAddFriends() {
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
