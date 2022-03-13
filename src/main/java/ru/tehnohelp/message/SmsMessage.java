package ru.tehnohelp.message;

import ru.tehnohelp.message.util.MessageUtils;
import ru.tehnohelp.message.util.SMSCSender;

public class SmsMessage {
    private static final String SMS_TO_NUMBER = MessageUtils.loadPassword(MessageUtils.SMS_TO_NUMBER);
    private static final String SMS_LOGIN    = MessageUtils.loadPassword(MessageUtils.SMS_LOGIN);
    private static final String SMS_PASSWORD = MessageUtils.loadPassword(MessageUtils.SMS_PASSWORD);
    private static final String SMS_SENDER = MessageUtils.loadPassword(MessageUtils.SMS_SENDER);

    public static boolean sendMessage(String message) {
        message = "tehnoluga zayavka " + message;
        try {
            SMSCSender sd = new SMSCSender(SMS_LOGIN, SMS_PASSWORD);

            final String[] result = sd.sendSms(SMS_TO_NUMBER, message, 1, "", "", 0, SMS_SENDER, "");
            //sd.getSmsCost("38*********5", "Вы успешно зарегистрированы!", 0, 0, "", "");
            try {
                double balance = Double.parseDouble(sd.getBalance());
                System.out.println("Balance = " + balance);
                if ((int) balance < 50) {
                    //balanceControl(balance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Integer.parseInt(result[1]) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void balanceControl(double balance){

        Runnable task = () -> {
            try {
                Thread.sleep(70000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SMSCSender sd = new SMSCSender(SMS_LOGIN, SMS_PASSWORD);
            sd.sendSms("79062522523", "!!!tehnoluga!!! sms balance = " + balance + " smsc.ru", 1, "", "", 0, "", "");
            sd.sendSms("info@tehnoluga.ru", "!!!tehnoluga!!! sms balance = " + balance + " smsc.ru", 1, "", "", 8, "", "");
        };

        task.run();

        Thread thread = new Thread(task);
        thread.start();
    }
}