/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Modifier;

import JLINKImage.JLINKImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
@WebServlet(name = "FinishModifierResult", urlPatterns = {"/FinishModifierResult"})
public class FinishModifierResult extends HttpServlet {

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
        
        JLINKImage image;
        AppUtilsModifier utils;
        Connection connection;
        PreparedStatement statement;  
        String sql;
        String file_name, file_title, file_description, storage_date;
        long startTime, endTime, duration;
        
        try{
            file_name = (String) getServletContext().getAttribute("file_name");
            if (!file_name.equals("MODIFICATION IS DONE")) {
                image = (JLINKImage) getServletContext().getAttribute("image");
                utils = new AppUtilsModifier();             
                file_name = utils.updateFileName(file_name, image.getAppPath());
                file_title = (String) getServletContext().getAttribute("file_title");
                file_description = (String) getServletContext().getAttribute("file_description");
                storage_date = (String) getServletContext().getAttribute("storage_date");
                startTime = System.nanoTime();
                utils.createFile(image, file_name);   
                endTime = System.nanoTime();
                duration = (endTime - startTime);
                System.out.println("Creatrion time in milliseconds: " + duration / 1000000);   
                
                connection = DriverManager.getConnection(Const.AppConst.DATABASE_CONNECTION);
                sql =  "INSERT INTO IMAGE (FILENAME, TITLE, DESCRIPTION, STORAGE_DATE) "
                         + "VALUES (?, ?, ?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, file_name);
                statement.setString(2, file_title);
                statement.setString(3, file_description);
                statement.setString(4, storage_date);
                statement.executeUpdate();
                
                getServletContext().setAttribute("file_name", "MODIFICATION IS DONE");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>JLINK Modifier</title>");
                    out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
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
        "                <h1>JLINK Modifier</h1>\n" +
        "            </div>\n" +
        "            <div class=\"container\">\n" +
        "                <div class=\"column\">\n" +
        "                    <p>The file has been created successfully.</p>\n" +
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
                    out.println("</table></br>\n" +
        "                    <p>Contains:</p>\n" +
        "                    <table id=\"content_table\">\n" +
        "                        <tr class=\"table-main-row\">\n" +
        "                            <th>Title</th>\n" +
        "                            <th>Description</th>\n" +
        "                            <th>Previous image</th>\n" +
        "                        </tr>\n");
                    utils.containsTable(out, image);
                    out.println("</table>\n" +
        "                    <p>The file has been saved in the application database. If you want to download the file press the download button.</p>\n" +
        "                    <a href=\"" + Const.AppConst.SAVE_DIR + File.separator + file_name + ".jpeg\" download>\n" +
        "                        <button class=\"button-link\"><i class=\"fa fa-download\"></i> Download File</button>\n" +
        "                    </a>\n" +
        "                    ");
                    out.println("</br></br><a href =\"Viewer?file_name=" + file_name + "\"><button class=\"button-link\">View file</button></a>&nbsp;&nbsp;&nbsp;");
                    out.println("<a href =\"menu.jsp\"><button class=\"button-link\">Return to menu</button></a>"
                            + "</div>\n" +
        "                <div class=\"column\">\n" +
        "                    <div class=\"center\">\n" +
        "                        <img class=\"imageFinal\" id=\"final_image\" src=\"" + Const.AppConst.SAVE_DIR + File.separator + file_name + ".jpeg\">\n" +
        "                        <img class=\"image\" id=\"image\" src='#'>\n" +
        "                    </div>\n" +
        "                </div>     \n" +
        "            </div>\n" +
        "        </div>");
                    out.println("<script type=\"text/javascript\" src=\"" + Const.AppConst.JS_FINISH_RESULT_PATH + "\"></script>");
                    out.println("</body>");
                    out.println("</html>");  
                }
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
                rd = request.getRequestDispatcher("/error_creating.jsp");
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FinishModifierResult.class.getName()).log(Level.SEVERE, null, ex);
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
                response.setContentType("text/html;charset=UTF-8");
        
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FinishModifierResult.class.getName()).log(Level.SEVERE, null, ex);
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
