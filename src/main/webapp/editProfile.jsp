<%-- 
    Document   : editProfile
    Created on : 25-Oct-2014, 23:33:07
    Author     : Fury
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <header>
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
       
        <article>
            <h3>Edit your profile</h3>
            <form method="POST"  action="/Instagrim/EditProfile">
                <ul>
                    
                    <li>New email <input type="email" name="new_email"> </li>
                    <li> New first name <input type="text" name="new_first_name"</li>
                    <li>New last name <input type="text" name="new_last_name"</li>
                </ul>
                <br/>
                <input type="Submit" value="EditProfile"> 
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
