/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modifier;

import JLINKBuilder.BuilderWeb;
import JLINKImage.JLINKImage;
import JLINKLibrary.JLINKSuperBox;
import JLINKLibrary.JLINKUtilsWeb;
import JUMBF.JUMBFSuperBox;
import JUMBF.JUMBFUtilsWeb;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Victor Ojeda
 */
public class AppUtilsModifier {
    
    private final JUMBFUtilsWeb JumbfUtils = new JUMBFUtilsWeb();
    private Map<String, Integer> images;
    private int JLINK_box_id;
    private int cont_scene;
    private int cont_sprite;    
    private JLINKImage scene;
    
    public AppUtilsModifier(){
        images = new HashMap<>();
        JLINK_box_id = 1;
        cont_scene = 0;
        cont_sprite = 0;
    }
    
    public void setJLINKImage(JLINKImage image, String file_name) throws Exception{
        
        File f;
        JLINKSuperBox jlink;
        JLINKUtilsWeb utils;
        
        f = new File(image.getAppPath() + File.separator + Const.AppConst.SAVE_DIR + File.separator + file_name + ".jpeg");
        utils = new JLINKUtilsWeb();
        jlink = utils.shapeJLINKSuperBox(utils.getJUMBFUtils().getBoxesFromFile(f.getAbsolutePath()));
        this.parseJLINKFile(jlink, image);
    }
        
    public JLINKImage parseJLINKFile(JLINKSuperBox jlink, JLINKImage img) throws Exception{
        
        this.getMetadata(jlink.getXMLContentBox().getContentBox().getContentData(), img);
        for (JUMBFSuperBox jsb:jlink.getNestedJumbfBoxes().values()) {
            if(this.JumbfUtils.getImageLabelFromBox(jsb).contains(Const.JLINKConst.IMAGE_LABEL)){
                img.setLabel(this.JumbfUtils.getImageLabelFromBox(jsb));
                this.saveContent(this.JumbfUtils.getImageBytesFromBox(jsb), img.getTitle().replace(" ", "_"), img.getAppPath());
            } else {
                this.saveContent(this.JumbfUtils.getImageBytesFromBox(jsb), this.JumbfUtils.getImageLabelFromBox(jsb), img.getAppPath());
            }
        }
        for (JLINKSuperBox links:jlink.getNestedJlinkBoxes().values()) {
            cont_scene++;
            JLINKImage aux = new JLINKImage();
            aux.setPrevious_image(img.getTitle().replace(" ", "_"));
            aux.setAppPath(img.getAppPath());
            aux = this.parseJLINKFile(links, aux);
            aux.setAppPath(img.getAppPath());
            img.addLink(aux);
        }
        return img;
    }
    
