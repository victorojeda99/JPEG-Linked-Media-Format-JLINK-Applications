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
@WebServlet(name = "ApplySceneModificator", urlPatterns = {"/ApplySceneModificator"})
public class ApplySceneModificator extends HttpServlet {

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
        
        JLINKImage image;
        String scene, title, description, duration, sprite_color;
        AppUtilsModifier utils;
        RequestDispatcher rd;
        
        try{
            image = (JLINKImage) getServletContext().getAttribute("image");
            scene = request.getParameter("scene");
            title = request.getParameter("title");
            description = request.getParameter("description");
            duration = request.getParameter("duration");
            sprite_color = request.getParameter("sprite_color");
            
            switch(sprite_color){
                case "Green":
                    sprite_color = Const.AppConst.SPRITE_PATH;
                break;
                case "Blue":
                    sprite_color = Const.AppConst.SPRITE_BLUE_PATH;
                break;
                case "Red":
                    sprite_color = Const.AppConst.SPRITE_RED_PATH;
                break;
            }
            
            utils = new AppUtilsModifier(); 
            utils.modifySceneInformation(image, scene, title, description, duration, sprite_color);
            
            getServletContext().setAttribute("image", image);

            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/ActionModifier");
                rd.forward(request, response);
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
