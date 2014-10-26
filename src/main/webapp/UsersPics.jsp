<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.InstagrimTL.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>InstagrimTL</title>
        <link rel="stylesheet" type="text/css" href="/InstagrimTL/Styles.css" />
    </head>
    <body>
        <header>
        
        <h1>InstagrimTL ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
        
        <nav>
            <ul>
                <li class="nav"><a href="/InstagrimTL/upload.jsp">Upload</a></li>
                  <%
                        
                        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        if (lg != null) {
                            String UserName = lg.getUsername();
                            if (lg.getlogedin() == true){
                            }
                        }%>

                <li class="nav"><a href="/InstagrimTL/Profile/<%=lg.getUsername()%>">Profile</a></li>
                <form method = "POST" action = "/InstagrimTL/Logout">
                    <input type = "submit" value ="Logout">
                </form> 
            </ul>
        </nav>
 
        <article>
            <h1>Your Pics</h1>
        <%
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) {
                Pic p = (Pic) iterator.next();

        %>
                <a href="/InstagrimTL/Image/<%=p.getSUUID()%>" ><img src="/InstagrimTL/Thumb/<%=p.getSUUID()%>"></a><br/>
                <a href="/InstagrimTL/Delete/<%=p.getSUUID()%>" >Delete </a>
                <a href="/InstagrimTL/Filter/<%=p.getSUUID()%>" >Red Filter</a></br><%

            }
        }
        %>
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/InstagrimTL">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