    public void saveContent(byte[] data, String title, String appPath) throws FileNotFoundException, IOException{   
        
        OutputStream out = null;
        
        out = new FileOutputStream(new File(appPath + File.separator + Const.AppConst.MODIFIER_DIR + File.separator + title + ".jpeg"));  
        out.write(data);
    }   
    
    
    public void getMetadata(byte[] data, JLINKImage image){
       
        String metadata, value;
        
        metadata = new String(data);
        image.setVersion(StringUtils.substringsBetween(metadata, Const.MetaConst.START_VERSION, Const.MetaConst.END_VERSION)[0]);
        image.setTitle(StringUtils.substringsBetween(metadata, Const.MetaConst.START_TITLE, Const.MetaConst.END_TITLE)[0]);
        image.setNote(StringUtils.substringsBetween(metadata, Const.MetaConst.START_NOTE, Const.MetaConst.END_NOTE)[0]);
        image.setImage_format(StringUtils.substringsBetween(metadata, Const.MetaConst.START_IMAGE_FORMAT, Const.MetaConst.END_IMAGE_FORMAT)[0]);
        value = StringUtils.substringsBetween(metadata, Const.MetaConst.START_IMAGE_HREF, Const.MetaConst.END_IMAGE_HREF)[0];
        value = Const.JLINKConst.IMAGE_LABEL + value.replaceAll("[^0-9]", "");
        image.setImage_Href(value);
        value = StringUtils.substringsBetween(metadata, Const.MetaConst.START_LINK_X, Const.MetaConst.END_LINK_X)[0];
        image.setLink_region_X(value);
        image.setLink_region_Y(StringUtils.substringsBetween(metadata, value + Const.MetaConst.START_LINK_Y, Const.MetaConst.END_LINK_Y)[0]);
        image.setLink_duration(StringUtils.substringsBetween(metadata, Const.MetaConst.START_LINK_DURATION, Const.MetaConst.END_LINK_DURATION)[0]);
        image.setLink_Vpid(Integer.parseInt(StringUtils.substringsBetween(metadata, Const.MetaConst.START_LINK_VPID, Const.MetaConst.END_LINK_VPID)[0]));
        image.setImage_format(StringUtils.substringsBetween(metadata, Const.MetaConst.START_IMAGE_FORMAT, Const.MetaConst.END_IMAGE_FORMAT)[0]);
        value = StringUtils.substringsBetween(metadata, Const.MetaConst.START_LINK_SPRITE, Const.MetaConst.END_LINK_SPRITE)[0];
        if(!value.equals("")){
            value = Const.JLINKConst.SPRITE_LABEL + value.replaceAll("[^0-9]", "");
        }
        image.setLink_sprite(value);
        image.setImage_format(StringUtils.substringsBetween(metadata, Const.MetaConst.START_IMAGE_FORMAT, Const.MetaConst.END_IMAGE_FORMAT)[0]);
        value = StringUtils.substringsBetween(metadata, Const.MetaConst.START_LINK_TO, Const.MetaConst.END_LINK_TO)[0];
        if(!value.equals("")){
            value = Const.JLINKConst.SCENE_LABEL + value.replaceAll("[^0-9]", "");
        }        
        image.setLink_to(value);
    }
    
