package ru.tehnohelp.message;

import ru.tehnohelp.wallpost.LoadPosts;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MessageUtils {

    public static final String GVK = "token_vk";
    public static final String EMAIL = "password_mail";
    public static final String VK = "token_garsey";
    public static final String CAPTCHA = "captcha_key";

    private MessageUtils() {
    }

    public static String loadPassword(String property) {
        InputStream resourceAsStream = MessageUtils.class.getClassLoader().getResourceAsStream("/properties/password.properties");
        Properties prop = new Properties();
        String pass = "";
        try {
            prop.load(resourceAsStream);
            switch (property) {
                case GVK:
                    pass = prop.getProperty(GVK);
                    break;
                case EMAIL:
                    pass = prop.getProperty(EMAIL);
                    break;
                case VK:
                    pass = prop.getProperty(VK);
                    break;
                case CAPTCHA:
                    pass = prop.getProperty(CAPTCHA);
                    break;
                default:
                    pass = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoadPosts.closeResource(resourceAsStream);
        return pass;
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
