package controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@MultipartConfig
public class DataController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String author = (String) session.getAttribute("username");
        String message = request.getParameter("message");
        InputStream inputStream = null;
        Part filePart = request.getPart("image");

        Boolean messageCheck = message.isEmpty();
        Integer filePartCheck = (int) filePart.getSize();

        if(messageCheck.equals(true) && filePartCheck.equals(0)) {
            select(request, response);
        } else {

            if (filePart != null) {
                inputStream = filePart.getInputStream();
            }

            try {
                String myDriver = "com.mysql.cj.jdbc.Driver";
                String myUrl = "jdbc:mysql://localhost:3306/mydb?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
                Class.forName(myDriver);
                Connection conn = DriverManager.getConnection(myUrl, "root", "root");
                Statement st = conn.createStatement();

                String sql = "INSERT INTO data (author, image) values (?, ?)";

                PreparedStatement imageQuery = conn.prepareStatement(sql);

                if (!filePartCheck.equals(0)) {
                    imageQuery.setString(1, author);
                    imageQuery.setBlob(2, inputStream);
                    imageQuery.executeUpdate();
                }

                if (!message.isEmpty()) {
                    String query = "INSERT INTO data (author, message) values ('" + author + "', '" + message + "')";
                    st.executeUpdate(query);
                }
                inputStream.close();
                st.close();
                conn.close();
            } catch (Exception e) {
                System.err.println("Got an exception! ");
                e.printStackTrace();
            } finally {
                select(request, response);
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        select(request, response);
    }

    protected void select(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String actualPass = "";
        Cookie[] passCookie = request.getCookies();
        HttpSession session = request.getSession();

        String pass = (String) session.getAttribute("password");

        for(int i = 0; i<passCookie.length; i++) {
            if(passCookie[i].getName().equals("password")){
                actualPass = passCookie[i].getValue();
            }
        }

            if (actualPass.equals(pass)) {

                List authorList = new ArrayList();
                List indexList = new ArrayList();
                List imageList = new ArrayList();

                try {
                    String myDriver = "com.mysql.cj.jdbc.Driver";
                    String myUrl = "jdbc:mysql://localhost:3306/mydb?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
                    Class.forName(myDriver);
                    Connection conn = DriverManager.getConnection(myUrl, "root", "root");

                    String sql = "SELECT author,message,image FROM data";
                    PreparedStatement imageSelect = conn.prepareStatement(sql);
                    ResultSet result = imageSelect.executeQuery();

                    while (result.next()) {
                        String author = result.getString("author");
                        Blob blob = result.getBlob("image");
                        String str = result.getString("message");

                        if (blob != null) {
                            InputStream inputStream = blob.getBinaryStream();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[4096];
                            int bytesRead = -1;

                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            byte[] imageBytes = outputStream.toByteArray();
                            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                            imageList.add(base64Image);
                            authorList.add(author);
                            indexList.add(1);
                            inputStream.close();
                            outputStream.close();
                        } else if (str != null) {
                            imageList.add(str);
                            authorList.add(author);
                            indexList.add(0);
                        }
                    }
                    result.close();
                    imageSelect.close();
                    conn.close();
                } catch (Exception e) {
                    System.err.println("Got an exception! ");
                    System.err.println(e);
                    e.printStackTrace();
                }
                request.setAttribute("authorList", authorList);
                request.setAttribute("indexList", indexList);
                request.setAttribute("imageList", imageList);
                RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/view/user_menu.jsp");

                if (dispatcher != null) {
                    dispatcher.forward(request, response);
                }
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                if (dispatcher != null) {
                    dispatcher.forward(request, response);
                }
            }
    }
}
