/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Creator;

import JLINKImage.JLINKImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Victor Ojeda
 */
@WebServlet(name = "AddLINK", urlPatterns = {"/AddLINK"})
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB 
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50,   // 50MB
                 location="/")
public class AddLINK extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        
        
        JLINKImage image, link_image;
        AppUtilsCreator utils;
        String file_name, file_title, file_description, storage_date, sprite_x, sprite_y;
        
        try{
            image = (JLINKImage) getServletContext().getAttribute("image");
            link_image = (JLINKImage) getServletContext().getAttribute("link_image");
            sprite_x = request.getParameter("sprite_x");
            sprite_y = request.getParameter("sprite_y");
            if(sprite_x != null){
                link_image.setLink_region_X(sprite_x);
            }
            if(sprite_y != null){
                link_image.setLink_region_Y(sprite_y);
            }
            utils = new AppUtilsCreator();
            utils.insertLINK(image, link_image);
            getServletContext().setAttribute("image", image);

            file_name = (String) getServletContext().getAttribute("file_name");
            file_title = (String) getServletContext().getAttribute("file_title");
            file_description = (String) getServletContext().getAttribute("file_description");
            storage_date = (String) getServletContext().getAttribute("storage_date");

            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>JLINK Creator</title>");   
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + Const.AppConst.CSS_SYLE_PATH + "\" />");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class=\"header\">\n" +
    "            <div class=\"header_left\">\n" +
    "                <h1>JLINK APPLICATION | Creator</h1>\n" +
    "            </div>\n" +
    "            <div class=\"header_right\">\n" +
    "                <img class=\"header_image\" src=\"" + Const.AppConst.HEADER_LOGO_PATH + "\" />\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"main_container\">          \n" +
    "            <div class=\"title\">\n" +
    "                <h1>Creating file '" + file_name + ".jpeg'</h1>\n" +
    "            </div>\n" +
    "            <div class=\"container\">\n" +
    "                <div class=\"column\">\n" +
    "                    <p>File information:</p>\n" +
    "                    <table id=\"content_table\">\n" +
    "                    <tr class=\"table-main-row\"><th>File name</th>\n" +
    "                        <th>Title</th>\n" +
    "                        <th>Description</th>\n" +
    "                        <th>Storage date</th></tr>\n" +
    "                    <tr>");           
                out.println("<td>" + file_name +"</td>");
                out.println("<td>" + file_title +"</td>");
                out.println("<td>" + file_description +"</td>");
                out.println("<td>" + storage_date +"</td>");
                out.println("                    </table></br>\n" +
    "                    <p>Contains:</p>\n" +
    "                    <table id=\"content_table\">\n" +
    "                        <tr class=\"table-main-row\">\n" +
    "                            <th>Title</th>\n" +
    "                            <th>Description</th>\n" +
    "                            <th>Previous image</th>\n" +
    "                        </tr>");
                this.containsTable(out, image);
                out.println("</table></br>");
                out.println("                    <p>Add an image:</p>\n" +
    "                    <table>\n" +
    "                        <form method=\"post\" action=\"SelectSprite\", enctype = \"multipart/form-data\">\n" +
    "                            <tr><td>Upload image: </td><td><input type=\"file\" name=\"image\" id=\"imgInp\" required/></td></tr>\n" +
    "                            <tr><td>Title: </td><td><input type=\"text\" name=\"title\" required/></td></tr>\n" +
    "                            <tr><td>Description: </td><td><textarea name=\"description\"/></textarea></td></tr>\n" +
    "                            <tr><td>Previous image: </td><td><select name=\"previous_image\"  required>");
                this.formOptions(out, image);
                out.println("</select></td></tr>\n" +
    "                            <tr><td><input type=\"submit\" value=\"Next\" /></td><td>                   \n" +
    "                        </form>\n" +
    "                    </table>\n" +
    "                </div>\n" +
    "                <div class=\"column\">\n" +
    "                    <img class=\"image\" id=\"pre-img\" src=\"#\"/>\n" +
    "                    <img class=\"image\" id=\"image\" src='#'>\n" +
    "                </div>     \n" +
    "            </div>\n" +
    "        </div>");
                out.println("<script type=\"text/javascript\" src=\"" + Const.AppConst.JS_PREVIEW_IMAGE_PATH + "\"></script>");
                out.println("<script type=\"text/javascript\" src=\"" + Const.AppConst.JS_CONTAIN_TABLE_PATH + "\"></script>");
                out.println("</body>");
                out.println("</html>");
            }
        }catch (Exception e){
            RequestDispatcher rd;
            
            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                out.println("</html>");
            }
        }        
    }
   
    
    public void containsTable(PrintWriter out, JLINKImage image){
        
        out.println("<tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" data-value=\"" + Const.AppConst.CREATOR_DIR + File.separator + image.getTitle().replace(" ", "_") + ".jpeg\">");
        out.println("<td>" + image.getTitle() +"</td>");
        out.println("<td>" + image.getNote() +"</td>");
        if(image.isIsMain()){
            out.println("<td>-</td></tr>");
        }else{
            out.println("<td>" + image.getPrevious_image().replace("_", " ") +"</td></tr>");
        }
        for(JLINKImage aux: image.getLinked_images()){
            this.containsTable(out, aux);        
        }
    }
    
    public void formOptions(PrintWriter out, JLINKImage image){
        
        if(image.isIsMain()){
            out.println("<option selected>"+ image.getTitle().replace(" ", "_") +"</option>");
        }else{
            out.println("<option>"+ image.getTitle().replace(" ", "_") +"</option>");
        }
        for(JLINKImage aux: image.getLinked_images()){
            this.formOptions(out, aux);        
        }
    }

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
            } catch (SQLException ex) {
                Logger.getLogger(AddLINK.class.getName()).log(Level.SEVERE, null, ex);
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
            } catch (SQLException ex) {
                Logger.getLogger(AddLINK.class.getName()).log(Level.SEVERE, null, ex);
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
