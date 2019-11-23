package ru.tehnohelp.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

//    private static List<String> pages = new ArrayList<>();
//
//    static {
//        pages.add("computerHelper");
//        pages.add("i");
//
//        pages = pages.stream().map(i -> i.toLowerCase()).collect(Collectors.toList());
//    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        chain.doFilter(req, resp);
//        try {
//            HttpServletRequest req1 = (HttpServletRequest) req;
//            String url = req1.getRequestURI().substring(1).toLowerCase();
//            if (!url.isEmpty() && !pages.contains(url)) {
//            req.getRequestDispatcher("/main.html").forward(req, resp);
////                HttpServletResponse resp1 = (HttpServletResponse) resp;
////                resp1.sendRedirect("/i");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void destroy() {

    }
}