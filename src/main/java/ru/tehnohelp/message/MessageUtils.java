package ru.tehnohelp.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MessageUtils {

    public static final String VK = "token_vk";
    public static final String EMAIL = "password_mail";

    private MessageUtils() {
    }

    protected static String loadPassword(String service) {
        InputStream resourceAsStream = MessageUtils.class.getClassLoader().getResourceAsStream("/properties/password.properties");
        Properties prop = new Properties();
        try {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (VK.equals(service)) {
            return prop.getProperty(VK);
        } else {
            return prop.getProperty(EMAIL);
        }
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

    private static String checkNullString(String check) {
        if (check == null) {
            check = "- null -";
        }
        return check;
    }
}
