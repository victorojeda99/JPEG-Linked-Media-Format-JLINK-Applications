<%-- 
    Document   : modifier_loader
    Created on : 03-may-2022, 10:59:13
    Author     : Victor Ojeda
--%>

<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="Modifier.AppUtilsModifier"%>
<%@page import="JLINKImage.JLINKImage"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.io.File"%>
<%@page import="java.io.PrintWriter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JLINK Modifier</title>
        <link rel="stylesheet" type="text/css" href=<%= Const.AppConst.CSS_SYLE_PATH %> />       
    </head>
    <body>
        <div class="header">
            <div class="header_left">
                <h1>JLINK APPLICATION | Modifier</h1>
            </div>
            <div class="header_right">
                <img class="header_image" src=<%= Const.AppConst.HEADER_LOGO_PATH %> />
            </div>
        </div>
        <div class="main_container">          
            <div class="title">
                <h1>Building the JLINK file</h1>
            </div>
            <div class="container">
                <div id="loader"></div>
            </div>
        </div>

        <script>
            location.replace("/JLINKApplication/FinishModifierResult");
        </script>
    </body>
</html>
