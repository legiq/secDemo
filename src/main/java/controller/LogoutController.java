package controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            session.invalidate();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        } catch(Exception e) {
            System.err.println("Got an exception! " + e.getMessage());
            e.printStackTrace();
        } finally {
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");

            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
        }
    }
}
