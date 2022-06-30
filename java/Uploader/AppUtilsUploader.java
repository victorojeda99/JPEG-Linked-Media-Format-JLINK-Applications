/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Uploader;

import JLINKImage.JLINKImage;
import JLINKLibrary.JLINKSuperBox;
import JLINKLibrary.JLINKUtilsWeb;
import JUMBF.JUMBFSuperBox;
import JUMBF.JUMBFUtilsWeb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Victor Ojeda
 */
public class AppUtilsUploader {
    
    
    private final JUMBFUtilsWeb JumbfUtils = new JUMBFUtilsWeb();


    public void setJLINKImage(JLINKImage image, String file_name) throws Exception{
        
        File f;
        JLINKSuperBox jlink;
        JLINKUtilsWeb utils;
        
        f = new File(image.getAppPath() + File.separator + Const.AppConst.UPLOADER_DIR + File.separator + file_name);
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
            JLINKImage aux = new JLINKImage();
            aux.setPrevious_image(img.getTitle());
            aux.setAppPath(img.getAppPath());
            aux = this.parseJLINKFile(links, aux);
            aux.setAppPath(img.getAppPath());
            img.addLink(aux);
        }
        return img;
    }
    

    public void saveContent(byte[] data, String title, String appPath) throws FileNotFoundException, IOException{   
        
        OutputStream out = null;
        
        out = new FileOutputStream(new File(appPath + File.separator + Const.AppConst.UPLOADER_DIR + File.separator + title + ".jpeg"));  
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
}
