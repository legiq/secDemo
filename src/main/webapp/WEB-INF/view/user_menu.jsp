<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2 style="color: dimgrey">Enter your message</h2>

    <form name="fileform" method="post" action="uploadServlet" enctype="multipart/form-data">
        <table bgcolor="#62b294">

        <tr><td>Message: <input name="message" placeholder="Enter your message"/></td></tr>
        <tr><td>File: <input type="file" name="image" size="50"/></td></tr>
        <tr><td><input type="submit" value="Save"></td></tr>
        </table>
    </form>
    <form name="logoutform" method="post" action="logout">
        <input type="submit" value="Logout">
    </form>

    <form name="fileselect" method="get" action="uploadServlet" enctype="multipart/form-data" >
        <table align="center" bgcolor="#81b27c">
        <%Iterator itr;
        Iterator iter;
        Iterator itrt;
        List imageList = (List)request.getAttribute("imageList");
        List indexList = (List)request.getAttribute("indexList");
        List authorList = (List)request.getAttribute("authorList");
        if (imageList!=null) {
            Collections.reverse(imageList);
            Collections.reverse(indexList);
            Collections.reverse(authorList);
            iter=indexList.iterator();
            itrt=authorList.iterator();
            for (itr=imageList.iterator(); itr.hasNext(); )
            {
                String author = (String) itrt.next();
                int i = (int) iter.next();
                if(i==1) {
        %>
        <tr><td style="color: dimgrey"><%=author%></td><td><img src="data:;base64,<%=itr.next()%>"/></td></tr>
        <%      } else if(i==0) {
                    %>
                    <tr><td style="color: dimgrey"><%=author%></td><td align="center"  style="font-size: 14pt"><%=itr.next()%></td></tr>
         <%      }
            }
        }%>
        </table>
    </form>
</body>
</html>
