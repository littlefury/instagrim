<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
    Author     : Administrator
--%>

<%@page import="uk.ac.dundee.computing.aec.InstagrimTL.stores.LoggedIn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>InstagrimTL</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
    </head>
    <body>
        <h1>InstagrimTL ! </h1>
        <h2>Your world in Black and White</h2>
        <nav>
            <ul>
                <%              
                        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        if (lg != null) {
                            String UserName = lg.getUsername();
                            if (lg.getlogedin() == true){
                            }
                        }%>
                <li class="nav"><a href="upload.jsp">Upload</a></li>
                <li><a href="/InstagrimTL/Profile/<%=lg.getUsername()%>">Your profile</a></li>
                <form method = "POST" action = "/InstagrimTL/Logout">
                    <input type = "submit" value ="Logout">
                </form> 
            </ul>
            
        </nav>
 
        <article>
            <h3>File Upload</h3>
            <form method="POST" enctype="multipart/form-data" action="Image">
                File to upload: <input type="file" name="upfile"><br/>

                <br/>
                <input type="submit" value="Press"> to upload the file!
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/InstagrimTL">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
