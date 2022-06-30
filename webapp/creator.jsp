<%-- 
    Document   : creator
    Created on : 14-mar-2022, 22:44:44
    Author     : Victor Ojeda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JLINK Creator</title>
        <link rel="stylesheet" type="text/css" href=<%= Const.AppConst.CSS_SYLE_PATH %> />    
    </head>
    <body>
        <div class="header">
            <div class="header_left">
                <h1>JLINK APPLICATION | Creator</h1>
            </div>
            <div class="header_right">
                <img class="header_image" src=<%= Const.AppConst.HEADER_LOGO_PATH %> />
            </div>
        </div>
        <div class="main_container">          
            <div class="title">
                <h1>Create a JLINK file</h1>
            </div>
            <div class="container">
                <div class="column">
                    <table>
                    <form method="post" action="InitCreator", enctype = "multipart/form-data">
                        <tr><td>File information: </td></tr>
                        <tr><td>File name: </td><td><input type="text" name="file_name"  required/></td></tr>
                        <tr><td>JLINK File Title: </td><td><input type="text" name="file_title"/></td></tr>
                        <tr><td>JLINK File Description: </td><td><textarea name="file_description"/></textarea></td></tr>
                        <tr><td></br>Add first image: </td></tr>
                        <tr><td>Upload image: </td><td><input type="file" name="image" id="imgInp" required/></td></tr>
                        <tr><td>Title: </td><td><input type="text" name="title" required/></td></tr>
                        <tr><td>Description: </td><td><textarea name="description"/></textarea></td></tr> 
                        <tr><td><input type="submit" value="Add LINK" /></td></tr>    
                    </form>
                    </table>
                </div>
                <div class="column">
                    <img class="image" id="pre-img" src="#"/>
                </div>
            </div>
        </div>
        <script type="text/javascript" src=<%= Const.AppConst.JS_PREVIEW_IMAGE_PATH %>></script>
    </body>
</html>
