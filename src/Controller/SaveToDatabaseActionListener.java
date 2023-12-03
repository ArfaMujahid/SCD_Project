package Controller;
import Model.StyledTextSegment2;
import View.NewDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import  java.sql.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class SaveToDatabaseActionListener implements ActionListener {
    private NewDocument document;

    public SaveToDatabaseActionListener(NewDocument doc) {
        this.document = doc;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String documentName = document.getDocumentNameFromUser();
            if (documentName != null) {
                document.setDocumentName(documentName);
                saveStyledTextToDatabase(document.getTextPane(), document.getConnection(), document.getDocumentName(), document.getUserName(), "text");
                JOptionPane.showMessageDialog(document, "Document saved to database successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                document.setWindowTitle(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(document, "Error saving to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveStyledTextToDatabase(javax.swing.JTextPane textPane, java.sql.Connection connection, final String fileName, final String userName, final String type) {
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

            try {
                styledTextSegments.add(new StyledTextSegment2(doc.getText(start, end - start), attributes));
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
