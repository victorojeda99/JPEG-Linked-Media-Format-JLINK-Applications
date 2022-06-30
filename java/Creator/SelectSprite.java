/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Creator;

import JLINKImage.JLINKImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Victor Ojeda
 */
@WebServlet(name = "SelectSprite", urlPatterns = {"/SelectSprite"})
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB 
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50,   // 50MB
                 location="/")
public class SelectSprite extends HttpServlet {

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

        JLINKImage link_image;
        OutputStream outStream; 
        InputStream filecontent;
        Part filePart;
        File fileSaveDir;
        String appPath, saveDir, savePath, file_name;
        int read;
        byte[] bytes;
        
        try{
            link_image = new JLINKImage();
            link_image.setPrevious_image(request.getParameter("previous_image"));
            link_image.setTitle(request.getParameter("title")); 
            link_image.setNote(request.getParameter("description")); 
            appPath = request.getServletContext().getRealPath("");
            saveDir = appPath + File.separator + Const.AppConst.CREATOR_DIR;
            fileSaveDir = new File(saveDir);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }
            link_image.setAppPath(appPath);
            filePart = request.getPart("image");       
            savePath = saveDir + File.separator + link_image.getTitle().replace(" ", "_") + ".jpeg";

            outStream = new FileOutputStream(new File(savePath));
            filecontent = filePart.getInputStream();
            bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
                outStream.write(bytes, 0, read);
            }

            getServletContext().setAttribute("link_image", link_image);

            file_name = (String) getServletContext().getAttribute("file_name");

            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>JLINK Creator</title>"); 
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + Const.AppConst.CSS_SYLE_PATH + "\" />");
                out.println("</head>");
                out.println("<body onload=\"getImgSize()\">");
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
    "            <div class=\"container\"> \n" +
    "                <div class=\"column\">");
                out.println("<p>Image size:</p>    \n" +
    "                    <ul>\n" +
    "                    <li><p>Width: \n" +
    "                    <q id=\"img_width\"></q></p></li>\n" +
    "                    <li><p>Height: \n" +
    "                    <q id=\"img_height\"></q></p></li>\n" +
    "                    </ul>\n" +
    "                    <p>Select sprite:</p>                \n" +
    "                    <ul>\n" +
    "                    <li><p>Coord X: \n" +
    "                    <q id=\"output_x\">Click on the image</q></p></li>\n" +
    "                    <li><p>Coord Y: \n" +
    "                    <q id=\"output_y\">Click on the image</q></p></li>\n" +
    "                    </ul>");        
                out.println("<form id=\"selectSprite_button\" method=\"post\" action=\"AddLINK\">\n" +
    "                    <input type=\"hidden\" name=\"sprite_x\" id=\"sprite_x\" value=\"\" />\n" +
    "                    <input type=\"hidden\" name=\"sprite_y\" id=\"sprite_y\" value=\"\" />\n" +
    "                    <input type=\"submit\" value=\"Add LINK\"/>&nbsp; or &nbsp;<input type=\"submit\" value=\"Finish\" formaction=\"FinishCreator\"/>\n" +
    "                </form>\n" +
    "                </div>\n" +
    "                <div class=\"column\">\n" +
    "                <img class=\"previous_image\" id=\"img\" src=\"" + Const.AppConst.CREATOR_DIR + File.separator + link_image.getPrevious_image() + ".jpeg\">\n" +
    "                <img class=\"sprite\" id=\"sprite\" src=\"" + Const.AppConst.SPRITE_PATH + "\">\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </div>");
                out.println("<script type=\"text/javascript\" src=\"" + Const.AppConst.JS_SELECT_SPRITE_PATH + "\"></script>");
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
