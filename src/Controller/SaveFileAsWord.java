package Controller;

import Model.SaveWordFile;
import View.NewDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

public class SaveFileAsWord implements ActionListener {
    NewDocument document;
    SaveWordFile saveWordFile;
    public SaveFileAsWord(NewDocument doc){
        this.document = doc;
        this.saveWordFile = new SaveWordFile();
    }
    @Override
    public void actionPerformed(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println(file.getAbsolutePath());
            saveWordFile.saveToWordFile(this.document.getTextPane(), file.getAbsolutePath()+".docx");
        }
    }


}
