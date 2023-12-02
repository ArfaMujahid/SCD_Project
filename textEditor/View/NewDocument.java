package View;
import Controller.*;
import Model.SaveFileAsPdf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

public class NewDocument extends JFrame {
    JPanel southPanel; //At south of frame to add buttons to implement functionalities.
    JButton applyStyle;//to apply font style and font size
    JButton applyColorBtn; // to apply color using color chooser
    JMenuBar menuBar;
    JMenu fileMenu, fontMenu, styleMenu, insertMenu, boldItalicMenu;
    JMenuItem saveAsPDF, saveAsWord, saveAsText;
    JMenuItem font12, font14, font16;
    JMenuItem insertImage, bold, italic;
    MyStyleListener styleListener; // Controller class to manage all changings in the text.
    FontSizeActionListener fontSizeActionListener;
    FontStyleActionListener fontStyleActionListener;
    FontColorActionListener fontColorActionListener;
    InsertImageActionListener insertImageActionListener;
    JMenuItem arialStyle, timesRomanStyle, courierStyle;
    JTextPane textPane; // To write text in the document.
    private Connection connection;
    private String userName, documentName;
    public NewDocument(Connection conn, final String userName){
        this.userName = userName;
        this.connection = conn;
        this.menuBar = new JMenuBar(); // to make navigation bar.
        this.setJMenuBar(menuBar);

        this.fileMenu = new JMenu("File");//To display option for file selection.
        this.menuBar.add(fileMenu);

        this.saveAsPDF = new JMenuItem("Save As PDF");
        this.saveAsPDF.addActionListener(new SaveFileAsPdf(this));
        fileMenu.add(this.saveAsPDF);

        this.saveAsWord = new JMenuItem("Save As Word");
        this.saveAsWord.addActionListener(new SaveFileAsWord(this));
        fileMenu.add(this.saveAsWord);

        this.saveAsText = new JMenuItem("Save As Text");
        this.saveAsText.addActionListener(new SaveFileAsText(this));
        fileMenu.add(this.saveAsText);

        fontMenu = new JMenu("Font"); //To display option for font size selection.
        this.menuBar.add(fontMenu);

        this.font12 = new JMenuItem("12");
        this.font14 = new JMenuItem("14");
        this.font16 = new JMenuItem("16");
        this.fontMenu.add(font12);
        this.fontMenu.add(font14);
        this.fontMenu.add(font16);

        fontSizeActionListener = new FontSizeActionListener();//Action listener class in controller
                                                              //to handle font Size of text.
        font12.addActionListener(fontSizeActionListener);
        font14.addActionListener(fontSizeActionListener);
        font16.addActionListener(fontSizeActionListener);

        styleMenu = new JMenu("Style"); //To display option for font style selection.
        this.menuBar.add(styleMenu);

        fontStyleActionListener = new FontStyleActionListener();//Action listener class in controller
                                            //to handle font style of text.
        this.arialStyle = new JMenuItem("Arial");
        this.arialStyle.addActionListener(fontStyleActionListener);
        this.timesRomanStyle = new JMenuItem("Times New Roman");
        this.timesRomanStyle.addActionListener(fontStyleActionListener);
        this.courierStyle = new JMenuItem("Courier New");
        this.courierStyle.addActionListener(fontStyleActionListener);

        styleMenu.add(arialStyle);
        styleMenu.add(timesRomanStyle);
        styleMenu.add(courierStyle);

        this.insertMenu = new JMenu("Insert");
        this.menuBar.add(insertMenu);

        this.insertImage = new JMenuItem("Insert Image");

        insertImageActionListener = new InsertImageActionListener(this);
        insertImage.addActionListener(insertImageActionListener);
        insertMenu.add(insertImage);

        this.textPane = new JTextPane();
        this.textPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                setTitle("File - Unsaved Changes");
            }
        });

        this.setLayout(new BorderLayout());
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);
        this.add(new JScrollPane(this.textPane), BorderLayout.CENTER);


        southPanel = new JPanel(new FlowLayout());
        applyStyle = new JButton("Apply Style");
        applyColorBtn = new JButton("Apply Color");

        styleListener = new MyStyleListener(this);
        fontColorActionListener = new FontColorActionListener(this, styleListener);

        this.boldItalicMenu = new JMenu("Bold & Italic");
        this.menuBar.add(this.boldItalicMenu);

        this.bold = new JMenuItem("Bold");
        this.bold.addActionListener(new BoldItalicActionListener(this.styleListener));
        this.italic = new JMenuItem("Italic");
        this.italic.addActionListener(new BoldItalicActionListener(this.styleListener));

        this.boldItalicMenu.add(this.bold);
        this.boldItalicMenu.add(this.italic);




        applyStyle.addActionListener(styleListener);
        applyColorBtn.addActionListener(this.fontColorActionListener);
        southPanel.add(applyStyle);
        southPanel.add(applyColorBtn);
        this.add(southPanel, BorderLayout.SOUTH);

        this.addWindowListener(new WindowAdapter() { //to remove this frame on click cross button.
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });


    }



    public void display(final String docName){
        if(!docName.equals("NA")){
            this.documentName = docName;
        }
        this.setVisible(true);
    }

    public JTextPane getTextPane(){
        return this.textPane;
    }

    public FontSizeActionListener getFontSizeActionListener(){
        return this.fontSizeActionListener;
    }
    public FontStyleActionListener getFontStyleActionListener(){
        return this.fontStyleActionListener;
    }

    public FontColorActionListener getFontColorActionListener(){
        return this.fontColorActionListener;
    }
    public Connection getConnection(){
        return this.connection;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getDocumentName(){
        return this.documentName;
    }
    public void setDocumentName(final String documentName){
        this.documentName = documentName;
    }
}
