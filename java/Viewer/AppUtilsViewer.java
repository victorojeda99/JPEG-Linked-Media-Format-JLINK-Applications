/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Viewer;

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
public class AppUtilsViewer {
    
    private final JUMBFUtilsWeb JumbfUtils = new JUMBFUtilsWeb();
        
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
            }
            this.saveContent(this.JumbfUtils.getImageBytesFromBox(jsb), this.JumbfUtils.getImageLabelFromBox(jsb), Common.Values.String_TYPE_ContiguousCodestream, img.getAppPath());
        }
        for (JLINKSuperBox links:jlink.getNestedJlinkBoxes().values()) {
            JLINKImage aux = new JLINKImage();
            aux.setAppPath(img.getAppPath());
            aux = this.parseJLINKFile(links, aux);
            aux.setAppPath(img.getAppPath());
            aux.setPrevious_image(img.getImage_Href());
            img.addLink(aux);
        }
        return img;
    }
    
    public void saveContent(byte[] data, String label, String contentType, String appPath) throws FileNotFoundException, IOException{   
        
        OutputStream out = null;
        
        out = new FileOutputStream(new File(appPath + File.separator + Const.AppConst.VIEW_DIR + File.separator + label.toLowerCase() + ".jpeg"));
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
    
    public void viewerScript(JLINKImage image) throws FileNotFoundException, IOException{
        
        StringBuilder sb;
        OutputStream out;
        File f;
        
        sb = new StringBuilder();
        this.initViewer(image, sb);
        this.resizeViewer(image, sb);
        this.jumpIn(image, sb);
        this.jumpOut(image, sb);
        this.spriteMouseOverAndOut(image, sb);
        this.backButtonMouseOverAndOut(image, sb);
        this.imageTextInformation(sb);
        f = new File(image.getAppPath() + File.separator + Const.AppConst.JS_VIEWER_PATH);
        out = new FileOutputStream(f);
        out.write(sb.toString().getBytes());
    }
    
    public void initViewer(JLINKImage image,  StringBuilder sb){
        
        sb.append("function initViewer() {\n");
        sb.append("var view = document.getElementById('view');\n");
        this.initImg(image, sb);
        sb.append("if (img0.height*100/document.documentElement.clientHeight > 75) {\n");
        sb.append("img0.style.height = 75 + 'vh';\n");
        sb.append("img0.style.width = 'auto';\n");
        sb.append("}\n");
        sb.append("if (img0.width*100/document.documentElement.clientWidth > 45) {\n");
        sb.append("img0.style.width = 45 + 'vw';\n");
        sb.append("img0.style.height = 'auto';\n");
        sb.append("}\n");
        sb.append("view.style.height = img0.height + 'px';\n");
        sb.append("view.style.width = img0.width + 'px';\n");
        sb.append("img0.style.top = 0 + 'px';\n");
        sb.append("img0.style.left = 0 + 'px';\n");
        sb.append("document.getElementById('scene-title').innerHTML = '" + image.getTitle().replace("'", "\\'") +"';\n");
        sb.append("document.getElementById('scene-description').innerHTML = '" + image.getNote().replace("'", "\\'") +"';\n");
        this.initSprite(image, sb);
        sb.append("view.style.visibility = 'visible'\n");
        sb.append("document.getElementById('description-region').style.maxHeight = img0.height - 150 + 'px';\n");
        sb.append("document.getElementById('back-button').style.visibility = 'visible'\n");
        sb.append("}\n");
    }
    
    public void initImg(JLINKImage image, StringBuilder sb){
        
        String index;
        
        sb.append("var img");
        index = image.getImage_Href();
        index = index.replaceAll("[^0-9]", "");
        sb.append(index + " = document.getElementById('" + Const.JLINKConst.IMAGE_LABEL + index +"');\n");
        
        for(JLINKImage aux: image.getLinked_images()){
            this.initImg(aux, sb);   
        }
    }
    
    public void initSprite(JLINKImage image, StringBuilder sb){
        
        String index, sprite;
        
        if(!image.getLinked_images().isEmpty()){
            for(JLINKImage aux: image.getLinked_images()){
                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite =  Const.JLINKConst.SPRITE_LABEL + index;
                if(image.getImage_Href().equals(Const.JLINKConst.IMAGE_LABEL + 0)){
                    sb.append("document.getElementById('" + sprite + "').style.display = 'block';\n");
                } else {
                    sb.append("document.getElementById('" + sprite + "').style.display = 'none';\n");
                }
                sb.append("document.getElementById('" + sprite + "').style.left = " + aux.getLink_region_X() + "/100*img0.width + 'px';\n");
                sb.append("document.getElementById('" + sprite + "').style.top = " + aux.getLink_region_Y() + "/100*img0.height + 'px';\n");
                sb.append("img" + index + ".style.top =  " + aux.getLink_region_Y() + "/100*img0.height + 'px';\n");
                sb.append("img" + index + ".style.left =  " + aux.getLink_region_X() + "/100*img0.width + 'px';\n");
                sb.append("img" + index + ".style.height = 0 + 'px';\n");
                sb.append("img" + index + ".style.width = 0 + 'px';\n");
                this.initSprite(aux, sb);
            }
        } 
    }
    
    public void jumpIn(JLINKImage image, StringBuilder sb){
        
        String index, sprite;
        
        if(!image.getLinked_images().isEmpty()){
            for(JLINKImage aux: image.getLinked_images()){
                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite =  Const.JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').addEventListener('click', function () {\n");
                sb.append("var source = document.getElementById('" + image.getImage_Href() + "');\n");
                sb.append("var destination = document.getElementById('" + aux.getImage_Href() + "');\n");
                sb.append("var view = document.getElementById('view');\n");
                sb.append("const linkageRegion = {xPercent: " + aux.getLink_region_X() + ", yPercent: " + aux.getLink_region_Y() + "};\n");
                sb.append("const duration = " + aux.getLink_duration() + ";\n");
                sb.append("document.getElementById('scene-title').innerHTML = '" + aux.getTitle().replace("'", "\\'") + "';\n");
                sb.append("document.getElementById('scene-description').innerHTML = '" + aux.getNote().replace("'", "\\'") + "';\n");
                sb.append("view.setAttribute('data-scene', '" + aux.getImage_Href() + "');\n");
                this.hideSprites(image, sb);               
                sb.append("document.getElementById('back-button').style.opacity = '1';\n");
                sb.append("const ImageSize = {width: source.width, height: source.height};\n");
                sb.append("const xRegion = linkageRegion.xPercent/100*ImageSize.width;\n");
                sb.append("const yRegion = linkageRegion.yPercent/100*ImageSize.height;\n");
                sb.append("const scale = 3;\n");
                sb.append("source.style.height = ImageSize.height + 'px';\n");
                sb.append("source.style.width = ImageSize.width + 'px';\n");
                sb.append("const zoomInSource = source.animate([{\n");
                sb.append("left: (ImageSize.width * 0.5 - xRegion * scale) + 'px',\n");
                sb.append("top: (ImageSize.height * 0.5 - yRegion * scale) + 'px',\n");
                sb.append("width: ImageSize.width * scale + 'px',\n");
                sb.append("height: ImageSize.height * scale + 'px',\n");
                sb.append("opacity: 0 + '%',\n");
                sb.append("easing: 'ease-in-out'\n");
                sb.append("}], duration);\n");
                sb.append("const zoomInDestination = destination.animate([{\n");
                sb.append("left: 0 + 'px',\n");
                sb.append("top: 0 + 'px',\n");
                sb.append("width: ImageSize.width + 'px',\n");
                sb.append("height: ImageSize.height + 'px',\n");
                sb.append("easing: 'ease-in-out'\n");
                sb.append("}], duration);\n");
                sb.append("zoomInDestination.onfinish = () => {\n");
                sb.append("source.style.height = ImageSize.width + 'px';\n");
                sb.append("source.style.width = ImageSize.height + 'px';\n");
                sb.append("source.style.left = 0 + 'px';\n");
                sb.append("source.style.top = 0 + 'px';\n");
                sb.append("source.style.opacity = 0 + '%';\n");
                sb.append("destination.style.opacity = 100 + '%';\n");
                sb.append("destination.style.left = 0 + 'px';\n");
                sb.append("destination.style.top = 0 + 'px';\n");
                sb.append("destination.style.width = ImageSize.width + 'px';\n");
                sb.append("destination.style.height = ImageSize.height + 'px';\n");
                for(JLINKImage auxiliar: aux.getLinked_images()){
                    index = auxiliar.getImage_Href();
                    index = index.replaceAll("[^0-9]", "");
                    sprite =  Const.JLINKConst.SPRITE_LABEL + index;
                    sb.append("document.getElementById('" + sprite + "').style.display = 'block';\n");  
                }
                sb.append("};\n");
                sb.append("});\n");
                this.jumpIn(aux, sb);
            }
        }
    }
    
    public void hideSprites(JLINKImage image, StringBuilder sb) {
        
        String index, sprite;
        
        if(!image.getLinked_images().isEmpty()){
            for(JLINKImage aux: image.getLinked_images()){
                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite =  Const.JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').style.display = 'none';\n");
                this.hideSprites(aux, sb);
            }
        } 
    }
    
    public void jumpOut(JLINKImage image, StringBuilder sb){
        
        sb.append("document.getElementById('back-button').addEventListener('click', function () {\n");
        sb.append("var view = document.getElementById('view');\n");
        sb.append("if (!(view.getAttribute('data-scene').toString() === '" + Const.JLINKConst.IMAGE_LABEL + "0')) {\n");
        sb.append("var source = document.getElementById(view.getAttribute('data-scene').toString());\n");
        sb.append("var destination = document.getElementById(source.getAttribute('link-to').toString());\n");
        sb.append("var duration;\n");
        this.jumpOutDuration(image, sb);
        sb.append("const ImageSize = {width: source.width, height: source.height};\n");
        sb.append("var linkageRegion;\n");
        this.jumpOutLinkageregion(image, sb);
        this.hideSprites(image, sb);
        sb.append("this.style.pointerEvents = 'none';\n");
        sb.append("const xRegion = linkageRegion.xPercent/100*ImageSize.width;\n");
        sb.append("const yRegion = linkageRegion.yPercent/100*ImageSize.height;\n");
        sb.append("const scale = 3;\n");
        sb.append("const zoomOutSource = source.animate([{\n");
        sb.append("left: xRegion + 'px',\n");
        sb.append("top: yRegion + 'px',\n");
        sb.append("width: 0 + 'px',\n");
        sb.append("height: 0 + 'px',\n");
        sb.append("easing: 'ease-in-out'\n");
        sb.append("}], duration);\n");
        sb.append("const zoomOutDestination = destination.animate([{\n");
        sb.append("left: (ImageSize.width * 0.5 - xRegion * scale) + 'px',\n");
        sb.append("top: (ImageSize.height * 0.5 - yRegion * scale) + 'px',\n");
        sb.append("width: ImageSize.width * scale + 'px',\n");
        sb.append("height: ImageSize.height * scale + 'px'\n");
        sb.append("},{\n");
        sb.append("left: 0 + 'px',\n");
        sb.append("top: 0 + 'px',\n");
        sb.append("width: ImageSize.width + 'px',\n");
        sb.append("height: ImageSize.height + 'px',\n");
        sb.append("opacity: 100 + '%',");
        sb.append("easing: 'ease-in-out'\n");
        sb.append("}], duration);\n");
        sb.append("zoomOutDestination.onfinish = ()=>{\n");
        sb.append("view.setAttribute('data-scene', source.getAttribute('link-to').toString());\n");
        sb.append("destination.style.opacity = 100 + '%';\n");
        sb.append("destination.style.left = 0 + 'px';\n");
        sb.append("destination.style.top = 0 + 'px';\n");
        sb.append("destination.style.width = ImageSize.width + 'px';\n");
        sb.append("destination.style.height = ImageSize.height + 'px';\n");
        sb.append("destination.style.opacity = 100 + '%';\n");
        this.jumpOutOnFinish(image, sb);
        sb.append("source.style.top =  yRegion + 'px';\n");
        sb.append("source.style.left =  xRegion + 'px';\n");
        sb.append("source.style.height = 0 + 'px';\n");
        sb.append("source.style.width = 0 + 'px';\n");
        sb.append("source.style.display = 'block';\n");
        sb.append("this.style.cursor = 'auto';\n");
        sb.append("this.style.pointerEvents = 'auto';\n");
        sb.append("};\n");
        sb.append("}\n");
        sb.append("});\n");
    }
    
    public void jumpOutDuration(JLINKImage image, StringBuilder sb){
        
        for(JLINKImage aux: image.getLinked_images()){
            sb.append("if (view.getAttribute('data-scene').toString() === '" + aux.getImage_Href() + "') {\n");
            sb.append("duration = " + aux.getLink_duration() + ";\n");
            sb.append("}\n");
            this.jumpOutDuration(aux, sb);
        }
    }
    
    public void jumpOutLinkageregion(JLINKImage image, StringBuilder sb){
                
        for(JLINKImage aux: image.getLinked_images()){
            sb.append("if(view.getAttribute('data-scene').toString() === '" + aux.getImage_Href() + "'){\n");
            sb.append("linkageRegion = {xPercent: " + aux.getLink_region_X() + ", yPercent: " + aux.getLink_region_Y() + "};\n");
            sb.append("document.getElementById('scene-title').innerHTML = '" + image.getTitle().replace("'", "\\'") + "';\n");
            sb.append("document.getElementById('scene-description').innerHTML = '" + image.getNote().replace("'", "\\'") + "';\n");
            sb.append("}\n");
            this.jumpOutLinkageregion(aux, sb);
        }
    }
    
    public void jumpOutOnFinish(JLINKImage image, StringBuilder sb){
        
        String index, sprite;
        
        if(!image.getLinked_images().isEmpty()){
            sb.append("if (source.getAttribute('link-to').toString() === '" + image.getImage_Href() + "') {\n");
            if(image.getImage_Href().equals(Const.JLINKConst.IMAGE_LABEL + 0)){
                sb.append("this.style.opacity = '0.5';\n");
            }
            for(JLINKImage aux: image.getLinked_images()){
                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite =  Const.JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').style.display = 'block';\n");
            }
            sb.append("}\n");          
        }
        for(JLINKImage aux: image.getLinked_images()){
            this.jumpOutOnFinish(aux, sb);
        }
    }
    
    public void spriteMouseOverAndOut(JLINKImage image, StringBuilder sb){
        
        String index, sprite;
        
        for(JLINKImage aux: image.getLinked_images()){
            if(!image.getLinked_images().isEmpty()){
                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite =  Const.JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').addEventListener('mouseover', function () {\n"); 
                sb.append("this.style.width = this.width + this.width * 5 / 100 + 'px';\n");   
                sb.append("});\n");   
                sb.append("document.getElementById('" + sprite + "').addEventListener('mouseout', function () {\n");   
                sb.append("this.style.width = this.width - this.width * 5 / 100 + 'px';\n");   
                sb.append("});\n");        
                this.spriteMouseOverAndOut(aux, sb);
            }
        }   
    }
    
    public void backButtonMouseOverAndOut(JLINKImage image, StringBuilder sb){
        
        sb.append("document.getElementById('back-button').addEventListener('mouseover', function () {\n" +
"if (!(document.getElementById('view').getAttribute('data-scene').toString() === 'image0')) {\n" +
"this.style.height = 5 + 'vh';\n" +
"this.style.cursor = 'pointer';\n" +
"}\n" +
"});\n" +
"document.getElementById('back-button').addEventListener('mouseout', function () {\n" +
"if (!(document.getElementById('view').getAttribute('data-scene').toString() === 'image0')) {\n" +
"this.style.height = 4 + 'vh';\n" +
"this.style.cursor = 'auto';\n" +
"}\n" +
"});\n");   
    }
    
    public void resizeViewer(JLINKImage image, StringBuilder sb){
        
        sb.append("window.onresize = function(){\n");
        sb.append("var view = document.getElementById('view');\n");
        this.initImg(image, sb);
        sb.append("var source = document.getElementById(view.getAttribute('data-scene').toString());\n");
        sb.append("source.style.height = img0.naturalHeight + 'px';\n");
        sb.append("source.style.width = img0.naturalWidth + 'px';\n");
        sb.append("if (source.height*100/document.documentElement.clientHeight > 75) {\n");
        sb.append("source.style.height = 75 + 'vh';\n");
        sb.append("source.style.width = 'auto';\n");
        sb.append("}\n");
        sb.append("if (source.width*100/document.documentElement.clientWidth > 45) {\n");
        sb.append("source.style.width = 45 + 'vw';\n");
        sb.append("source.style.height = 'auto';\n");
        sb.append("}\n");
        sb.append("view.style.height = source.height + 'px';\n");
        sb.append("view.style.width = source.width + 'px';\n");
        this.resizeSprite(image, sb);
        this.resizeImg(image, image, sb);
        sb.append("document.getElementById('description-region').style.maxHeight = source.height - 150 + 'px';\n");
        sb.append("};\n");
    }

    
    public void resizeSprite(JLINKImage image, StringBuilder sb){
        
        String index, sprite;
        
        if(!image.getLinked_images().isEmpty()){
            for(JLINKImage aux: image.getLinked_images()){
                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite =  Const.JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').style.left = " + aux.getLink_region_X() + "/100*source.width + 'px';\n");
                sb.append("document.getElementById('" + sprite + "').style.top = " + aux.getLink_region_Y() + "/100*source.height + 'px';\n");
                this.resizeSprite(aux, sb);
            }
        } 
    }    
    
    public void resizeImg(JLINKImage image, JLINKImage all, StringBuilder sb){
            
        String index;
        
        
        index = image.getImage_Href();
        index = index.replaceAll("[^0-9]", "");
        sb.append("if(view.getAttribute('data-scene').toString() === '" + Const.JLINKConst.IMAGE_LABEL + index + "'){\n");
        sb.append("img" + index + ".style.height = source.height + 'px';\n");
        sb.append("img" + index + ".style.width = source.width + 'px';\n");
        this.resizeLinkedImg(image, all, sb);
        sb.append("}\n");
        
        for(JLINKImage aux: image.getLinked_images()){
            this.resizeImg(aux, all, sb);
        }
    }
    
    public void resizeLinkedImg(JLINKImage image, JLINKImage all, StringBuilder sb){
            
        String index;
        
        for(JLINKImage aux: all.getLinked_images()){
            if(!aux.getImage_Href().equals(image.getImage_Href())){
                if(!aux.getImage_Href().equals( Const.JLINKConst.IMAGE_LABEL+0)){
                    index = aux.getImage_Href();
                    index = index.replaceAll("[^0-9]", "");
                    sb.append("img" + index + ".style.top =  " + aux.getLink_region_Y() + "/100*source.height + 'px';\n");
                    sb.append("img" + index + ".style.left =  " + aux.getLink_region_X() + "/100*source.width + 'px';\n");
                }
            }
            this.resizeLinkedImg(image, aux, sb);
        }
    }
    
    public void imageTextInformation(StringBuilder sb){
        
        sb.append("document.getElementById('text-button').addEventListener('click', function () {\n" +
"                this.style.opacity = '0.5'; \n" +
"                document.getElementById('mask').style.opacity = '1';\n" +
"                document.getElementById('mask').style.visibility = 'visible';\n" +
"                document.getElementById('back-button').style.opacity = '0.5';  \n" +
"                document.getElementById('back-button').style.pointerEvents = 'none';\n" +
"            });\n" +
"            \n" +
"            document.getElementById('close-icon').addEventListener('click', function () {\n" +
"                document.getElementById('text-button').style.opacity = '1';\n" +
"                document.getElementById('mask').style.opacity = '0';\n" +
"                document.getElementById('mask').style.visibility = 'hidden';\n" +
"                document.getElementById('back-button').style.pointerEvents = 'auto';\n" +
"                if(!(view.getAttribute('data-scene').toString() === '" + Const.JLINKConst.IMAGE_LABEL + "0')){\n" +
"                    document.getElementById('back-button').style.opacity = '1';  \n" +
"                }\n" +
"            });\n");
    }
}