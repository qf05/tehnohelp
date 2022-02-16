package ru.tehnohelp.servlet;

import com.google.gson.JsonObject;
import ru.tehnohelp.message.EmailMessage;
import ru.tehnohelp.message.VkMessage;
import ru.tehnohelp.message.util.MessageUtils;
import ru.tehnohelp.message.SmsMessage;
import ru.tehnohelp.wallpost.Command;
import ru.tehnohelp.wallpost.CommandService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/sendmessage")
public class Message extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/index.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        //String theme = req.getParameter("theme");
        String message = req.getParameter("message");

        if ("+7 (921) 924-12-24".equals(phone)){// && "post".equals(theme)) {
            Command command = Command.getByValue(message.toLowerCase().trim());
            if (command == null) {
                print(resp, createJson("Ошибка команды", true));
                System.out.println("not found command");
            } else {
                String result = CommandService.managerCommand(command);
                print(resp, createJson(result, result.contains("error")));
                System.out.println(result);
            }
            return;
        }
        String theme = "";
        String sendMessage = MessageUtils.createMessage(name, phone, theme, message);
        boolean isSendVk = VkMessage.sendMessage(sendMessage);
        boolean isSendMail = EmailMessage.sendMessage(sendMessage);
        boolean isSendSms = SmsMessage.sendMessage(phone);
        System.out.println("VK = " + isSendVk);
        System.out.println("Mail = " + isSendMail);
        System.out.println("SMS = " + isSendSms);
        if (( isSendVk || isSendMail ) && isSendSms) {
            print(resp, createJson("Ваша заявка принята", false));
        } else {
            print(resp, createJson("Ошибка отправки", true));
        }
    }

    private void print(HttpServletResponse resp, JsonObject json) {
        try {
            PrintWriter out = resp.getWriter();
            out.write(json.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject createJson(String message, boolean error) {
        JsonObject json = new JsonObject();
        json.addProperty("message", message);
        json.addProperty("error", error);
        return json;
    }
}
