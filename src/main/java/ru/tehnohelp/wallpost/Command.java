package ru.tehnohelp.wallpost;

public enum Command {
    DOM("dom"),
    VIDEO("video"),
    TV("tv"),
    INTERNET("i"),
    START("start"),
    RESUME("resume"),
    PROGRESS("progress"),
    STOP("stop"),
    START_ADD("start add"),
    STOP_ADD("stop add"),
    START_F_ADD("start f add"),
    STOP_F_ADD("stop f add");

    public final String value;

    Command(String value) {
        this.value = value;
    }

    public static Command getByValue(String value) {
        if (value==null||value.length()<1){
            return null;
        }
        for (Command command: Command.values()) {
            if (value.equals(command.value)){
                return command;
            }
        }
        return null;
    }
}
