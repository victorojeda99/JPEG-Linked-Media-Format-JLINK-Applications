/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Modifier;

import JLINKImage.JLINKImage;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "ActionModifier", urlPatterns = {"/ActionModifier"})
public class ActionModifier extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String file_name;
        JLINKImage image;
        AppUtilsModifier utils;  
        String file_title, file_description, storage_date;
        
        try{
            image = (JLINKImage) getServletContext().getAttribute("image");
            utils = new AppUtilsModifier();
            file_name = (String) getServletContext().getAttribute("file_name");
            file_title = (String) getServletContext().getAttribute("file_title");
            file_description = (String) getServletContext().getAttribute("file_description");
            storage_date = (String) getServletContext().getAttribute("storage_date");

            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>JLINK Modifier</title>");   
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + Const.AppConst.CSS_SYLE_PATH + "\" />");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class=\"header\">\n" +
    "            <div class=\"header_left\">\n" +
    "                <h1>JLINK APPLICATION | Modifier</h1>\n" +
    "            </div>\n" +
    "            <div class=\"header_right\">\n" +
    "                <img class=\"header_image\" src=\"" + Const.AppConst.HEADER_LOGO_PATH + "\" />\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"main_container\">          \n" +
    "            <div class=\"title\">\n" +
    "                <h1>Modifing file '" + file_name + ".jpeg'</h1>\n" +
    "            </div>\n" +
    "            <div class=\"container\">\n" +
    "                <div class=\"column\">\n" +
    "                    <p>File information:</p>\n" +
    "                    <table id=\"content_table\">\n" +
    "                    <tr class=\"table-main-row\"><th>File name</th>\n" +
    "                        <th>Title</th>\n" +
    "                        <th>Description</th>\n" +
    "                        <th>Storage date</th></tr>\n");           
                out.println("<td>" + file_name +"</td>");
                out.println("<td>" + file_title +"</td>");
                out.println("<td>" + file_description +"</td>");
                out.println("<td>" + storage_date +"</td>");
                out.println("                    </table></br>\n" +
    "                    <p>Contains (By clicking on the corresponding row you can modify the configuration of each scene):</p>\n" +
    "                    <table id=\"content_table\">\n" +
    "                        <tr class=\"table-main-row\">\n" +
    "                            <th>Title</th>\n" +
    "                            <th>Description</th>\n" +
    "                            <th>Previous image</th>\n" +
    "                        </tr>\n" +
    "                        <tr>");
                utils.containsTable(out, image);
                out.println("</table>");
                out.println("<div class=\"text-and-button\">");
                out.println("<p>You can also add or delete an image, or finish the modification and create the new file&nbsp;&nbsp;&nbsp;&nbsp;</p>");
                out.println("<form method=\"post\" action=\"FinishModifier\">\n" +
    "                        <input type=\"submit\" value=\"Finish\" /></td><td>                   \n" +
    "                    </form>");
                out.println("</div>");
                out.println("<strong>Add an image:</strong>\n" +
    "                    <table>\n" +
    "                        <form method=\"post\" action=\"SelectSpriteModifier\", enctype = \"multipart/form-data\">\n" +
    "                            <tr><td>Upload image: </td><td><input type=\"file\" name=\"image\" id=\"imgInp\" required/></td></tr>\n" +
    "                            <tr><td>Title: </td><td><input type=\"text\" name=\"title\" required/></td></tr>\n" +
    "                            <tr><td>Description: </td><td><textarea name=\"description\"/></textarea></td></tr>\n" +
    "                            <tr><td>Previous image: </td><td><select name=\"previous_image\"  required>");
                utils.formOptions(out, image);
                out.println("</select></td></tr>\n" +
    "                            <tr><td><input type=\"submit\" value=\"Next\" /></td><td>                   \n" +
    "                        </form>\n" +
    "                    </table>");
                out.println("</br><strong>Delete an image:</strong>\n" +
    "                    <table>\n" +
    "                        <form method=\"post\" action=\"DeleteLink\">\n" +
    "                            <tr><td>Select image: </td><td><select name=\"deleted_image\"  required>");
                utils.formDelete(out, image);
                out.println("</select></td></tr>\n" +
    "                            <tr><td><input type=\"submit\" value=\"Next\" /></td><td>                   \n" +
    "                        </form>\n" +
    "                    </table>");
                out.println("</div>\n" +
    "                <div class=\"column\">\n" +
    "                    <img class=\"image\" id=\"pre-img\" src=\"#\"/>\n" +
    "                    <img class=\"image\" id=\"image\" src='#'>\n" +
    "                </div>     \n" +
    "            </div>\n" +
    "        </div>");
                out.println("<script type=\"text/javascript\" src=\"" + Const.AppConst.JS_PREVIEW_IMAGE_PATH + "\"></script>");
                out.println("<script type=\"text/javascript\" src=\"" + Const.AppConst.JS_CONTAIN_TABLE_MODIFIER_PATH + "\"></script>");
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
            processRequest(request, response);
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
            processRequest(request, response);
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
