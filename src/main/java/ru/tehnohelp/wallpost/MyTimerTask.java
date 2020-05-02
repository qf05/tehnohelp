package ru.tehnohelp.wallpost;

import ru.tehnohelp.message.VkMessage;

import java.util.*;

public class MyTimerTask extends TimerTask {

    private Command command;

    public MyTimerTask(Command command) {
        this.command = command;
    }

    @Override
    public void run() {
        String result = VkWallPosting.sendPost(command);
        if (result.contains("error")) {
            VkMessage.sendMessage(result);
        }
        this.cancel();
        CommandService.startTimer(new MyTimerTask(command), null);
    }

    public Command getCommand() {
        return command;
    }
}
