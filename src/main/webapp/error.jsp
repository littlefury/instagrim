<%-- 
    Document   : error
    Created on : 09-Oct-2014, 18:43:54
    Author     : Fury
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
    </head>
    <body>
        
        <article>
            <h1>Error</h1>
            <a href="/Instagrim">Home page</a>
            
            <p>
                <%
                    String error = (String) request.getAttribute("error");
                    out.print(error);
                %>
            </p>
    
        </article>
            
            <nav>
            <ul>

               
                <li><a href="upload.jsp">Upload</a></li>
                    <%
                        
                        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        if (lg != null) {
                            String UserName = lg.getUsername();
                            if (lg.getlogedin() == true){
                    %>

                <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
              
                <form method = "POST" action = "/Instagrim/Logout">
                    <input type = "submit" value ="Logout">
                </form> 
                    <%}
                            }else{
                                %>
                 <li><a href="register.jsp">Register</a></li>
                <li><a href="login.jsp">Login</a></li>
                <%
                                        
                            
                    }%>
            </ul>
        </nav>
    </body>
</html>
