package Controller;

import View.NewDocument;


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyStyleListener implements ActionListener {
    NewDocument doc;
    public MyStyleListener(final NewDocument doc){
        this.doc = doc;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(((JButton)e.getSource()).getText().equals("Apply Style") ){
            AttributeSet attributeSet = this.doc.getTextPane().getCharacterAttributes();

            //creating new desired style.
            SimpleAttributeSet newAttributes = new SimpleAttributeSet();
            StyleConstants.setFontFamily(newAttributes, this.doc.getFontStyleActionListener().getFontStyle());
            StyleConstants.setFontSize(newAttributes, this.doc.getFontSizeActionListener().getFontSize());

            //Applying style to textPane.
            this.doc.getTextPane().setCharacterAttributes(newAttributes, false);
        }
        else{
            //To do when a color is selected. . .
        }

        //Following code to take everyline of the string.
//        String text = doc.getTextPane().getText();
//        String str[] = text.split("\n");
//        for(int i = 0; i < str.length; i++){
//            System.out.println(str[i]);
//        }
        //Following code to get selected text from textpane;
//        System.out.println(doc.getTextPane().getSelectedText());


    }
    public void applyColor(){
//        AttributeSet attributeSet = this.doc.getTextPane().getCharacterAttributes();
//
//        //creating new desired style.
//        SimpleAttributeSet newAttributes = new SimpleAttributeSet();
//        StyleConstants.setForeground(newAttributes, this.doc.getFontColorActionListener().getFontColor());
//
//        //Applying style to textPane.
//        this.doc.getTextPane().setCharacterAttributes(newAttributes, false);

        StyledDocument doc = this.doc.getTextPane().getStyledDocument();
        int start = this.doc.getTextPane().getSelectionStart();
        int end = this.doc.getTextPane().getSelectionEnd();

        if (start != end) {
            Style style = this.doc.getTextPane().addStyle("Color Style", null);
            StyleConstants.setForeground(style, this.doc.getFontColorActionListener().getFontColor());
            doc.setCharacterAttributes(start, end - start, style, false);
        }
    }
}
