<%-- 
    Document   : profile
    Created on : 20-Oct-2014, 21:51:23
    Author     : Fury
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>InstagrimTL</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
    </head>
    <body>
        <% java.util.LinkedList<String> info = (java.util.LinkedList<String>) 
            request.getAttribute("info"); 
            String userName = info.get(3);
        %>
        <h1>InstagrimTL ! </h1>
        <h2>Your world in Black and White</h2>
        <nav>
            <ul>
                <li class="nav"><a href="upload.jsp">Upload</a></li>
               
                <li class="nav"><a href="/InstagrimTL/editProfile.jsp">Edit my profile</a></li>
                <form method = "POST" action = "/InstagrimTL/Logout">
                    <input type = "submit" value ="Logout">
                </form> 
            </ul>
            
        </nav>
        
        <% String first_name = info.get(1); %>
        <% String second_name = info.get(0); %>
        <% String uemail = info.get(2); %>
        
        <p>Username is: <%=userName%></p>
        <p>First name: <%=first_name%></p>
        <p>Second name: <%=second_name%></p>
        <p>Email is: <%=uemail%></p>
        
        
        <footer>
            <ul>
                <li class="footer"><a href="/InstagrimTL">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
