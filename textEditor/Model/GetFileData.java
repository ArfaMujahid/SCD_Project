package Model;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetFileData {
    private static Connection connection;

    // Deserialize and load styled text from the database
    public void LoadDataofFile(JTextPane textPane, Connection connection, final String fileName) throws SQLException {
        System.out.println("Loading data filename = " + fileName);

        // Define the SQL query to retrieve the serialized data
        String selectQuery = "SELECT text, attributes FROM document where documentName = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            // Execute the select query
            preparedStatement.setString(1, fileName);
            ResultSet resultSet = preparedStatement.executeQuery();

            StyledDocument doc = textPane.getStyledDocument();
            while (resultSet.next()) {
                // Deserialize the segment from the database
                byte[] serializedSegment = resultSet.getBytes("text");
                byte[] serializedAttributes = resultSet.getBytes("attributes"); // Retrieve the attributes as bytes

                try (ByteArrayInputStream byteArrayInputStreamSegment = new ByteArrayInputStream(serializedSegment);
                     ByteArrayInputStream byteArrayInputStreamAttributes = new ByteArrayInputStream(serializedAttributes);
                     ObjectInputStream objectInputStreamSegment = new ObjectInputStream(byteArrayInputStreamSegment);
                     ObjectInputStream objectInputStreamAttributes = new ObjectInputStream(byteArrayInputStreamAttributes)) {

                    StyledTextSegment2 segment = (StyledTextSegment2) objectInputStreamSegment.readObject();

                    // Deserialize the attributes
                    AttributeSet attributes = (AttributeSet) objectInputStreamAttributes.readObject();

                    // Insert the text with the retrieved attributes
                    doc.insertString(doc.getLength(), segment.getText(), attributes);
                } catch (IOException | ClassNotFoundException | BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //Check Exists function will check that provided user has already made a file of this name or not.
    public boolean checkExists(final String userName, final String fileName){
        Boolean dataExists = false;
        String query = "select count(*) from document where username = ? and documentName = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, fileName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            int count = resultSet.getInt("count(*)");
            if(count != 0){
                dataExists = true;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dataExists;
    }
    public void deleteAllRecord(final String userName, final String fileName){
        System.out.println("delete data: username: " + userName + ", fileName: " + fileName);
        String query = "delete from document where username = ? and documentName = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, fileName);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void setConnection(Connection conn){
        this.connection =  conn;
    }

}
