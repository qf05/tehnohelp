package ru.tehnohelp.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


public class EmailMessage {

    private static final String username = "qf013";
    //    private static final String password = PROPERTIES.getProperty("password_mail"); // aplication password
    private static final String password = ""; // aplication password

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
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
            //от кого
            message.setFrom(new InternetAddress(username + "@yandex.ru"));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема сообщения
            message.setSubject(MimeUtility.encodeText(theme, "UTF-8", "Q"));
//            message.setSubject(theme);
            //текст
            message.setContent(textMessage, "text/plain;charset=UTF-8");
//            message.setText(textMessage);
            //отправляем сообщение
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
