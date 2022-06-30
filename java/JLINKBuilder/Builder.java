package JLINKBuilder;

import JLINKLibrary.*;
import JUMBF.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author carlos
 */
public class Builder implements ActionListener {
    
    private JFrame frame;
    final JLINKSuperBox jlink;
    private final boolean isMainBox;
    private final Builder parent;
    private JTextField tfLabel;
    private JTextField tfID;
    private short BoxInstance = 1;
    
    protected Builder(String title, boolean mainBox, Builder parent, short BoxInstance) throws Exception{
        this.BoxInstance = BoxInstance;
        jlink = new JLINKSuperBox();
        setJLINKDescriptionBoxFrame("Set JLINK DescriptionBox data", BoxInstance);
        isMainBox = mainBox;
        if(!mainBox)
            System.out.println("Creada nueva JLINK");
        this.parent = parent;
    }
    
    private void setBuilderFrame(String title) {
        frame = new JFrame(title);
        
        JButton b1 = new JButton("Set XML metadata");
        JButton b3 = new JButton("Add JLINK");
        JButton b4 = new JButton("Add JUMBF Content Box");
        JButton b5 = new JButton("Done");
        JButton b6 = new JButton("Cancel");
        JButton baux = new JButton();
        
        b1.setBounds(200, 100, 200, 100);
        b3.setBounds(200, 200, 200, 100);
        b4.setBounds(600, 200, 200, 100);
        b5.setBounds(200, 400, 200, 100);
        b6.setBounds(600, 400, 200, 100);
        baux.setBounds(0, 0, 0, 0);
        baux.setVisible(false);
        
        b1.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);
        
        frame.add(b1);
        frame.add(b3);
        frame.add(b4);
        frame.add(b5);
        frame.add(b6);
        frame.add(baux);
        frame.setSize(900, 600);
        frame.setVisible(true);
    }
    
    private void setJLINKDescriptionBoxFrame(String title, short BoxInstance) {
        Common.Values.setMap();
        
        frame = new JFrame(title);

        tfLabel = new JTextField();
        tfID = new JTextField();

        JLabel labelLabel = new JLabel("Set JLINK box Label");
        JLabel labelId = new JLabel("Set JLINK box Id");
        JButton b1 = new JButton("Continue");
        JCheckBox Bit1 = new JCheckBox("Is Requestable");
        JCheckBox Bit2 = new JCheckBox("Has Label");
        JCheckBox Bit3 = new JCheckBox("Has ID");
        JCheckBox Bit4 = new JCheckBox("Has Signature");
        
        JButton baux = new JButton();
        
        baux.setVisible(false);
        
        tfLabel.setBounds(50, 60, 300, 30);
        tfID.setBounds(50, 140, 50, 30);
        b1.setBounds(250, 300, 100, 40);
        labelLabel.setBounds(50, 20, 300, 30);
        labelId.setBounds(50, 100, 300, 30);
        Bit1.setBounds(200, 120, 200, 20);
        Bit2.setBounds(200, 140, 200, 20);
        Bit3.setBounds(200, 160, 200, 20);
        Bit4.setBounds(200, 180, 200, 20);
        
        tfLabel.addActionListener(this);
        tfID.addActionListener(this);
        b1.addActionListener(this);
        Bit1.addActionListener(this);
        Bit2.addActionListener(this);
        Bit3.addActionListener(this);
        Bit4.addActionListener(this);
        
        frame.setSize(400, 400);
        frame.getContentPane().add(tfLabel);
        frame.getContentPane().add(tfID);
        frame.getContentPane().add(b1);
        frame.getContentPane().add(labelLabel);
        frame.getContentPane().add(labelId);
        frame.getContentPane().add(Bit1);
        frame.getContentPane().add(Bit2);
        frame.getContentPane().add(Bit3);
        frame.getContentPane().add(Bit4);
        
        frame.getContentPane().add(baux);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) { //if button is pressed then this changes button text
        switch(event.getActionCommand()) {
            case "Set XML metadata":
                fileGetter fg = new fileGetter();
                try{
                    File f = fg.getFile("Select a XML file");
                    if(f.getName().endsWith(".xml") || f.getName().endsWith(".xsd") || f.getName().endsWith(".txt")){
                        JUMBFSuperBox XMLContentBox = new JUMBFSuperBox(Common.Values.TYPE_XMLContentType, (byte) 0b1111, "XML Metadata file in JLINK: " + jlink.getDescriptionBox().getLabel(), 1);
                        XMLContentBox.addData(f);
                        jlink.setXMLContentBox(XMLContentBox);
                    } else {
                        System.out.println("File is not XML");
                    }
                } catch (Exception e){
                    System.out.println("Failed adding XML metadata");
                }
                
                fg.dispose();
                break;
                
            case "Add JLINK":
                try {
                    new Builder("Adding JLINK", false, this, BoxInstance);
                    BoxInstance++;
                } catch (Exception ex) {
                    Logger.getLogger(Builder.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
                
            case "Add JUMBF Content Box":
                descriptionBoxFrame dbf;
                try {
                    dbf = new descriptionBoxFrame(jlink, BoxInstance);
                    BoxInstance++;
                } catch (Exception ex) {
                    Logger.getLogger(Builder.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                break;
                
            case "Done":
                frame.dispose();
                JLINKUtils utils = new JLINKUtils();
                if(jlink != null){
                    try {
                        if(isMainBox){
                            //utils.JLINKToBox(jlink, jlink.getDescriptionBox().getLabel(), BoxInstance);
                            //utils.DisplayJlink(jlink);
                        }
                            
                        else {
                            this.parent.getJLINK().addJLINK(this.jlink);
                            this.parent.setBoxInstance(this.getBoxInstance());
                            //utils.JLINKToBox(jlink, jlink.getDescriptionBox().getLabel(), BoxInstance);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Builder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                
            case "Continue":
                try {
                    jlink.setDescriptionBox(new JUMBFDescriptionBox(Common.Values.TYPE_JLINK_Metadata_Elements, (byte) 0b00001111, tfLabel.getText(), Integer.parseInt(tfID.getText())));
                } catch (Exception ex) {
                    Logger.getLogger(Builder.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.dispose();
                if(isMainBox){
                    setBuilderFrame("Building JLINK");
                }

                else {
                    setBuilderFrame("Adding JLINK");
                }
                
                break;
                
            case "Cancel":
                frame.dispose();
                break;
        }
    }
    
    public JLINKSuperBox getJLINK(){
        return this.jlink;
    }
    
    public short getBoxInstance() {
        return this.BoxInstance;
    }
    
    public void setBoxInstance(short BoxInstance) {
        this.BoxInstance = BoxInstance;
    }

}
