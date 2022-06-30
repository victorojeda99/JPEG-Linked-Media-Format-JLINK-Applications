/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Modifier;

import JLINKImage.JLINKImage;
import java.io.File;
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
@WebServlet(name = "SceneModifier", urlPatterns = {"/SceneModifier"})
public class SceneModifier extends HttpServlet {

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
                
        JLINKImage image, img;
        String scene;
        AppUtilsModifier utils;
        RequestDispatcher rd;
        
        try{
            scene = request.getParameter("scene");
            image = (JLINKImage) getServletContext().getAttribute("image");
            
            utils = new AppUtilsModifier();
            utils.getSceneByLabel(image, scene);
            img = utils.getScene();
            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>\n" +
"        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
"        <title>JSP Page</title>\n" +
"        <link rel=\"stylesheet\" type=\"text/css\" href=\"" + Const.AppConst.CSS_SYLE_PATH + "\" />\n" +
"    </head>\n" +
"    <body>\n" +
"        <div class=\"header\">\n" +
"            <div class=\"header_left\">\n" +
"                <h1>JLINK APPLICATION | Modifier</h1>\n" +
"            </div>\n" +
"            <div class=\"header_right\">\n" +
"                <img class=\"header_image\" src=\"appImages/Logo_ETSETB.png\" />\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"main_container\">          \n" +
"            <div class=\"title\">\n" +
"                <h1>Scene modifier</h1>\n" +
"            </div>\n" +
"            <div class=\"container\"> \n" +
"                <div class=\"column\">\n" +
"                    <p>The actual version of this scene is: <q>" + img.getVersion() + "</q></p>    \n" +
"                    <p>You can change the textual values that define the scene and the duration (in milliseconds) of the animation transition</p>  \n" +
"                    <table>\n" +
"                        <form method=\"post\" action=\"ApplySceneModificator\">\n" +
"                            <tr><td>Title: </td><td><input type=\"text\" name=\"title\" value=\"" + img.getTitle() + "\"/></td></tr>\n" +
"                            <tr><td>Description: </td><td><textarea name=\"description\"/>" + img.getNote() + "</textarea></td></tr>");
                if(!img.isIsMain()){
                    out.println("<tr><td>Duration (ms): </td><td><input type=\"number\" name=\"duration\" min=\"1\" value=\"" + img.getLink_duration() + "\"></td></tr>");
                }else{
                    out.println("<input type=\"hidden\" name=\"duration\" value=\"600\">");
                }                
                if(!img.getLinked_images().isEmpty()){
                    out.println("<tr><td>Sprite color: </td><td><select name=\"sprite_color\">");
                    switch(img.getSprite_color()){
                        case Const.AppConst.SPRITE_PATH:
                            out.println("<option selected>Green</option>");
                            out.println("<option>Blue</option>");
                            out.println("<option>Red</option>");
                            out.println("</select></td></tr>");
                        break;
                        case Const.AppConst.SPRITE_BLUE_PATH:
                            out.println("<option>Green</option>");
                            out.println("<option selected>Blue</option>");
                            out.println("<option>Red</option>");
                            out.println("</select></td></tr>");
                        break;
                        case Const.AppConst.SPRITE_RED_PATH:
                            out.println("<option selected>Green</option>");
                            out.println("<option>Blue</option>");
                            out.println("<option selected>Red</option>");
                            out.println("</select></td></tr>");
                        break;
                    }
                } else {
                    out.println("<input type=\"hidden\" name=\"sprite_color\" value=\"Green\">");
                }                
                out.println("<input type=\"hidden\" name=\"scene\" value=\"" + scene + "\">  \n" +
"                            <tr><td><input type=\"submit\" value=\"Do another action\" /></td></tr>  \n" +
"                        </form>\n" +
"                    </table>\n" +
"                </div>\n" +
"                <div class=\"column\">\n" +
"                    <img class=\"imageFinal\" src=\"" + Const.AppConst.MODIFIER_DIR + File.separator + img.getTitle().replace(" ", "_")  + ".jpeg\">\n" +
"                </div>\n" +
"            </div>\n" +
"        </div>\n" +
"    </body>");
                out.println("</html>");
            }
        }catch (Exception e){
            
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
