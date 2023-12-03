package Model;

import View.NewDocument;
import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaveFileAsPdf implements ActionListener {
    NewDocument document;
    GetFileData getFileData;
    public SaveFileAsPdf(NewDocument doc){
        this.getFileData = new GetFileData();
        this.document = doc;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if(!this.document.getDocumentName().equals("")){ //If set document name if user has set during marking it as favourite.
            fileChooser.setSelectedFile(new File(this.document.getDocumentName()));
        }
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(this.getFileData.checkExists(this.document.getUserName(), this.document.getDocumentName())){ //condition for already made document by same user.when open from recents.
                this.getFileData.deleteAllRecord(this.document.getUserName(), this.document.getDocumentName());
            }
            try {
                savePdfFile(file);//Running good. But not giving text after image.
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (DocumentException ex) {
                throw new RuntimeException(ex);
            }
            try {
                saveStyledTextToDatabase(this.document.getTextPane(), this.document.getConnection(), file.getName(), this.document.getUserName(), "pdf");
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    private void savePdfFile(File file) throws IOException, DocumentException {
// Create a new PDF document
        Document pdfDocument = new Document();
        PdfWriter.getInstance(pdfDocument, new FileOutputStream(file.getAbsolutePath() + ".pdf"));
        pdfDocument.open();

// Get the text from your textPane
        String text = this.document.getTextPane().getText();

// Parse the HTML content (assuming it's in HTML format)
        org.jsoup.nodes.Document htmlDocument = Jsoup.parse(text);

// Loop through the parsed elements
        Elements elements = htmlDocument.body().children();
        for (Element element : elements) {

            // Check if the element is an image
            if ((element.tagName().equalsIgnoreCase("p") && element.select("img").size() > 0) || element.tagName().equalsIgnoreCase("img")) {
                Element imgElement = element.select("img").first();
                String src = imgElement.attr("src");

                // Create an Image instance from the image data
                Image image = Image.getInstance(src);

                // Add the image to the PDF document
                pdfDocument.add(image);
            } else if (element.tagName().equalsIgnoreCase("p") && !element.text().trim().isEmpty()) {
                // Add text elements inside <p> tags as paragraphs
                pdfDocument.add(new Paragraph(element.text()));
            } else if (!element.text().trim().isEmpty()) {
                // Add text elements not inside <p> tags as paragraphs
                pdfDocument.add(new Paragraph(element.text()));
            }
        }

        // Close the PDF document
        pdfDocument.close();
        this.document.setTitle(file.getName() + " - Saved");

    }

    // Serialize and save styled text to the database
    public void saveStyledTextToDatabase(JTextPane textPane, Connection connection, final String fileName, final String userName, final String type) throws BadLocationException, SQLException {
        StyledDocument doc = textPane.getStyledDocument();
        List<StyledTextSegment2> styledTextSegments = new ArrayList<>();

        // Iterate through the document and save styled text segments
        for (int i = 0; i < doc.getLength(); ) {
            int start = i;
            AttributeSet attributes = doc.getCharacterElement(i).getAttributes();
            while (i < doc.getLength() && doc.getCharacterElement(i).getAttributes().isEqual(attributes)) {
                i++;
            }
            int end = i;

            styledTextSegments.add(new StyledTextSegment2(doc.getText(start, end - start), attributes));
        }

        // Serialize and save the styled text data to the database
        String insertQuery = "INSERT INTO document VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (StyledTextSegment2 segment : styledTextSegments) {
                // Serialize the segment to a byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                    objectOutputStream.writeObject(segment);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                byte[] serializedSegment = byteArrayOutputStream.toByteArray();

                // Set the values in the prepared statement
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, fileName);
                preparedStatement.setBytes(3, serializedSegment);
                preparedStatement.setObject(4, segment.getAttributes());
                preparedStatement.setObject(5, type);
                // Execute the insert query
                preparedStatement.executeUpdate();

            }
        }
    }



}

// Custom class to store styled text segments using Serialization.

