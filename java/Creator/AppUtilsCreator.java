/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creator;

import JLINKBuilder.BuilderWeb;
import JLINKImage.JLINKImage;
import JLINKLibrary.JLINKUtilsWeb;
import JUMBF.JUMBFUtilsWeb;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Victor Ojeda
 */
public class AppUtilsCreator {
    
    private Map<String, Integer> images;
    private int JLINK_box_id;
    private int cont_scene;
    private int cont_sprite;
    
    public AppUtilsCreator(){
        images = new HashMap<>();
        JLINK_box_id = 1;
        cont_scene = 0;
        cont_sprite = 0;
    }
    
    
    public void insertLINK(JLINKImage source, JLINKImage destination){
        
        if((source.getTitle().equals(destination.getPrevious_image().replace("_", " ")) && (!source.getLinked_images().contains(destination)))){
            source.addLink(destination);
            return;
        }
        for(JLINKImage aux: source.getLinked_images()){
            this.insertLINK(aux, destination);      
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
        image.setLink_duration(Const.JLINKConst.DEFAULT_LINK_DURATION);
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
        if(!image.getLinked_images().isEmpty()){
            image.setLink_sprite("\"" + Const.JLINKConst.JUMBF_URI + Const.JLINKConst.SPRITE_LABEL + cont_sprite + "\"");
            cont_sprite++;
        }else{
            image.setLink_sprite("");
        }
        for(JLINKImage aux: image.getLinked_images()){
            cont_scene++;      
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
        builder_file.setXMLMetadata(image, Const.AppConst.CREATOR_DIR);
        builder_file.addJUMBFContentBox(image, builder_file.getJLINK(), JUMBF_box_id, Const.AppConst.CREATOR_DIR);
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
            utils.JLINKToBox(builder_file.getJLINK(), builder_file.getJLINK().getDescriptionBox().getLabel(), box_ins, image.getAppPath(), Const.AppConst.CREATOR_DIR);
            builder_file.setBoxInstance(box_ins);
            box_ins++;
            builder_file.getParent().setBoxInstance(box_ins);
        }
        if(mainBox){
            utils.JLINKToBox(builder_file.getJLINK(), builder_file.getJLINK().getDescriptionBox().getLabel(), builder_file.getBoxInstance(), image.getAppPath(), Const.AppConst.CREATOR_DIR);
        }
    }
    
    public void mergeFile(JLINKImage image, String merge_file) throws IOException, Exception{
        JUMBFUtilsWeb utils;
        String image_fileName, jumbf_fileName, merge_fileName;
        
        utils = new JUMBFUtilsWeb();
        image_fileName = image.getAppPath() + File.separator + Const.AppConst.CREATOR_DIR + File.separator + image.getTitle().replace(" ", "_") + ".jpeg";
        jumbf_fileName = image.getAppPath() + File.separator + Const.AppConst.CREATOR_DIR + File.separator + Const.JLINKConst.SCENE_LABEL + 0 + ".jmb";
        merge_fileName = image.getAppPath() + File.separator + Const.AppConst.SAVE_DIR + File.separator + merge_file;
        utils.mergeJUMBF(image_fileName, jumbf_fileName, merge_fileName);
    }  
}
