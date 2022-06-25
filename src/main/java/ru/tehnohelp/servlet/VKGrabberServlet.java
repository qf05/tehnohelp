package ru.tehnohelp.servlet;

import com.google.gson.JsonObject;
import ru.tehnohelp.wallpost.VKGrabber;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/grabber")
public class VKGrabberServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/VKGrabber.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        String pass = req.getParameter("pass");
        String link = req.getParameter("link");
        String date = req.getParameter("date");

        if (!"159357".equals(pass)) {
            print(resp, createJson("error", true, date));
            return;
        }

        VKGrabber grab = new VKGrabber();
        String newDate = grab.addDayToDate(date);
        String result = grab.sendPost(link, date);
        if ("OK".equals(result)) {
            print(resp, createJson("OK", false, newDate));
        } else {
            print(resp, createJson("ERROR", true, date));
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

    private JsonObject createJson(String message, boolean error, String date) {
        JsonObject json = new JsonObject();
        json.addProperty("message", message);
        json.addProperty("error", error);
        json.addProperty("date", date);
        return json;
    }
}
