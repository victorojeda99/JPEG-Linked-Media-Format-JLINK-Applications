<%-- 
    Document   : error
    Created on : 31-may-2022, 15:26:12
    Author     : Victor Ojeda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
        <link rel="stylesheet" type="text/css" href=<%= Const.AppConst.CSS_SYLE_PATH %> />   
    </head>
    <body>
        <div class="header">
            <div class="header_left">
                <h1>JLINK APPLICATION | Error</h1>
            </div>
            <div class="header_right">
                <img class="header_image" src=<%= Const.AppConst.HEADER_LOGO_PATH %> />
            </div>
        </div>
        <div class="main_container">          
            <div class="title">
                <h1>Oops!</h1>
            </div>
            <div class="container">
                <p>There has been an error reading the file, if the error persists please delete it.</p>
                <a href ="menu.jsp"><button class="button-link">Return to menu</button></a>
            </div>
        </div>
    </body>
</html>
