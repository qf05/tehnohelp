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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String theme = req.getParameter("theme");
        String message = req.getParameter("message");
        String sendMessage = MessageUtils.createMessage(name,phone,theme,message);
        boolean isSendMail = false;// EmailMessage.sendMessage(sendMessage);
        boolean isSendVk = false;//VkMessage.sendMessage(sendMessage);
        int i = (int) (Math.random()*4);
        if (i % 2 == 0){
            isSendVk=true;
        }
//        System.out.println( isSendMail + "  " + isSendVk);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        JsonObject json = new JsonObject();
        if (isSendMail||isSendVk){
            json.addProperty("message", "Ваша заявка принята");
            json.addProperty("err", "false");
        }else {
            json.addProperty("message", "Ошибка отправки");
            json.addProperty("err", "true");
        }
        PrintWriter out = resp.getWriter();
        out.write(json.toString());
        out.flush();
    }
}
