package ru.tehnohelp.message;

import jssc.*;
import ru.tehnohelp.message.util.MessageUtils;

public class ModemSMS {

    private static final String SMS_TO_NUMBER = MessageUtils.loadPassword(MessageUtils.SMS_TO_NUMBER);

    public static boolean sendMessage(String message) {
        message = "Zayavka s sayta " + message;
        String[] retPorts = SerialPortList.getPortNames();

        SerialPort serialPort = null;
        for (String retPort : retPorts) {
            serialPort = new SerialPort(retPort);
            if ("COM13".equals(serialPort.getPortName())) {  //"COM4"); указываем второй порт
                break;
            }
        }
        if (serialPort == null) {
            return false;
        }

        System.out.println(serialPort.getPortName());
        if (openSerialPort(serialPort)) {
            System.out.println("Open");
            System.out.println("For close type: \nclose");
        } else {
            System.out.println("Not open");
        }

        try {
//            Scanner scanner = new Scanner(System.in);
            serialPort.writeBytes("AT+cmgf=1\r".getBytes());
            serialPort.writeBytes(("at+cmgs=\"+" + SMS_TO_NUMBER + "\"\r").getBytes());
            serialPort.writeBytes((message + "\032").getBytes());//calling ctrl+z
            Thread.sleep(300);
            if (serialPort.isOpened()) {
                serialPort.closePort();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean openSerialPort(SerialPort serialPort) {
        try {
            //Открываем порт
            if (serialPort.isOpened())
                serialPort.closePort(); //Здесь бывает ситуация Busy, которую не обработаешь - только если ожидать
            serialPort.openPort();
            //Выставляем параметры
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //Включаем аппаратное управление потоком
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            PortReader portReader = new PortReader(serialPort);
            serialPort.addEventListener(portReader, SerialPort.MASK_RXCHAR);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class PortReader implements SerialPortEventListener {

        SerialPort serialPort;

        public PortReader(SerialPort serialPort) {
            this.serialPort = serialPort;
        }

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    byte[] data = serialPort.readBytes(event.getEventValue());
                    for (byte b : data) {
                        System.out.println(b);
                    }
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

}
