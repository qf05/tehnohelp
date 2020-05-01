package ru.tehnohelp.wallpost;

import java.util.*;

import static ru.tehnohelp.wallpost.VkWallPosting.sendPost;

public class CommandService {

    private static Map<Command, Long> taskTimesHash = new HashMap<>();
    private static List<Command> commands = Arrays.asList(Command.DOM, Command.VIDEO, Command.TV, Command.INTERNET);

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

    private static String startTasks() {
        stopTasks();
        taskTimesHash = new HashMap<>();
        commands.forEach(i -> new MyTimerTask(i, null));
        return "Resume task is OK";
    }

    private static String resumeTasks() {
        List<MyTimerTask> tasks = MyTimerTask.tasks;
        if (tasks != null && tasks.size() == 0) {
            taskTimesHash.forEach((key, value) -> new MyTimerTask(key, new Date(value)));
            taskTimesHash = new HashMap<>();
        } else {
            return "Resume task is error";
        }
        return "Resume task is OK";
    }

    private static String stopTasks() {
        List<MyTimerTask> tasks = MyTimerTask.tasks;
        if (tasks != null && tasks.size() > 0) {
            taskTimesHash = new HashMap<>();
            tasks.forEach(i -> taskTimesHash.put(i.getCommand(), i.scheduledExecutionTime()));
            tasks.stream().map(MyTimerTask::getTimer).forEach(Timer::cancel);
            tasks.clear();
        }
        MyTimerTask.tasks = new ArrayList<>();
        return "Stop task is OK";
    }

    private static String progress(){
        List<MyTimerTask> tasks = MyTimerTask.tasks;
        if (tasks != null && tasks.size() > 0){
            return "IS PROGRESS - " + tasks.size() + " task";
        }else {
            return "STOP TASKS";
        }
    }
}
