/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JLINKLibrary;

import JPEGXTBox.SuperBox;
import JUMBF.JUMBFSuperBox;
import JUMBF.JUMBFUtilsWeb;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author Victor Ojeda
 */
public class JLINKUtilsWeb extends JLINKUtils {
    
    private final JUMBFUtilsWeb JumbfUtils = new JUMBFUtilsWeb();
    
    

    public short JLINKToBox(JLINKSuperBox superBox, String fileName, short BoxInstance, String appPath, String dir) throws Exception {    
        
        String s = new String(appPath + File.separator + dir + File.separator + fileName + ".jmb");
        File file = new File(s);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        for(JLINKSuperBox link:superBox.getNestedJlinkBoxes().values()) {
            this.JLINKToBox(link, link.getDescriptionBox().getLabel(), BoxInstance, appPath, dir);
            BoxInstance += superBox.getNestedJumbfBoxes().size();
        }
        for(JUMBFSuperBox sb:superBox.getNestedJumbfBoxes().values()) {
            this.JumbfUtils.JUMBFToBox(sb, sb.getDescriptionBox().getLabel(), BoxInstance, appPath, dir);
            BoxInstance++;
        }   
        
        SuperBox[] xtBoxes = this.JumbfUtils.getBoxes(superBox, BoxInstance);
        for (SuperBox box:xtBoxes) {
            baos.write(box.getXTBoxData());
        }
        
        Files.write(file.toPath(), baos.toByteArray());
        return BoxInstance;
    }

}
