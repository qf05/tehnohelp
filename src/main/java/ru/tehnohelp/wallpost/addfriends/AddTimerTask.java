package ru.tehnohelp.wallpost.addfriends;

import java.util.TimerTask;

import static ru.tehnohelp.wallpost.addfriends.TimerAddFriends.startAddFriends;

public class AddTimerTask extends TimerTask {

    @Override
    public void run() {
        AddFriends.addFriends();
        this.cancel();
        startAddFriends();
    }
}
