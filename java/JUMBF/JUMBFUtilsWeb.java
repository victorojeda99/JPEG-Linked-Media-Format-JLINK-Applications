/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JUMBF;

import JPEGXTBox.SuperBox;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Victor Ojeda
 */
public class JUMBFUtilsWeb extends JUMBFUtils{
    
    
    
    
    public byte[] getImageBytesFromBox(Object input) throws Exception {
        byte[] data;
        if (input.getClass().equals(String.class)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(this.getBoxesFromFile((String) input));
            data = baos.toByteArray();
        } else if (input.getClass().equals(JUMBFSuperBox.class)) {
            JUMBFSuperBox box = (JUMBFSuperBox) input;            
            data = box.getContentBox().getContentData();
        } else {
            return null;
        }
        return data;
    }
    
    public String getImageLabelFromBox(Object input) throws Exception{
        String label;        
        if (input.getClass().equals(String.class)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(this.getBoxesFromFile((String) input));
            byte[] data = baos.toByteArray();
            JUMBFSuperBox box = shapeJUMBFSuperBox(data);
            label = box.getDescriptionBox().getLabel();
        } else if (input.getClass().equals(JUMBFSuperBox.class)) {
            JUMBFSuperBox box = (JUMBFSuperBox) input;            
            label = box.getDescriptionBox().getLabel();
        } else {
            return null;
        }
        return label;
    }
    
    public String getXMLMetadataFromBox(Object input) throws Exception{
        String metadata;        
        if (input.getClass().equals(String.class)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(this.getBoxesFromFile((String) input));
            byte[] data = baos.toByteArray();
            JUMBFSuperBox box = shapeJUMBFSuperBox(data);
            metadata = box.getDescriptionBox().getLabel();
            
        } else if (input.getClass().equals(JUMBFSuperBox.class)) {
            JUMBFSuperBox box = (JUMBFSuperBox) input;            
            metadata = box.getDescriptionBox().getLabel();
        } else {
            return null;
        }
        return metadata;
    }
    
    public void mergeJUMBF(String imageName, String fileName, String mergeName) throws Exception {
       
        byte[] image = Files.readAllBytes(Paths.get(imageName));
        byte[] jumbf = Files.readAllBytes(Paths.get(fileName));
        
        File file = new File(mergeName + ".jpeg");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int counter = 0;
        
        for(byte b:image){
            if (String.format("%x", Byte.toUnsignedInt(image[counter])).equals("ff") && String.format("%x", Byte.toUnsignedInt(image[counter + 1])).equals("d8")) {
                break;
            }
            counter++;
        }
        counter += 2;
        
        byte[] placeholder = new byte[counter];
        
        for(int i = 0; i < counter; i++) {
            placeholder[i] = image[i];
        }
        
        baos.write(placeholder);
        
        placeholder = new byte[jumbf.length];
        
        for(int i = 0; i < jumbf.length; i++) {
            placeholder[i] = jumbf[i];
        }
        
        baos.write(placeholder);
        baos.write(image, counter, image.length - counter);
                
        Files.write(file.toPath(), baos.toByteArray());
    }
    
    public void JUMBFToBox(JUMBFSuperBox superBox, String fileName, short boxInstance, String appPath, String dir) throws Exception {        
        
        String s = new String(appPath + File.separator + dir + File.separator + fileName + ".jumbf");
        File file = new File(s);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        SuperBox[] xtBoxes = this.getBoxes(superBox, boxInstance);
        
        for (SuperBox box:xtBoxes) {
            baos.write(box.getXTBoxData());
        }
        Files.write(file.toPath(), baos.toByteArray());
    }
}
