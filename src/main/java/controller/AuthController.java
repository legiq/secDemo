package controller;

import User.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class AuthController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        String login = request.getParameter("username");
        String pass = request.getParameter("password");

        try
        {
            Cookie passCookie = new Cookie("password", pass);
            response.addCookie(passCookie);

            String myDriver = "com.mysql.cj.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost:3306/mydb?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "root");

            String query = "SELECT * FROM users where username = '" + login + "'" + "and" + " password = '" + pass + "'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            String username = "def";
            String password = "def";
            String role = User.ROLE.UNKNOWN.toString();
            while (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
                role = rs.getString("role");
            }
//                if (username.equals(login) && password.equals(pass) && role.equals(User.ROLE.ADMIN.toString())) {
            if (role!=null && role.equals(User.ROLE.ADMIN.toString())) {
                session.setAttribute("username", username);
                session.setAttribute("password", password);
                session.setAttribute("role", role);
                request.getRequestDispatcher("WEB-INF/view/admin_menu.jsp").forward(request, response);
                //} else if (username.equals(login) && password.equals(pass) && role.equals(User.ROLE.USER.toString())) {
            } else if (role!=null && role.equals(User.ROLE.USER.toString())) {
                    session.setAttribute("username", username);
                    session.setAttribute("password", password);
                    session.setAttribute("role", role);
                    request.getRequestDispatcher("WEB-INF/view/user_menu.jsp").forward(request, response);
            } else {
                passCookie.setMaxAge(0);
                response.addCookie(passCookie);
                request.getRequestDispatcher("WEB-INF/loginError.jsp").forward(request, response);
            }
            rs.close();
            st.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            e.printStackTrace();
        }
    }
}
