/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Viewer;

import JLINKImage.JLINKImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Victor Ojeda
 */
@WebServlet(name = "Viewer", urlPatterns = {"/Viewer"})
public class Viewer extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        
        String file_name;
        JLINKImage image;
        AppUtilsViewer utils;
        String appPath;
        Connection connection;
        PreparedStatement statement;  
        ResultSet rs;    
        String sql;    
        String title, description, storage_date;
        
        try{
            file_name = request.getParameter("file_name");
            image = new JLINKImage();
            utils = new AppUtilsViewer();
            appPath = request.getServletContext().getRealPath("");
            image.setAppPath(appPath);
            image.setIsMain(true);
            utils.setJLINKImage(image, file_name);

            sql = "select * from image where filename like '%"+file_name+"%'";
            connection = DriverManager.getConnection(Const.AppConst.DATABASE_CONNECTION);
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            rs.next();
            title = rs.getString("title");
            description = rs.getString("description");
            storage_date = rs.getString("storage_date"); 

            utils.viewerScript(image);
            this.show(image);
            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>JLINK Viewer</title>");        
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + Const.AppConst.CSS_SYLE_PATH + "\" />");
                out.println("</head>");
                out.println("<body onload=\"initViewer()\">");
                out.println("<div class=\"header\">\n" +
    "            <div class=\"header_left\">\n" +
    "                <h1>JLINK APPLICATION | Viewer</h1>\n" +
    "            </div>\n" +
    "            <div class=\"header_right\">\n" +
    "                <img class=\"header_image\" src=\"" + Const.AppConst.HEADER_LOGO_PATH + "\" />\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"main_container\">          \n" +
    "            <div class=\"title\">\n" +
    "                <h1>Viewing file '" + file_name + ".jpeg'</h1>\n" +
    "            </div>\n" +
    "            <div class=\"container\">");
                out.println("<div class=\"column\" id=\"column-view\">");
                out.println("<div id=\"view\" data-scene=\"" + image.getImage_Href() + "\">");  
                this.putImgElements(image, out);
                out.println("<div class=\"div-text\">");
                out.println("<img id='text-button' class=\"text-button\" src=\"appImages/text-button.svg\" alt=\"Text button\">");
                out.println("</div>");
                out.println("<div class=\"mask\" id=\"mask\">");
                out.println("<div class=\"image-text\" id=\"image-text\">");
                out.println("<div class=\"text-region\" id=\"text-region\">");
                out.println("<div class=\"text-close\">");
                out.println("<img id='close-icon' class=\"close-icon\" src=\"appImages/close-icon.svg\" alt=\"Close\">");            
                out.println("</div>");
                out.println("<div class=\"title-region\">");
                out.println("<p class=\"scene-title\" id=\"scene-title\"></p>");
                out.println("</div>");
                out.println("<div class=\"description-region\" id=\"description-region\">");
                out.println("<p class=\"scene-description\" id=\"scene-description\"></p>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("<img id='back-button' class=\"back-button\" src=\"" + Const.AppConst.BACK_BUTTON_PATH + "\" alt=\"Back button\">");
                out.println("</div>");
                out.println("<div class=\"column\">");
                out.println("<div class=\"viewer-text\">");
                out.println("<strong>File information:</strong>");
                out.println("<p>Title: <q>" + title +"</q></p>");
                out.println("<p>Description: <q>" + description +"</q></p>");
                out.println("<p>Storage date: <q>" + storage_date +"</q></p>");
                out.println("</br>");
                out.println("<a href =\"viewerList.jsp\"><button class=\"button-link\">View another file</button></a></br>");
                out.println("<a href =\"menu.jsp\"><button class=\"button-link\">Return to menu</button></a>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("<script type=\"text/javascript\" src=\"" + Const.AppConst.JS_VIEWER_PATH + "\"></script>");
                out.println("</body>");
                out.println("</html>");
            }
        }catch (SQLException e){
            RequestDispatcher rd;
            
            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/error_database.jsp");
                rd.forward(request, response);
                out.println("</html>");
            }
        }catch (Exception e){
            RequestDispatcher rd;
            
            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/error_reading.jsp");
                rd.forward(request, response);
                out.println("</html>");
            }
        }
    }

    
    public void show(JLINKImage image){
        System.out.println("Label: " + image.getLabel());
        System.out.println("Title: " + image.getTitle());
        System.out.println("Previous image: " + image.getPrevious_image());
        System.out.println("Link to: " + image.getLink_to());
        System.out.println("Sprite: " + image.getLink_sprite());
        System.out.println("-------------");
        for(JLINKImage aux: image.getLinked_images()){
            this.show(aux);   
        }
    }
    
    public void putImgElements(JLINKImage image, PrintWriter out){
        
        out.println("<img class=\"viewer-image\" id=\"" + image.getImage_Href() + "\" link-to=\"" + image.getPrevious_image() + "\""
                + " src=\"" + Const.AppConst.VIEW_DIR + File.separator + image.getImage_Href().replace("'", "\'") + ".jpeg\" alt=\"" + image.getNote().replace("'", "\'")+ "\">");
        for(JLINKImage aux: image.getLinked_images()){
            if(!image.getLinked_images().isEmpty()){
                String sprite = aux.getImage_Href();
                sprite = Const.JLINKConst.SPRITE_LABEL + sprite.replaceAll("[^0-9]", "");
                out.println("<img class=\"viewer-sprite\" id=\""+ sprite +"\" src=\"" + Const.AppConst.VIEW_DIR + File.separator + image.getLink_sprite() + ".jpeg\">");
            }
            this.putImgElements(aux, out);   
        }
    }
    
    
//    public void show(JLINKImage image){
//        System.out.println();
//        System.out.println("label: "+image.getLabel());
//        System.out.println("href: "+image.getImage_Href());
//        System.out.println("previous: "+image.getPrevious_image());
//        System.out.println("sprite: "+image.getLink_sprite());
//        
//        for(JLINKImage aux: image.getLinked_images().values()){
//            show(aux);   
//        }
//    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
