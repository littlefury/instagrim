<%-- 
    Document   : error
    Created on : 09-Oct-2014, 18:43:54
    Author     : Fury
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    </body>
</html>
