<%--
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.InstagrimTL.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <title>InstagrimTL</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <header>
            <h1>InstagrimTL ! </h1>
            <h2>Your world in Black and White</h2>
        </header>
        <nav>
            <ul>

               
                <li><a href="upload.jsp">Upload</a></li>
                    <%
                        
                        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        if (lg != null) {
                            String UserName = lg.getUsername();
                            if (lg.getlogedin() == true){
                    %>

                <li><a href="/InstagrimTL/Images/<%=lg.getUsername()%>">Your Images</a></li>
                <li><a href="/InstagrimTL/Profile/<%=lg.getUsername()%>">Your profile</a></li>
                <form method = "POST" action = "/InstagrimTL/Logout">
                    <input type = "submit" value ="Logout">
                </form> 
                    <%}
                        }else{
                                %>
                            <li><a href="register.jsp">Register</a></li>
                            <li><a href="login.jsp">Login</a></li>
                    <% } %>
            </ul>
        </nav>
        <footer>
            <ul>
                <li class="footer"><a href="/InstagrimTL">Home</a></li>
                
            </ul>
        </footer>
    </body>
</html>
