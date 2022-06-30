/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JLINKBuilder;

import JLINKImage.JLINKImage;
import JLINKLibrary.JLINKSuperBox;
import JUMBF.JUMBFContentBox;
import JUMBF.JUMBFDescriptionBox;
import JUMBF.JUMBFSuperBox;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Victor Ojeda
 */
public class BuilderWeb {
    
    private final boolean isMainBox;
    private final JLINKSuperBox jlink;
    private final BuilderWeb parent;
    private short BoxInstance;
    
    public BuilderWeb (boolean mainBox, BuilderWeb parent, short BoxInstance) throws Exception{
        this.isMainBox = mainBox;
        this.jlink = new JLINKSuperBox();
        this.parent = parent;
        this.BoxInstance = BoxInstance;
    }
    
    public void setJLINKDescriptionBox(String label, int id) throws Exception{
        jlink.setDescriptionBox(new JUMBFDescriptionBox(Common.Values.TYPE_JLINK_Metadata_Elements, (byte) 0b00001111, label, id));
    }
    
    public void setXMLMetadata(JLINKImage image, String dir) throws Exception{
        File f;
        JUMBFSuperBox XMLContentBox;
        String metadata;
        OutputStream out;
        
        f = new File(image.getAppPath() + File.separator + dir + File.separator + "metadata_" + image.getLabel() + ".xml");
        metadata = XMLBuilder(image);
        out = new FileOutputStream(f);
        out.write(metadata.getBytes());   
        XMLContentBox = new JUMBFSuperBox(Common.Values.TYPE_XMLContentType, (byte) 0b1111, "XML Metadata file in JLINK: " + jlink.getDescriptionBox().getLabel(), 1);
        XMLContentBox.addData(f);
        jlink.setXMLContentBox(XMLContentBox);
    }
    
    public void addJUMBFContentBox(JLINKImage image, JLINKSuperBox parent, int id, String dir) throws Exception{
        
        byte toggle;
        JUMBFDescriptionBox box;        
        JUMBFSuperBox jsb;
        JUMBFContentBox jcb;
        Path path;
        File f;
        
        toggle = (byte) 0b0000;
        toggle |= ((byte) 0b0001); //Is Requestable
        toggle |= ((byte) 0b0010); //Has Label
        toggle |= ((byte) 0b0100); //Has Id
        toggle |= ((byte) 0b1000); //Has Signature
        //box = new JUMBFDescriptionBox(Common.Values.TYPE_codestreamContentType, toggle, image.getLabel(), id);
        box = new JUMBFDescriptionBox(Common.Values.TYPE_ContiguousCodestream, toggle, image.getLabel(), id);
        jsb = new JUMBFSuperBox();
        jsb.setDescriptionBox(box);

        path = Paths.get(image.getAppPath() + File.separator + dir + File.separator + image.getTitle().replace(" ", "_") + ".jpeg");
        f = path.toFile();
        jcb = new JUMBFContentBox(f, box.getContentType());
        jsb.addContentBox(jcb);
        parent.addJUMBFBox(jsb);
    }
    
    public void addJUMBFContentBoxForSprite(int cont_sprite, JLINKSuperBox parent, int id, String appPath, String sprite_path) throws Exception{
        
        byte toggle;
        JUMBFDescriptionBox box;        
        JUMBFSuperBox jsb;
        JUMBFContentBox jcb;
        Path path;
        File f;
        
        toggle = (byte) 0b0000;
        toggle |= ((byte) 0b0001); //Is Requestable
        toggle |= ((byte) 0b0010); //Has Label
        toggle |= ((byte) 0b0100); //Has Id
        toggle |= ((byte) 0b1000); //Has Signature
        box = new JUMBFDescriptionBox(Common.Values.TYPE_ContiguousCodestream, toggle, Const.JLINKConst.SPRITE_LABEL + cont_sprite, id);
        jsb = new JUMBFSuperBox();
        jsb.setDescriptionBox(box);
        path = Paths.get(appPath + File.separator + sprite_path);
        f = path.toFile();
        jcb = new JUMBFContentBox(f, box.getContentType());
        jsb.addContentBox(jcb);
        parent.addJUMBFBox(jsb);
    }
    
    public JLINKSuperBox getJLINK(){
        return this.jlink;
    }
    
    public BuilderWeb getParent(){
        return this.parent;
    }
    
    public short getBoxInstance() {
        return this.BoxInstance;
    }
    
    public void setBoxInstance(short BoxInstance) {
        this.BoxInstance = BoxInstance;
    }
    
