package ru.tehnohelp.wallpost;

import ru.tehnohelp.message.VkMessage;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    private Command command;

    public MyTimerTask(Command command) {
        this.command = command;
    }

    @Override
    public void run() {
        String result = VkWallPosting.sendPost(command);
        VkMessage.sendMessage(result);
        CommandService.startTimer(new MyTimerTask(command), null);
        this.cancel();
    }

    public Command getCommand() {
        return command;
    }
}