    public void deleteLINK(JLINKImage image, String deleted_image){
        
        for(JLINKImage aux: image.getLinked_images()){
            if(aux.getTitle().equals(deleted_image.replace("_", " "))){
                image.getLinked_images().remove(aux);
                return;
            }
            this.deleteLINK(aux, deleted_image);
        }
    }
    
    
    public void containsTable(PrintWriter out, JLINKImage image){
        
        out.println("<tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" data-value=\"" + Const.AppConst.MODIFIER_DIR + File.separator + image.getTitle().replace(" ", "_") + ".jpeg\""
                + " onclick=\"window.location='SceneModifier?scene=" + image.getLabel() + "'\">");
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
    
    public void formDelete(PrintWriter out, JLINKImage image){
        
        if(image.getLinked_images().isEmpty()){
            out.println("<option>"+ image.getTitle().replace(" ", "_") +"</option>");
        }
        for(JLINKImage aux: image.getLinked_images()){
            this.formDelete(out, aux);        
        }
    }
    
    public void createFile(JLINKImage image, String merge_file) throws Exception{
        
        this.setMetadataParam(image);
        cont_scene = 0;
        cont_sprite = 0;
        this.builder(image, true, null);
        this.mergeFile(image, merge_file);
    }
    
    public void setDefaultParam(JLINKImage image) throws Exception{
        
        image.setViewport_Id(Const.JLINKConst.DEFAULT_VIEWPORT_ID);
        image.setViewport_X(Const.JLINKConst.DEFAULT_VIEWPORT_X);
        image.setViewport_Y(Const.JLINKConst.DEFAULT_VIEWPORT_Y);
        image.setViewport_xFov(Const.JLINKConst.DEFAULT_VIEWPORT_XFOV);
        image.setViewport_yFov(Const.JLINKConst.DEFAULT_VIEWPORT_YFOV);
        image.setImage_format(Const.JLINKConst.IMAGE_FORMAT_JPEG);
        image.setImage_Href("\"" + Const.JLINKConst.JUMBF_URI + image.getLabel() + "\"");
        image.setLink_region_shape(Const.JLINKConst.DEFAULT_LINK_SHAPE);
        image.setLink_region_W(Const.JLINKConst.DEFAULT_LINK_W);
        image.setLink_region_H(Const.JLINKConst.DEFAULT_LINK_H);
        image.setLink_region_rotation(Const.JLINKConst.DEFAULT_LINK_ROTATION);
        image.setLink_Vpid(Const.JLINKConst.DEFAULT_LINK_VPID);
    }

    public void setMetadataParam(JLINKImage image) throws Exception{
        
        image.setLabel(Const.JLINKConst.IMAGE_LABEL + cont_scene);
        this.setDefaultParam(image);
        if(image.isIsMain()){
            image.setLink_region_X("");
            image.setLink_region_Y("");
            image.setLink_to("");   
        }else{
            image.setLink_to("\"" + Const.JLINKConst.JUMBF_URI + Const.JLINKConst.SCENE_LABEL + images.get(image.getPrevious_image()) + "\"");
        }
        images.put(image.getTitle().replace(" ", "_"), cont_scene);
        cont_scene++;
        if(!image.getLinked_images().isEmpty()){
            image.setLink_sprite("\"" + Const.JLINKConst.JUMBF_URI + Const.JLINKConst.SPRITE_LABEL + cont_sprite + "\"");
            cont_sprite++;
        }else{
            image.setLink_sprite("");
        }
        for(JLINKImage aux: image.getLinked_images()){
                  
            this.setMetadataParam(aux);        
        }
    }
    
    
    public void builder(JLINKImage image, boolean mainBox, BuilderWeb builder_file) throws Exception{
        
        JLINKUtilsWeb utils;
        int JUMBF_box_id;
        short box_ins;
        
        if(builder_file == null){
            builder_file = new BuilderWeb(mainBox, builder_file, (short)1);    
        } else {
            builder_file = new BuilderWeb(mainBox, builder_file, builder_file.getBoxInstance());    
        }
        JUMBF_box_id = 1;
        box_ins = builder_file.getBoxInstance();
        builder_file.setJLINKDescriptionBox(Const.JLINKConst.SCENE_LABEL + cont_scene, JLINK_box_id);
        cont_scene++;
        JLINK_box_id++;
        builder_file.setXMLMetadata(image, Const.AppConst.MODIFIER_DIR);
        builder_file.addJUMBFContentBox(image, builder_file.getJLINK(), JUMBF_box_id, Const.AppConst.MODIFIER_DIR);
        JUMBF_box_id++;
        box_ins++;
        if(!image.getLinked_images().isEmpty()){
            builder_file.addJUMBFContentBoxForSprite(cont_sprite, builder_file.getJLINK(), JUMBF_box_id, image.getAppPath(), image.getSprite_color());
            cont_sprite++;
            box_ins++;
        }
        utils = new JLINKUtilsWeb();
        builder_file.setBoxInstance(box_ins);
        
        for(JLINKImage aux: image.getLinked_images()){ 
            this.builder(aux, false, builder_file); 
        }
        if(!mainBox){
            builder_file.getParent().getJLINK().addJLINK(builder_file.getJLINK());
            builder_file.getParent().setBoxInstance(box_ins);
            utils.JLINKToBox(builder_file.getJLINK(), builder_file.getJLINK().getDescriptionBox().getLabel(), box_ins, image.getAppPath(), Const.AppConst.MODIFIER_DIR);
            builder_file.setBoxInstance(box_ins);
            box_ins++;
            builder_file.getParent().setBoxInstance(box_ins);
        }
        if(mainBox){
            utils.JLINKToBox(builder_file.getJLINK(), builder_file.getJLINK().getDescriptionBox().getLabel(), builder_file.getBoxInstance(), image.getAppPath(), Const.AppConst.MODIFIER_DIR);
        }
    }
    
    public void mergeFile(JLINKImage image, String merge_file) throws IOException, Exception{
        
        JUMBFUtilsWeb utils;
        String image_fileName, jumbf_fileName, merge_fileName;
        
        utils = new JUMBFUtilsWeb();
        image_fileName = image.getAppPath() + File.separator + Const.AppConst.MODIFIER_DIR + File.separator + image.getTitle().replace(" ", "_") + ".jpeg";
        jumbf_fileName = image.getAppPath() + File.separator + Const.AppConst.MODIFIER_DIR + File.separator + Const.JLINKConst.SCENE_LABEL + 0 + ".jmb";
        merge_fileName = image.getAppPath() + File.separator + Const.AppConst.SAVE_DIR + File.separator + merge_file;
        utils.mergeJUMBF(image_fileName, jumbf_fileName, merge_fileName);
    }
    
    public String updateFileName(String file_name, String appPath){
        
        StringBuilder name;
        char v;
        File f;
        Boolean file_not_exist;
        name = new StringBuilder(file_name);
        v = file_name.charAt(file_name.length()-1);
        file_not_exist = true;

        if(file_name.contains("_v") && Character.isDigit(v)) {
            do{
                v++;
                System.out.println(v);
                name.setCharAt(file_name.length()-1, v);
                f = new File(appPath + File.separator + Const.AppConst.SAVE_DIR + File.separator + name + ".jpeg");
                if (!f.exists()) {
                    file_not_exist = false;
                }
            }while(file_not_exist);
        } else {
            name.append("_v1");
            f = new File(appPath + File.separator + Const.AppConst.SAVE_DIR + File.separator + name + ".jpeg");
            if (f.exists()) {
                v = name.charAt(name.length()-1);
                do{
                    v++;
                    System.out.println(v);
                    name.setCharAt(name.length()-1, v);
                    f = new File(appPath + File.separator + Const.AppConst.SAVE_DIR + File.separator + name + ".jpeg");
                    if (!f.exists()) {
                        file_not_exist = false;
                    }
                }while(file_not_exist);
            }
        }
        return name.toString();
    }
    
    public void getSceneByLabel(JLINKImage image, String label){
        
        for(JLINKImage aux: image.getLinked_images()){
            this.getSceneByLabel(aux, label);

        }
        if(image.getLabel().equals(label)){  
           scene = image;
        }
    }
    
    public JLINKImage getScene(){
        return scene;
    }
    
    public int getContScene(){
        cont_scene++;
        return cont_scene;
    }

    public void modifySceneInformation(JLINKImage image, String label, String title, String description, String duration, String sprite_color) throws FileNotFoundException, IOException {
        
        String old_title, old_version;
        char v;
        
        for(JLINKImage aux: image.getLinked_images()){
            this.modifySceneInformation(aux, label, title, description, duration, sprite_color);
        }
        if(image.getLabel().equals(label)){  
           old_title = image.getTitle();
           image.setTitle(title);
           image.setNote(description);
           image.setLink_duration(duration);
           image.setSprite_color(sprite_color);
           old_version =  image.getVersion();
           v = old_version.charAt(0);
           v++;
           image.setVersion(v + ".0.0");
           if(!old_title.equals(title)){
                InputStream is = null;
                OutputStream os = null;
                try {
                    is = new FileInputStream(image.getAppPath() + File.separator + Const.AppConst.MODIFIER_DIR + File.separator + old_title.replace(" ", "_") + ".jpeg");
                    os = new FileOutputStream(image.getAppPath() + File.separator + Const.AppConst.MODIFIER_DIR + File.separator + title.replace(" ", "_") + ".jpeg");
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                } finally {
                    is.close();
                    os.close();
                }
           }
        }
    }
    
}
