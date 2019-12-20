package controller;

import User.User;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class RegController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("username");
        String password = request.getParameter("password");

        if(!login.isEmpty() && !password.isEmpty()) {

            try {
                String myDriver = "com.mysql.cj.jdbc.Driver";
                String myUrl = "jdbc:mysql://localhost:3306/mydb?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
                Class.forName(myDriver);
                Connection conn = DriverManager.getConnection(myUrl, "root", "root");

                String sql = "INSERT INTO users (username, password, role) values (?, ?, ?)";

                PreparedStatement query = conn.prepareStatement(sql);
                query.setString(1, login);
                query.setString(2, password);
                query.setString(3, User.ROLE.USER.toString());
                query.executeUpdate();
                query.close();
                conn.close();

            } catch (Exception e) {
                System.err.println("Got an exception! ");
                e.printStackTrace();
            } finally {
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");

                if (dispatcher != null) {
                    dispatcher.forward(request, response);
                }
            }
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("registerError.jsp");

            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
        }
    }
}