    public String XMLBuilder(JLINKImage image){
        StringBuilder sb = new StringBuilder("<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"XMP Core 5.5.0\">\n" +
                                        "\t<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n" +
                                        "\t\t<rdf:Description rdf:about=\"\" xmlns:xmp=\"http://ns.adobe.com/xap/1.0/\" xmlns:umf=\"http://ns.intel.com/umf/2.0\">\n");
        sb.append("<umf:next-id>0</umf:next-id>\n" +
    "            \n" +
    "            <umf:schemas>\n" +
    "                <rdf:Bag>\n" +
    "                    <rdf:li rdf:parseType=\"Resource\">\n" +
    "                        <!-- JLINK Metadata -->\n" +
    "                        <umf:schema>scene</umf:schema>\n" +
    "                        <umf:descriptors>\n" +
    "                            \n" +
    "                            <rdf:Bag>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:name>version</umf:name>\n" +
    "                                    <umf:type>integer</umf:type>\n" +
    "                                </rdf:li>\n" +
    "\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:name>title</umf:name>\n" +
    "                                    <umf:type>string</umf:type>\n" +
    "                                </rdf:li>\n" +
    "\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:name>note</umf:name>\n" +
    "                                    <umf:type>string</umf:type>\n" +
    "                                </rdf:li>\n" +
    "\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "\n" +
    "                                    <umf:schema>viewport</umf:schema>\n" +
    "\n" +
    "                                    <umf:descriptors>\n" +
    "                                        <rdf:Bag>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>x</umf:name>\n" +
    "                                                <umf:type>real</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>y</umf:name>\n" +
    "                                                <umf:type>real</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>xFOV</umf:name>\n" +
    "                                                <umf:type>real</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>yFOV</umf:name>\n" +
    "                                                <umf:type>real</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>id</umf:name>\n" +
    "                                                <umf:type>integer</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "                                        </rdf:Bag>\n" +
    "                                    </umf:descriptors>\n" +
    "                                </rdf:li>\n" +
    "\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:schema>image</umf:schema>\n" +
    "\n" +
    "                                    <umf:descriptors>\n" +
    "                                        <rdf:Bag>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>format</umf:name>\n" +
    "                                                <umf:type>string</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>href</umf:name>\n" +
    "                                                <umf:type>string</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "                                        </rdf:Bag>\n" +
    "                                    </umf:descriptors>\n" +
    "                                </rdf:li>\n" +
    "                            </rdf:Bag>\n" +
    "                        </umf:descriptors>\n" +
    "                    </rdf:li>\n" +
    "                    \n" +
    "                    <rdf:li rdf:parseType=\"Resource\">\n" +
    "                        <umf:schema>link</umf:schema>\n" +
    "                    \n" +
    "                        <umf:descriptors>\n" +
    "                            <rdf:Bag>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:schema>region</umf:schema>\n" +
    "                                    \n" +
    "                                    <umf:descriptors>\n" +
    "                                        <rdf:Bag>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>shape</umf:name>\n" +
    "                                                <umf:type>string</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "                                            \n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>x</umf:name>\n" +
    "                                                <umf:type>real</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "                                            \n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>y</umf:name>\n" +
    "                                                <umf:type>real</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "                                            \n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>w</umf:name>\n" +
    "                                                <umf:type>real</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "                                            \n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>h</umf:name>\n" +
    "                                                <umf:type>real</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "                                            \n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <umf:name>rotation</umf:name>\n" +
    "                                                <umf:type>real</umf:type>\n" +
    "                                            </rdf:li>\n" +
    "                                            \n" +
    "                                        </rdf:Bag>\n" +
    "                                    </umf:descriptors>\n" +
    "                                </rdf:li>\n" +
    "                    \n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:name>duration</umf:name>\n" +
    "                                    <umf:type>integer</umf:type>\n" +
    "                                </rdf:li>\n" +
    "                                \n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:name>vpid</umf:name>\n" +
    "                                    <umf:type>integer</umf:type>\n" +
    "                                </rdf:li>\n" +
    "                                \n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:name>sprite</umf:name>\n" +
    "                                    <umf:type>string</umf:type>\n" +
    "                                </rdf:li>\n" +
    "                                \n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:name>to</umf:name>\n" +
    "                                    <umf:type>string</umf:type>\n" +
    "                                </rdf:li>\n" +
    "                                \n" +
    "                            </rdf:Bag>\n" +
    "                        </umf:descriptors>\n" +
    "                    </rdf:li>\n" +
    "                </rdf:Bag>\n" +
    "\n" +
    "            </umf:schemas>");

            sb.append("<umf:metadata>\n" +
    "                <rdf:Bag>\n" +
    "                    <rdf:li rdf:parseType=\"Resource\">\n" +
    "                        <umf:schema>scene</umf:schema>\n" +
    "                        <umf:set>\n" +
    "                            <rdf:Bag>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <rdf:value>" + image.getVersion() + "</rdf:value>\n" +
    "                                    <umf:name>version</umf:name>\n" +
    "                                </rdf:li>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <rdf:value>" + image.getTitle() + "</rdf:value>\n" +
    "                                    <umf:name>title</umf:name>\n" +
    "                                </rdf:li>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <rdf:value>" + image.getNote() + "</rdf:value>\n" +
    "                                    <umf:name>note</umf:name>\n" +
    "                                </rdf:li>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:schema>viewport</umf:schema>\n" +
    "                                    <umf:set>\n" +
    "                                        <rdf:Bag>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getViewport_X() + "</rdf:value>\n" +
    "                                                <umf:name>x</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getViewport_Y() + "</rdf:value>\n" +
    "                                                <umf:name>y</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getViewport_xFov() + "</rdf:value>\n" +
    "                                                <umf:name>xFOV</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getViewport_yFov() + "</rdf:value>\n" +
    "                                                <umf:name>yFOV</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getViewport_Id() + "</rdf:value>\n" +
    "                                                <umf:name>id</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                        </rdf:Bag>\n" +
    "                                    </umf:set>\n" +
    "                                </rdf:li>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:schema>image</umf:schema>\n" +
    "                                    <umf:set>\n" +
    "                                        <rdf:Bag>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getImage_format() + "</rdf:value>\n" +
    "                                                <umf:name>format</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getImage_Href() + "</rdf:value>\n" +
    "                                                <umf:name>href</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                        </rdf:Bag>\n" +
    "                                    </umf:set>\n" +
    "                                </rdf:li>\n" +
    "                            </rdf:Bag>\n" +
    "                        </umf:set>\n" +
    "                    </rdf:li>\n" +
    "                    <rdf:li rdf:parseType=\"Resource\">\n" +
    "                        <umf:schema>link</umf:schema>\n" +
    "                        <umf:set>\n" +
    "                            <rdf:Bag>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <umf:schema>region</umf:schema>\n" +
    "                                    <umf:set>\n" +
    "                                        <rdf:Bag>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getLink_region_shape() + "</rdf:value>\n" +
    "                                                <umf:name>shape</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getLink_region_X() + "</rdf:value>\n" +
    "                                                <umf:name>x</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getLink_region_Y() + "</rdf:value>\n" +
    "                                                <umf:name>y</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getLink_region_H() + "</rdf:value>\n" +
    "                                                <umf:name>width</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getLink_region_W() + "</rdf:value>\n" +
    "                                                <umf:name>height</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                                <rdf:value>" + image.getLink_region_rotation() + "</rdf:value>\n" +
    "                                                <umf:name>rotation</umf:name>\n" +
    "                                            </rdf:li>\n" +
    "                                        </rdf:Bag>\n" +
    "                                    </umf:set>\n" +
    "                                </rdf:li>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <rdf:value>" + image.getLink_duration() + "</rdf:value>\n" +
    "                                    <umf:name>duration</umf:name>\n" +
    "                                </rdf:li>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <rdf:value>" + image.getLink_Vpid() + "</rdf:value>\n" +
    "                                    <umf:name>vpid</umf:name>\n" +
    "                                </rdf:li>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <rdf:value>" + image.getLink_sprite() + "</rdf:value>\n" +
    "                                    <umf:name>sprite</umf:name>\n" +
    "                                </rdf:li>\n" +
    "                                <rdf:li rdf:parseType=\"Resource\">\n" +
    "                                    <rdf:value>" + image.getLink_to() + "</rdf:value>\n" +
    "                                    <umf:name>to</umf:name>\n" +
    "                                </rdf:li>\n" +
    "                            </rdf:Bag>\n" +
    "                        </umf:set>\n" +
    "                    </rdf:li>\n" +
    "                </rdf:Bag>\n" +
    "            </umf:metadata>"
            + "\t\t</rdf:Description>\n" +
                      "\t</rdf:RDF>\n" +
                      "</x:xmpmeta>\n" +
                      "<?xpacket end=\"w\"?>");
        return sb.toString();   
    }
    

}
