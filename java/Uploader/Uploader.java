/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Uploader;

import JLINKImage.JLINKImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "Uploader", urlPatterns = {"/Uploader"})
@MultipartConfig(location="/")
public class Uploader extends HttpServlet {

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
        AppUtilsUploader utils;
        OutputStream outStream; 
        InputStream filecontent;
        Part filePart;
        File fileSaveDir;
        Path file_path;
        Connection connection;
        PreparedStatement statement;  
        String sql;
        String appPath, saveDir, savePath;
        String file_name, file_title, file_description, storage_date;
        DateFormat dateFormat;
        Date date;       
        int read;
        byte[] bytes;

        try{
            file_title = request.getParameter("file_title");
            file_description = request.getParameter("file_description");
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            date = new Date();            
            storage_date = dateFormat.format(date);

            image = new JLINKImage();
            image.setTitle(request.getParameter("title"));
            image.setIsMain(true);
            appPath = request.getServletContext().getRealPath("");
            saveDir = appPath + File.separator + Const.AppConst.UPLOADER_DIR;
            fileSaveDir = new File(saveDir);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }
            image.setAppPath(appPath);
            filePart = request.getPart("image");     
            file_name = this.getFileName(filePart);

            file_path = Paths.get(file_name);
            file_name = file_path.getFileName().toString();
            savePath = saveDir + File.separator + file_name;
            outStream = new FileOutputStream(new File(savePath));
            filecontent = filePart.getInputStream();
            bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
                outStream.write(bytes, 0, read);
            }
            
            utils = new AppUtilsUploader();
            utils.setJLINKImage(image, file_name);
            
            filePart = request.getPart("image");  
            filecontent = filePart.getInputStream();
            saveDir = appPath + File.separator + Const.AppConst.SAVE_DIR;
            savePath = saveDir + File.separator + file_name;
            outStream = new FileOutputStream(new File(savePath));
            bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
                outStream.write(bytes, 0, read);
            }
            
            connection = DriverManager.getConnection(Const.AppConst.DATABASE_CONNECTION);
            sql =  "INSERT INTO IMAGE (FILENAME, TITLE, DESCRIPTION, STORAGE_DATE) "
                     + "VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, file_name.replace(".jpeg", ""));
            statement.setString(2, file_title);
            statement.setString(3, file_description);
            statement.setString(4, storage_date);
            statement.executeUpdate();

            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>JLINK Uploader</title>");
                out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + Const.AppConst.CSS_SYLE_PATH + "\" />");            
                out.println("</head>");
                out.println("<body>");
                out.println("<div class=\"header\">\n" +
    "            <div class=\"header_left\">\n" +
    "                <h1>JLINK APPLICATION | Uploader</h1>\n" +
    "            </div>\n" +
    "            <div class=\"header_right\">\n" +
    "                <img class=\"header_image\" src=\"" + Const.AppConst.HEADER_LOGO_PATH + "\" />\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"main_container\">          \n" +
    "            <div class=\"title\">\n" +
    "                <h1>JLINK Uploader</h1>\n" +
    "            </div>\n" +
    "            <div class=\"container\">\n" +
    "                <div class=\"column\">\n" +
    "                    <p>The file has been uploaded successfully.</p>\n" +
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
                this.containsTable(out, image);
                out.println("</table>\n" +
    "                    <p>The file has been saved in the application database.</p>");
                out.println("<a href =\"Viewer?file_name=" + file_name.replace(".jpeg", "") + "\"><button class=\"button-link\">View file</button></a>&nbsp;&nbsp;&nbsp;");
                out.println("<a href =\"menu.jsp\"><button class=\"button-link\">Return to menu</button></a>"
                        + "</div>\n" +
    "                <div class=\"column\">\n" +
    "                    <div class=\"center\">\n" +
    "                        <img class=\"imageFinal\" id=\"final_image\" src=\"" + Const.AppConst.SAVE_DIR + File.separator + file_name + "\">\n" +
    "                        <img class=\"image\" id=\"image\" src='#'>\n" +
    "                    </div>\n" +
    "                </div>     \n" +
    "            </div>\n" +
    "        </div>");
                out.println("<script type=\"text/javascript\" src=\"" + Const.AppConst.JS_FINISH_RESULT_PATH + "\"></script>");
                out.println("</body>");
                out.println("</html>");
            }
        }catch (Exception e){
            RequestDispatcher rd;

            try ( PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/error_uploading.jsp");
                rd.forward(request, response);
                out.println("</html>");
            }
        } 
    }
    
    private String getFileName(Part part) {                 
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
    
    private void containsTable(PrintWriter out, JLINKImage image){
        
        out.println("<tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" data-value=\"" + Const.AppConst.UPLOADER_DIR + File.separator + image.getTitle().replace(" ", "_") + ".jpeg\">");
        out.println("<td>" + image.getTitle() +"</td>");
        out.println("<td>" + image.getNote() +"</td>");
        if(image.isIsMain()){
            out.println("<td>-</td></tr>");
        }else{
            if(image.getPrevious_image() != null){
                out.println("<td>" + image.getPrevious_image().replace("_", " ") +"</td></tr>");
            }
        }
        for(JLINKImage aux: image.getLinked_images()){
            this.containsTable(out, aux);        
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
            Logger.getLogger(Uploader.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Uploader.class.getName()).log(Level.SEVERE, null, ex);
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
