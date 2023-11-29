package Controller;

import View.NewDocument;

import javax.swing.JColorChooser;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontColorActionListener implements ActionListener {
    private Color selectedColor;
    private NewDocument doc;
    private MyStyleListener styleListener;
    public FontColorActionListener(NewDocument doc, MyStyleListener styleListener){
        this.doc = doc;
        this.styleListener = styleListener;
    }
    private int font;
    @Override
    public void actionPerformed(ActionEvent e) {
        this.selectedColor = JColorChooser.showDialog(this.doc, "Choose Text Color", Color.BLACK);
        this.styleListener.applyColor();
    }

    public Color getFontColor(){
        return this.selectedColor;
    }
}
