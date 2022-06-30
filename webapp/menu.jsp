<%-- 
    Document   : menu
    Created on : 14-mar-2022, 22:41:19
    Author     : Víctor Ojeda
--%>

<%@page import="java.io.File"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JLINK Menu</title>
        <link rel="stylesheet" type="text/css" href=<%= Const.AppConst.CSS_SYLE_PATH %> />
    </head>
    <body>
        <div class="header">
            <div class="header_left">
                <h1>JLINK APPLICATION | Menu</h1>
            </div>
            <div class="header_right">
                <img class="header_image" src=<%= Const.AppConst.HEADER_LOGO_PATH %> />
            </div>
        </div>
        <div class="main_container">
            <div class="container">
                <a href ="uploader.jsp" ><button class="button upload">Upload a JLINK file</button></a>
                <a href ="creator.jsp" ><button class="button creator">Create a JLINK file</button></a>
                <a href ="viewerList.jsp" ><button class="button viewer">View a JLINK file</button></a>
                <a href ="modifierList.jsp" ><button class="button modifier">Modify a JLINK file</button></a>
                <a href ="downloadList.jsp" ><button class="button downloader">Download a JLINK file</button></a>
                <a href ="deleteList.jsp" ><button class="button deleter">Delete a JLINK file</button></a>
                <div class="menu-text">
                    <h1>JPEG Linked Media Format (JLINK) Applications</h1>
                    <h2>A Degree Thesis</br>
                        Submitted to the Faculty of the</br>
                        Escola Tècnica d'Enginyeria</br>
                        de Telecomunicació de Barcelona</br>
                        Universitat Politècnica de Catalunya</br>
                        by</br>
                        Víctor Ojeda Franco
                    </h2>
                    <h2>In partial fulfilment</br>
                        of the requirements for the degree in</br>
                        TELECOMMUNICATIONS TECHNOLOGIES AND</br>
                        SERVICES ENGINEERING
                    </h2>
                </div>
            </div>
        </div>     
                <%
    File f;
    String appPath;
    
    try{
        appPath = request.getServletContext().getRealPath("");
        f = new File(appPath + File.separator + Const.AppConst.VIEW_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
        for(File file: f.listFiles()){
            if (!file.isDirectory()){
                file.delete();
            }
        }
        f = new File(appPath + File.separator + Const.AppConst.CREATOR_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
        for(File file: f.listFiles()){
            if (!file.isDirectory()){
                file.delete();
            }
        }
        f = new File(appPath + File.separator + Const.AppConst.MODIFIER_DIR);
        for(File file: f.listFiles()){
            if (!file.isDirectory()){
                file.delete();
            }
        }
        f = new File(appPath + File.separator + Const.AppConst.UPLOADER_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
        for(File file: f.listFiles()){
            if (!file.isDirectory()){
                file.delete();
            }
        }
    }catch (Exception e){
        RequestDispatcher rd;

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        rd = request.getRequestDispatcher("/error.jsp");
        rd.forward(request, response);
        out.println("</html>");
    }
    
%>
    </body>
</html>