package ru.tehnohelp.wallpost;

import ru.tehnohelp.message.VkMessage;

import java.util.*;

public class MyTimerTask extends TimerTask {

    protected static List<MyTimerTask> tasks = new ArrayList<>();
    private Command command;
    private Timer timer;

    public MyTimerTask(Command command, Date time) {
        this.command = command;
        timer = new Timer(true);
        tasks.add(this);
        startTimer(this, time);
    }

    @Override
    public void run() {
        String result = VkWallPosting.sendPost(command);
        if (result.contains("error")) {
            VkMessage.sendMessage(result);
        }
        timer.cancel();
        tasks.remove(this);
        new MyTimerTask(command, null);
    }

    private void startTimer(MyTimerTask task, Date time) {
        int repeat = 1000 * 60 * 60 * 24 * 5;
        try {
            if (time == null) {
                time = nextDateGenerate();
            }
            task.timer.scheduleAtFixedRate(task, time, repeat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Date nextDateGenerate() {
        Calendar calendar = Calendar.getInstance();
        Time time = LoadPosts.getTimeProperty();
        if (time != null) {
            time.getAddTime().forEach((key, value) -> calendar.set(key, calendar.get(key) + value));
            time.getRandomTime().forEach((key, value) -> calendar.set(key, calendar.get(key) + (int) (Math.random() * value)));
            return calendar.getTime();
        }
        VkMessage.sendMessage("Time is broken");
        calendar.set(Calendar.DAY_OF_YEAR, Calendar.DAY_OF_YEAR + 7);
        return calendar.getTime();
    }

    public Timer getTimer() {
        return timer;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyTimerTask task = (MyTimerTask) o;
        return command == task.command &&
                Objects.equals(timer, task.timer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, timer);
    }
}
