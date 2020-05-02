package ru.tehnohelp.wallpost;

import ru.tehnohelp.message.VkMessage;

import java.util.*;

import static ru.tehnohelp.wallpost.VkWallPosting.sendPost;

public class CommandService {

    private static Map<Command, Long> taskTimesHash = new HashMap<>();
    private static Map<Command, MyTimerTask> taskTimers = new HashMap<>();
    private static List<Command> commands = Arrays.asList(Command.DOM, Command.VIDEO, Command.TV, Command.INTERNET);

    private static Timer timer = null;

    public static String managerCommand(Command command) {
        switch (command) {
            case DOM:
            case VIDEO:
            case TV:
            case INTERNET:
                return sendPost(command);
            case START:
                return startTasks();
            case RESUME:
                return resumeTasks();
            case PROGRESS:
                return progress();
            case STOP:
                return stopTasks();
        }
        return "error";
    }

    protected static void startTimer(MyTimerTask task, Date time) {
        int repeat = 1000 * 60 * 60 * 24 * 5;
        try {
            if (time == null) {
                time = nextDateGenerate();
            }
            taskTimers.put(task.getCommand(), task);
            timer.schedule(task, time, repeat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String startTasks() {
        stopTasks();
        initTimer();
        taskTimesHash = new HashMap<>();
        commands.forEach(i -> taskTimers.put(i, new MyTimerTask(i)));
        taskTimers.forEach((k, v) -> startTimer(v, null));
        return "Start task is OK";
    }

    private static String resumeTasks() {
        taskTimesHash = LoadPosts.loadTimeMap();
        if (taskTimers != null && taskTimers.size() == 0 && taskTimesHash != null && taskTimesHash.size() > 0) {
            initTimer();
            taskTimesHash.forEach((key, value) -> taskTimers.put(key, new MyTimerTask(key)));
            taskTimers.forEach((k, v) -> startTimer(v, new Date(taskTimesHash.get(k))));
            taskTimesHash = new HashMap<>();
            return "Resume task is OK";
        }
        return "Resume task is error";
    }

    private static String stopTasks() {
        if (taskTimers != null && taskTimers.size() > 0) {
            taskTimesHash = new HashMap<>();
            taskTimers.forEach((k, v) -> {
                taskTimesHash.put(k, v.scheduledExecutionTime());
                v.cancel();
            });
            taskTimers = new HashMap<>();
            timer.cancel();
            LoadPosts.saveTimeMap(taskTimesHash);
            return "Stop task is OK";
        }
        return "Stop task is error";
    }

    private static String progress() {
        if (taskTimers != null && taskTimers.size() > 0) {
            return "IS PROGRESS - " + taskTimers.size() + " task";
        } else {
            return "STOP TASKS";
        }
    }

    private static Date nextDateGenerate() {
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

    private static void initTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer(true);
    }
}
