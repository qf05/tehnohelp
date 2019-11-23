package ru.tehnohelp.service;

public class MessageUtils {

    private MessageUtils() {
    }

    public static String createMessage(String name, String tel, String theme, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("Имя: ").append(name).append("\r\n");
        builder.append("Тел: ").append(tel).append("\r\n");
        builder.append("Тема: ").append(theme).append("\r\n");
        builder.append("Сообщение: ").append(message);
        return builder.toString();
    }
}
