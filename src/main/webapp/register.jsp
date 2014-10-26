<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
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
        <header>
        <h1>InstagrimTL ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
     
       
        <article>
            <h3>Register as user</h3>
            <form method="POST"  action="Register">
                <ul>
                    <li>User Name <input type="text" name="username"></li>
                    <li>Password <input type="password" name="password"></li>
                    <li>Confirm password <input type="password" name="confirm_password"></li>
                    <li>Email <input type="email" name="email"></li>
                    <li>First name <input type="text" name="first_name"></li>
                    <li>Last name <input type="text" name="last_name"></li>
                </ul>
                <br/>
                <input type="Submit" value="Register"> 
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/InstagrimTL">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
