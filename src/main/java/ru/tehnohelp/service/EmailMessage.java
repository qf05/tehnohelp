package ru.tehnohelp.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailMessage {

    private static final String username = "qf013";
    private static final String password = MessageUtils.getProp().getProperty("password_mail"); // aplication password
    private static final String toEmail = "tehnohelpluga@gmail.com";
    private static final String theme = "Новая заявка с сайта";
    private static final Properties props;

    static {
        props = new Properties();
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.enable", "true");
    }

    private EmailMessage() {
    }

    public static boolean sendMessage(String textMessage) {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
//        session.setDebug(true);
        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username + "@yandex.ru"));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема сообщения
            message.setSubject(theme);
            //текст
            message.setText(textMessage);
            //отправляем сообщение
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
