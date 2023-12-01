package Model;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SaveStyleDataMySql {
    static  int i = 0;
    public static void main(String[] args) throws BadLocationException {
        Connection connection = DBConnection();
        // Create a JFrame
        JFrame frame = new JFrame("Styled TextPane Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Create a JTextPane
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");

        // Create a JTextField for user input
        JTextField userInputField = new JTextField(10);

        // Create a button to apply styling
        JButton applyStyleButton = new JButton("Apply Style");

        // Add a listener to the Apply Style button
        applyStyleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = userInputField.getText();
                if (!userInput.isEmpty()) {
                    // Apply styling to the user's input
                    setStyleForUserInput(textPane, userInput);
                    userInputField.setText(""); // Clear the input field
                }
            }
        });

        // Create a panel to hold the input components
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(userInputField);
        inputPanel.add(applyStyleButton);
        //add image button
        JButton addImageBtn = new JButton("Add Image");
        inputPanel.add(addImageBtn);
        addImageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedImageFile = fileChooser.getSelectedFile();
                    insertImage(textPane, selectedImageFile);
                }
            }
        });

        JButton bulletBtn = new JButton("Bullet");
        inputPanel.add(bulletBtn);
        bulletBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                StyledDocument doc = textPane.getStyledDocument();
                String selectedText = textPane.getSelectedText();

                if (selectedText != null) {
                    String bullet = "\u2022";

                    // Get the initial attributes of the selected text
                    AttributeSet initialAttributes = doc.getCharacterElement(textPane.getSelectionStart()).getAttributes();

                    // Initialize a variable to keep track of the current position
                    int currentPosition = textPane.getSelectionStart();

                    while (currentPosition < textPane.getSelectionEnd()) {
                        // Find the end of the current style run
                        int endOfRun = currentPosition;
                        AttributeSet currentAttributes = doc.getCharacterElement(currentPosition).getAttributes();
                        while (currentPosition < textPane.getSelectionEnd() &&
                                currentAttributes.isEqual(doc.getCharacterElement(currentPosition).getAttributes())) {
                            currentPosition++;
                        }

                        // Apply the bullet point style to the current run
                        String newText = bullet + " " + selectedText.substring(endOfRun - textPane.getSelectionStart(), currentPosition - textPane.getSelectionStart())
                                .replace("\n", "\n" + bullet + " ");
                        try {
                            doc.remove(endOfRun, currentPosition - endOfRun);
                            doc.insertString(endOfRun, newText, initialAttributes);
                            currentPosition += newText.length();
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        JButton save = new JButton("SaveLoad");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Serialize and save styled text to the database
                    saveStyledTextToDatabase(textPane, connection);
                    System.out.println("Data has been saved to the database\nNow loading again");

                    // Deserialize and load the styled text from the database
                    loadStyledTextFromDatabase(textPane, connection);
                } catch (BadLocationException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Create a panel to hold the JTextPane and input components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(textPane), BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        mainPanel.add(save, BorderLayout.EAST);

        // Add the main panel to the JFrame
        frame.add(mainPanel);

        // Display the JFrame
        frame.setVisible(true);
    }


    // Apply styling to user input and insert it into the JTextPane
    private static void setStyleForUserInput(JTextPane textPane, String userInput) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("UserStyle", null);
        if(i == 0){
            StyleConstants.setFontSize(style, 16);
            StyleConstants.setForeground(style, Color.BLUE);
        }
        else{
            StyleConstants.setFontSize(style, 12);
            StyleConstants.setForeground(style, Color.RED);
        }
        StyleConstants.setFontFamily(style, "Courier New");

        StyleConstants.setBold(style, true);
        i++;

        try {
            doc.insertString(doc.getLength(), userInput + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Serialize and save styled text to the database
    private static void saveStyledTextToDatabase(JTextPane textPane, Connection connection) throws BadLocationException, SQLException {
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
        String insertQuery = "INSERT INTO styled_text_data (text, attributes) VALUES (?, ?)";

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
                preparedStatement.setBytes(1, serializedSegment);
                preparedStatement.setObject(2, segment.getAttributes());
                // Execute the insert query
                preparedStatement.executeUpdate();

            }
        }
    }

    // Deserialize and load styled text from the database
    private static void loadStyledTextFromDatabase(JTextPane textPane, Connection connection) throws SQLException {
        textPane.setText("");

        // Define the SQL query to retrieve the serialized data
        String selectQuery = "SELECT text, attributes FROM styled_text_data";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            // Execute the select query
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
    private static void insertImage(JTextPane textPane, File imageFile) {
        try {
            ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
            int width = imageIcon.getIconWidth();
            int height = imageIcon.getIconHeight();
            HTMLDocument doc = (HTMLDocument) textPane.getDocument();
            Element element = doc.getElement(doc.getDefaultRootElement(), StyleConstants.NameAttribute, HTML.Tag.BODY);
            String imgTag = "<img src='file:" + imageFile.getAbsolutePath() + "' width='" + width + "' height='" + height + "'>";
            doc.insertBeforeEnd(element, imgTag);
            System.out.println("Path: " + imageFile.getAbsolutePath());
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }
    public static Connection DBConnection(){
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            String DBurl = "jdbc:mysql://localhost:3306/texteditor";
            String userName = "root";
            String password = "!mySQL@786#";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(DBurl, userName, password);
            System.out.println("Database Connected");
            return conn;

        }
        catch(Exception e){

        }
        return null;
    }
}

// Custom class to store styled text segments using Serialization.
class StyledTextSegment2 implements Serializable {
    private String text;
    private AttributeSet attributes;

    public StyledTextSegment2(String text, AttributeSet attributes) {
        this.text = text;
        this.attributes = attributes;
    }

    public String getText() {
        return text;
    }

    public AttributeSet getAttributes() {
        return attributes;
    }
}
