package ru.tehnohelp.service;

public class MessageUtils {

//    private static final String PATH_TO_PROPERTIES = "D:\\Project\\src\\main\\resources\\properties\\password.properties";
//    public static final Properties PROPERTIES;
//
//    static {
//        PROPERTIES = new Properties();
//        try {
//            FileInputStream fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
//            PROPERTIES.load(fileInputStream);
//        } catch (IOException e) {
//            System.out.println("Ошибка в программе: файл " + PATH_TO_PROPERTIES + " не обнаружено");
//            e.printStackTrace();
//        }
//    }

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

    private static String checkNullString(String check) {
        if (check == null) {
            check = "- null -";
        }
        return check;
    }
}
