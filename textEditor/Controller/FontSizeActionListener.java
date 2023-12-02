package Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontSizeActionListener implements ActionListener {
    private int fontSize;
    @Override
    public void actionPerformed(ActionEvent e) {
        String fontSizeText = ((JMenuItem) e.getSource()).getText();

        this.fontSize = Integer.parseInt(fontSizeText);
    }

    public int getFontSize(){
        return this.fontSize;
    }
}