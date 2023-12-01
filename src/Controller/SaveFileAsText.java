package Controller;

import View.NewDocument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveFileAsText implements ActionListener {
    private NewDocument document;
    private JFileChooser fileChooser;
    public SaveFileAsText(NewDocument document){
        this.document = document;
        this.fileChooser = new JFileChooser();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        int returnVal = fileChooser.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            saveTextFile(file);
        }
    }
    public void saveTextFile(File file){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath() + ".txt"))) {
            this.document.getTextPane().write(writer);
            this.document.setTitle(file.getName() + " - Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
