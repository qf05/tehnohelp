package ru.tehnohelp.servlet;

import ru.tehnohelp.service.EmailMessage;
import ru.tehnohelp.service.MessageUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        EmailMessage.sendMessage(sendMessage);
        
        req.getRequestDispatcher("/index.html").forward(req, resp);
    }
}
