<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>admin menu</title>
</head>
<body>
<h2 style="color: dimgrey">Edit panel</h2>

<form name="editform" method="post" action="editServlet">
    <table bgcolor="#8bb296">
    <tr><td>Delete message: <input name="idMessageToDelete" placeholder="Enter id of message"/></td></tr>
    <tr>
        <td>
            Edit text message: <input name="idMessageToUpdate" placeholder="Enter id of message"/>
        </td>
        <td>
            <input name="messageToUpdate" placeholder="Enter new text"/>
        </td>
    </tr>
    <tr><td><input type="submit" value="Update"></td></tr>
    </table>
</form>
<form name="logoutform" method="post" action="logout">
    <input type="submit" value="Logout">
</form>

<form name="fileselect" method="get" action="editServlet" enctype="multipart/form-data" >
    <table align="center" bgcolor="#81b27c">
        <%Iterator itr;
            Iterator iter;
            Iterator itrt;
            Iterator itert;
            List imageList = (List)request.getAttribute("imageList");
            List indexList = (List)request.getAttribute("indexList");
            List authorList = (List)request.getAttribute("authorList");
            List idList = (List)request.getAttribute("idList");
            if (imageList!=null) {
                Collections.reverse(imageList);
                Collections.reverse(indexList);
                Collections.reverse(authorList);
                Collections.reverse(idList);
                iter=indexList.iterator();
                itrt=authorList.iterator();
                itert=idList.iterator();
                for (itr=imageList.iterator(); itr.hasNext(); )
                {
                    int id = (int) itert.next();
                    int i = (int) iter.next();
                    String author = (String) itrt.next();
                    if(i==1) {
        %>
        <%--        <img src="data:image/jpg;base64,<%=itr.next()%>"/><br>--%>
        <tr><td style="color: bisque"><%=id%></td><td style="color: dimgrey"><%=author%></td><td><img src="data:;base64,<%=itr.next()%>"/></td></tr>
        <%      } else if(i==0) {
        %>
        <tr><td style="color: bisque"><%=id%></td><td style="color: dimgrey"><%=author%></td><td align="center"  style="font-size: 14pt"><%=itr.next()%></td></tr>
        <%      }
        }
        }%>
    </table>
</form>
</body>
</html>
