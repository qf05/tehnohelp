package ru.tehnohelp.wallpost;

public enum Command {
    DOM("dom"),
    VIDEO("video"),
    TV("tv"),
    INTERNET("i"),
    START("start"),
    RESUME("resume"),
    PROGRESS("progress"),
    STOP("stop");

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
