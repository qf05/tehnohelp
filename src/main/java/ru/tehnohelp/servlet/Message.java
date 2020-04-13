package ru.tehnohelp.servlet;

import com.google.gson.JsonObject;
import ru.tehnohelp.service.EmailMessage;
import ru.tehnohelp.service.MessageUtils;
import ru.tehnohelp.service.VkMessage;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String theme = req.getParameter("theme");
        String message = req.getParameter("message");
        String sendMessage = MessageUtils.createMessage(name, phone, theme, message);
        boolean isSendVk = VkMessage.sendMessage(sendMessage);
        boolean isSendMail = EmailMessage.sendMessage(sendMessage);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        JsonObject json = new JsonObject();
        if (isSendVk) {
            json.addProperty("message", "Ваша заявка принята");
            json.addProperty("err", "false");
        } else {
//            boolean isSendMail = EmailMessage.sendMessage(sendMessage);
//            if (isSendMail) {
//                json.addProperty("message", "Ваша заявка принята");
//                json.addProperty("err", "false");
//            } else {
                json.addProperty("message", "Ошибка отправки");
                json.addProperty("error", "true");
//            }
        }
        PrintWriter out = resp.getWriter();
        out.write(json.toString());
        out.flush();
    }
}
