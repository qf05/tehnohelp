package ru.tehnohelp.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MessageUtils {

    private static final String PATH_TO_PROPERTIES = "src/main/resources/password.properties";

    private static Properties prop = null;

    private MessageUtils() {
    }

    public static String createMessage(String name, String tel, String theme, String message) {
        StringBuilder builder = new StringBuilder();
        name = checkNullString(name);
        tel = checkNullString(tel);
        theme = checkNullString(theme);
        message = checkNullString(message);
        builder.append("Имя: ").append(name).append("\r\n");
        builder.append("Тел: ").append(tel).append("\r\n");
        builder.append("Тема: ").append(theme).append("\r\n");
        builder.append("Сообщение: ").append(message);
        return builder.toString();
    }

    public static Properties getProp() {
        initProperties();
        return prop;
    }

    private static void initProperties() {
        if (prop == null) {
            prop = new Properties();
            try {
                FileInputStream fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
                prop.load(fileInputStream);
            } catch (IOException e) {
                System.out.println("Ошибка в программе: файл " + PATH_TO_PROPERTIES + " не обнаружено");
                e.printStackTrace();
            }
        }
    }

    private static String checkNullString(String check) {
        if (check == null) {
            check = "- null -";
        }
        return check;
    }
}
