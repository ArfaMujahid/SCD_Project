package Controller;

import Model.DatabaseWork;
import Model.GetFileData;
import Model.SaveFileAsPdf;
import View.NewDocument;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.SQLException;

public class SaveFileAsText implements ActionListener {
    private NewDocument document;
    private JFileChooser fileChooser;
    private SaveFileAsPdf saveFileAsPdf; //used to save data into data.
    private GetFileData getFileData;
    public SaveFileAsText(NewDocument document){
        this.document = document;
        this.fileChooser = new JFileChooser();
        this.saveFileAsPdf = new SaveFileAsPdf(this.document);//Only to use it's function to save data into Database.
        this.getFileData = new GetFileData();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(!this.document.getDocumentName().equals("")){ //If set document name if user has set during marking it as favourite.
            fileChooser.setSelectedFile(new File(this.document.getDocumentName()));
        }
        int returnVal = fileChooser.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            saveTextFile(file);
            try {
                if(this.getFileData.checkExists(this.document.getUserName(), this.document.getDocumentName())){ //condition for already made document by same user.when open from recents.
                    this.getFileData.deleteAllRecord(this.document.getUserName(), this.document.getDocumentName());
                }
                this.saveFileAsPdf.saveStyledTextToDatabase(this.document.getTextPane(), this.document.getConnection(), file.getName(), this.document.getUserName(), "text");
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

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
