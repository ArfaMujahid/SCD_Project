package Model;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveWordFile {

    public static void saveToWordFile(JTextPane textPane, String filePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();

            // Set the content from the JTextPane to the Word document
            run.setText(textPane.getText());

            // Only write to the file stream once
            document.write(fileOutputStream);

            // Optional: Display a success message
            // JOptionPane.showMessageDialog(null, "Export successful!");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle or log the IOException
            // JOptionPane.showMessageDialog(null, "Error exporting document: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle or log any other exceptions
            // JOptionPane.showMessageDialog(null, "Error creating document: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
