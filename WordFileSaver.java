package Model;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.*;

public class WordFileSaver {

    public static void main(String[] args) {
        // Assuming you have a JTextPane named textPane
        JTextPane textPane = new JTextPane();

        // Set the content in your JTextPane
        textPane.setText("hello i am noor.");

        // Save the content to a Word file
        saveToWordFile(textPane, "output.docx");
    }

    public static void saveToWordFile(JTextPane textPane, String filePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();

            // Set the content from the JTextPane to the Word document
            run.setText(textPane.getText());

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                document.write(outputStream);
             //   JOptionPane.showMessageDialog(this, "Export successful!");
            } catch (IOException e) {
                e.printStackTrace();
              //  JOptionPane.showMessageDialog(this, "Error exporting document: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
           // JOptionPane.showMessageDialog(this, "Error creating document: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
