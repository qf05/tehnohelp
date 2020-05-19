package ru.tehnohelp.wallpost.addfriends;

import ru.tehnohelp.message.VkMessage;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerAddFriends {

    private static Timer timer = null;
    private static TimerTask timerTask = null;
//    private int repeat = 1000 * 60 * 60 * 24 * 10;

    public static void startAddFriends() {
        if (timer == null) {
            timer = new Timer(true);
        }
        timerTask = new AddTimerTask();
        timer.schedule(timerTask, getNextTime());
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

    private static Date getNextTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 9 + (int) (Math.random() * 67));
        calendar.set(Calendar.MINUTE, (int) (Math.random() * 36)+(int) (Math.random() * 24));
        calendar.set(Calendar.SECOND, (int) (Math.random() * 53)+(int) (Math.random() * 7));
//        calendar.add(Calendar.SECOND, 20);
        VkMessage.sendMessage("next add " + calendar.get(Calendar.DAY_OF_MONTH)+" " +
                calendar.get(Calendar.HOUR_OF_DAY)+ " - " +calendar.get(Calendar.MINUTE));
        return calendar.getTime();
    }
}
