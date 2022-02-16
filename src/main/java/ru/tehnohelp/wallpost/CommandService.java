package ru.tehnohelp.wallpost;

import ru.tehnohelp.message.VkMessage;
import ru.tehnohelp.wallpost.addfriends.TimerAddFriends;
import ru.tehnohelp.wallpost.addfriends.TimerAddToFriends;

import java.util.*;

import static ru.tehnohelp.wallpost.VkWallPosting.sendPost;

public class CommandService {

    private static Map<Command, Long> taskTimesHash = new HashMap<>();
    private static Map<Command, MyTimerTask> taskTimers = new HashMap<>();
    private static List<Command> commands = Arrays.asList(Command.DOM, Command.VIDEO, Command.TV, Command.INTERNET);
    public static boolean addProgress = false;
    public static boolean addToFriendsProgress = false;

    private static Timer timer = null;

    public static String managerCommand(Command command) {
        Calendar calendar = Calendar.getInstance();
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
            case START_ADD:
                calendar.add(Calendar.HOUR_OF_DAY, 15);
                TimerAddFriends.startAddFriends(calendar.getTime());
                addProgress = true;
                return "OK";
            case STOP_ADD:
                TimerAddFriends.stopAddFriends();
                addProgress = false;
                return "OK";
            case START_F_ADD:
                calendar.add(Calendar.HOUR_OF_DAY, 31);
                TimerAddToFriends.startAddToFriends(calendar.getTime());
                addToFriendsProgress = true;
                return "OK";
            case STOP_F_ADD:
                TimerAddToFriends.stopAddToFriends();
                addToFriendsProgress = false;
                return "OK";
        }
        return "error";
    }

    protected static void startTimer(MyTimerTask task, Date time) {
        try {
            if (time == null) {
                time = nextDateGenerate();
            }
            taskTimers.put(task.getCommand(), task);
            timer.schedule(task, time);
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
        String result = "";
        if (taskTimers != null && taskTimers.size() > 0) {
            result = "IS PROGRESS - " + taskTimers.size() + " task";
        }
        if (addProgress) {
            result += " & ADD IS PROGRESS";
        }
        if (addToFriendsProgress) {
            result += " & ADD TO FRIENDS IS PROGRESS";
        }
        if (result.length() < 2) {
            result = "STOP ALL TASKS";
        }
        return result;
    }

    private static Date nextDateGenerate() {
        Calendar calendar = Calendar.getInstance();
        Time time = LoadPosts.getTimeProperty();
        if (time != null) {
            time.getAddTime().forEach((key, value) -> calendar.set(key, calendar.get(key) + value));
            time.getRandomTime().forEach((key, value) -> calendar.set(key, calendar.get(key) + (int) (Math.random() * value)));
            VkMessage.sendMessage("next post " + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                    calendar.get(Calendar.HOUR_OF_DAY) + " - " + calendar.get(Calendar.MINUTE));
            return calendar.getTime();
        }
        VkMessage.sendMessage("Time is broken");
        calendar.set(Calendar.DAY_OF_YEAR, Calendar.DAY_OF_YEAR + 20);
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
