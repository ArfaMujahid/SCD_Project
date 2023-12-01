package Controller;

import View.NewDocument;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class InsertImageActionListener implements ActionListener {
    NewDocument doc;
    public InsertImageActionListener(NewDocument doc){
        this.doc = doc;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(((JMenuItem)e.getSource()).getText().equals("Insert Image")){
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedImageFile = fileChooser.getSelectedFile();
                insertImage(this.doc.getTextPane(), selectedImageFile);
            }
        }
        else{
            //this.document.showFileChooser();
            //showgui
        }
    }
    private static void insertImage(JTextPane textPane, File imageFile) {
        try {
            ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
            int width = imageIcon.getIconWidth();
            int height = imageIcon.getIconHeight();
            HTMLDocument doc = (HTMLDocument) textPane.getDocument();
            Element element = doc.getElement(doc.getDefaultRootElement(), StyleConstants.NameAttribute, HTML.Tag.BODY);
            String imgTag = "<img src='file:" + imageFile.getAbsolutePath() + "' width='" + width + "' height='" + height + "'>";
            doc.insertBeforeEnd(element, imgTag);
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }

}
