package Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontStyleActionListener implements ActionListener {
    private String fontStyle;
    @Override
    public void actionPerformed(ActionEvent e) {
        this.fontStyle = ((JMenuItem) e.getSource()).getText();
    }

    public String getFontStyle(){
        return this.fontStyle;
    }
}
