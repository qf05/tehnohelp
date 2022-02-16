package ru.tehnohelp.wallpost.addfriends;

import ru.tehnohelp.message.VkMessage;

import java.util.Calendar;
import java.util.TimerTask;

import static ru.tehnohelp.wallpost.addfriends.TimerAddFriends.startAddFriends;

public class AddTimerTaskToFriends extends TimerTask {

    @Override
    public void run() {
        String result = null;// SearchUserAndAddToFriend.addToFriends();
        Calendar calendar = getNextTime();
        calendar.add(Calendar.HOUR_OF_DAY, 6);
        VkMessage.sendMessage("Result: " + result + " \r\n " +
                "Next add To Friend " + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                calendar.get(Calendar.HOUR_OF_DAY) + " - " + calendar.get(Calendar.MINUTE));
        calendar.add(Calendar.HOUR_OF_DAY, -6);
        this.cancel();
        startAddFriends(calendar.getTime());
    }


    private static Calendar getNextTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 43 + (int) (Math.random() * 67));
        calendar.set(Calendar.MINUTE, (int) (Math.random() * 16) + (int) (Math.random() * 43));
        calendar.set(Calendar.SECOND, (int) (Math.random() * 22) + (int) (Math.random() * 37));
//        calendar.add(Calendar.SECOND, 20);
        return calendar;
    }
}
