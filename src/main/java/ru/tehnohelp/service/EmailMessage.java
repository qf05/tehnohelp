package ru.tehnohelp.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailMessage {

    private static final String username = "qf013";
    private static final String password = "111111";
    private static final String toEmail = "tehnohelpluga@gmail.com";
    private static final String theme = "Новая заявка с сайта";
    private static final Properties props = new Properties();

    static {
        //      props.put("mail.smtp.auth", "true");
        //      props.put("mail.smtp.starttls.enable", "true");
        //      props.put("mail.smtp.host", "smtp.yandex.ru");
        //      props.put("mail.smtp.port", "465");

        props.put("mail.smtp.host", "smtp.yandex.ru");
        //       props.put("mail.smtp.socketFactory.port", "465");
        //       props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
    }

    private EmailMessage() {
    }

    public static void sendMessage(String textMessage) {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема сообщения
            message.setSubject(theme);
            //текст
            message.setText(textMessage);

            //отправляем сообщение
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
