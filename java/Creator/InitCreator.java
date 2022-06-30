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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@WebServlet(name = "InitCreator", urlPatterns = {"/InitCreator"})
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB 
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50,   // 50MB
                 location="/")
public class InitCreator extends HttpServlet {

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
        OutputStream outStream; 
        InputStream filecontent;
        Part filePart;
        File fileSaveDir;
        String appPath, saveDir, savePath;
        String file_name, file_title, file_description, storage_date;
        DateFormat dateFormat;
        Date date;       
        int read;
        byte[] bytes;

        try{
            file_name = request.getParameter("file_name");
            file_title = request.getParameter("file_title");
            file_description = request.getParameter("file_description");
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            date = new Date();            
            storage_date = dateFormat.format(date);

            image = new JLINKImage();
            image.setTitle(request.getParameter("title"));
            image.setNote(request.getParameter("description"));
            image.setIsMain(true);
            appPath = request.getServletContext().getRealPath("");
            saveDir = appPath + File.separator + Const.AppConst.CREATOR_DIR;
            fileSaveDir = new File(saveDir);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }
            image.setAppPath(appPath);
            filePart = request.getPart("image");       
            savePath = saveDir + File.separator + image.getTitle().replace(" ", "_") + ".jpeg";
            outStream = new FileOutputStream(new File(savePath));
            filecontent = filePart.getInputStream();
            bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
                outStream.write(bytes, 0, read);
            }

            getServletContext().setAttribute("image", image);
            getServletContext().setAttribute("file_name", file_name);
            getServletContext().setAttribute("file_title", file_title);
            getServletContext().setAttribute("file_description", file_description);
            getServletContext().setAttribute("storage_date", storage_date);

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
                out.println("<tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" data-value=\"" + Const.AppConst.CREATOR_DIR + File.separator + image.getTitle().replace(" ", "_") + ".jpeg\">");
                out.println("<td>" + image.getTitle() +"</td>");
                out.println("<td>" + image.getNote() +"</td>");
                out.println("<td>-</td></tr>");
                out.println("</table></br>");
                out.println("                    <p>Add an image:</p>\n" +
    "                    <table>\n" +
    "                        <form method=\"post\" action=\"SelectSprite\", enctype = \"multipart/form-data\">\n" +
    "                            <tr><td>Upload image: </td><td><input type=\"file\" name=\"image\" id=\"imgInp\" required/></td></tr>\n" +
    "                            <tr><td>Title: </td><td><input type=\"text\" name=\"title\" required/></td></tr>\n" +
    "                            <tr><td>Description: </td><td><textarea name=\"description\"/></textarea></td></tr>\n" +
    "                            <tr><td>Previous image: </td><td><select name=\"previous_image\"  required>\n" +
    "                            <option selected>" + image.getTitle().replace(" ", "_") + "</option>\n" +
    "                            </select></td></tr>\n" +
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
    


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }

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
