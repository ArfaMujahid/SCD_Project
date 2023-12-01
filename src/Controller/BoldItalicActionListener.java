package Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoldItalicActionListener implements ActionListener {
    private MyStyleListener styleListener;
    public BoldItalicActionListener(MyStyleListener styleListener){
        this.styleListener = styleListener;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(((JMenuItem)e.getSource()).getText().equals("Bold")){
            this.styleListener.boldText();
        }
        else if(((JMenuItem)e.getSource()).getText().equals("Italic")){
            this.styleListener.italicText();
        }
    }
}
