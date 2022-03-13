package ru.tehnohelp.message.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MessageUtils {

    public static final String GVK = "token_vk_group";
    public static final String TO_MAIL = "to_mail";
    public static final String FROM_MAIL_LOGIN_YANDEX = "login_from_mail_yandex";
    public static final String PASSWORD_MAIL = "password_mail";
    public static final String TOKEN_GARSEY = "token_garsey";
    public static final String TOKEN_IVANOV = "token_ivanov";
    public static final String CAPTCHA = "captcha_key";
    public static final String SMS_TO_NUMBER = "sms_to_number";
    public static final String SMS_LOGIN = "sms_login";
    public static final String SMS_SENDER = "sms_sender";
    public static final String SMS_PASSWORD = "sms_password";

    private MessageUtils() {
    }

    public static String loadPassword(String property) {
        InputStream resourceAsStream = MessageUtils.class.getClassLoader().getResourceAsStream("/properties/password.properties");
        Properties prop = new Properties();
        String pass = "";
        try {
            prop.load(resourceAsStream);
            switch (property) {
                case SMS_SENDER:
                    pass = prop.getProperty(SMS_SENDER);
                    break;
                case SMS_LOGIN:
                    pass = prop.getProperty(SMS_LOGIN);
                    break;
                case SMS_PASSWORD:
                    pass = prop.getProperty(SMS_PASSWORD);
                    break;
                case SMS_TO_NUMBER:
                    pass = prop.getProperty(SMS_TO_NUMBER);
                    break;
                case TO_MAIL:
                    pass = prop.getProperty(TO_MAIL);
                    break;
                case FROM_MAIL_LOGIN_YANDEX:
                    pass = prop.getProperty(FROM_MAIL_LOGIN_YANDEX);
                    break;
                    case GVK:
                    pass = prop.getProperty(GVK);
                    break;
                case PASSWORD_MAIL:
                    pass = prop.getProperty(PASSWORD_MAIL);
                    break;
                case TOKEN_GARSEY:
                    pass = prop.getProperty(TOKEN_GARSEY);
                    break;
                case TOKEN_IVANOV:
                    pass = prop.getProperty(TOKEN_IVANOV);
                    break;
                case CAPTCHA:
                    pass = prop.getProperty(CAPTCHA);
                    break;
                default:
                    pass = "";
            }
            if (resourceAsStream != null) {
                resourceAsStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
