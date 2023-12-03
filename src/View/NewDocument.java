package View;
import Controller.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

public class NewDocument extends JFrame {
    private final JMenuItem saveToDatabase;

    JButton checkSpellingBtn;
    JPanel southPanel; //At south of frame to add buttons to implement functionalities.
    JButton applyColorBtn; // to apply color using color chooser
    JMenuBar menuBar;
    JMenu fileMenu, fontMenu, styleMenu, insertMenu, boldItalicMenu, markAsFavouriteMenu, bulletMenu;
    JMenuItem saveAsPDF, saveAsWord, saveAsText;
    JMenuItem font12, font14, font16, font18, font20, font22;
    JMenuItem insertImage, bold, italic;
    JMenuItem arialStyle, timesRomanStyle, courierStyle;
    JMenuItem markAsFavouriteItem, bulletItem;
    MyStyleListener styleListener; // Controller class to manage all changings in the text.
    FontSizeActionListener fontSizeActionListener;
    FontStyleActionListener fontStyleActionListener;
    FontColorActionListener fontColorActionListener;
    InsertImageActionListener insertImageActionListener;
    MarkAsFavouriteActionListener markAsFavouriteActionListener;
    SearchAndReplaceActionListener searchAndReplaceActionListener;
    JTextPane textPane; // To write text in the document.
    private Connection connection;
    private String userName, documentName;
    public NewDocument(Connection conn, final String userName){
       // spellCheckerWrapper = new SpellCheckerWrapper();

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
        this.font18 = new JMenuItem("18");
        this.font20 = new JMenuItem("20");
        this.font22 = new JMenuItem("22");
        this.fontMenu.add(font12);
        this.fontMenu.add(font14);
        this.fontMenu.add(font16);
        this.fontMenu.add(font18);
        this.fontMenu.add(font20);
        this.fontMenu.add(font22);

        fontSizeActionListener = new FontSizeActionListener(this);//Action listener class in controller
                                                              //to handle font Size of text.
        font12.addActionListener(fontSizeActionListener);
        font14.addActionListener(fontSizeActionListener);
        font16.addActionListener(fontSizeActionListener);
        font18.addActionListener(fontSizeActionListener);
        font20.addActionListener(fontSizeActionListener);
        font22.addActionListener(fontSizeActionListener);

        styleMenu = new JMenu("Style"); //To display option for font style selection.
        this.menuBar.add(styleMenu);

        fontStyleActionListener = new FontStyleActionListener(this);//Action listener class in controller
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

        //Adding elements so that user can mark a document as favourite.
        this.markAsFavouriteMenu = new JMenu("Mark as Favourite");
        this.markAsFavouriteItem = new JMenuItem("Mark as Favourite");
        this.markAsFavouriteMenu.add(this.markAsFavouriteItem);
        markAsFavouriteActionListener = new MarkAsFavouriteActionListener(this.connection, this);
        this.markAsFavouriteItem.addActionListener(this.markAsFavouriteActionListener);
        this.menuBar.add(markAsFavouriteMenu);

        this.textPane = new JTextPane();
        this.textPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                setTitle("File - Unsaved Changes");
              //  checkSpelling();
            }
        });

        this.setLayout(new BorderLayout());
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);
        this.add(new JScrollPane(this.textPane), BorderLayout.CENTER);


        southPanel = new JPanel(new FlowLayout());
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

        applyColorBtn.addActionListener(this.fontColorActionListener);
        southPanel.add(applyColorBtn);
        this.add(southPanel, BorderLayout.SOUTH);

        this.addWindowListener(new WindowAdapter() { //to remove this frame on click cross button.
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        this.searchAndReplaceActionListener = new SearchAndReplaceActionListener(this);
        this.textPane.addKeyListener(this.searchAndReplaceActionListener);

        this.bulletMenu = new JMenu("Bullets");
        this.menuBar.add(this.bulletMenu);
        this.bulletItem = new JMenuItem("Add Bullets");
        this.bulletItem.addActionListener(this.styleListener);
        this.bulletMenu.add(this.bulletItem);

        this.saveToDatabase = new JMenuItem("Save");
        this.saveToDatabase.addActionListener(new SaveToDatabaseActionListener(this));
        this.menuBar.add(this.saveToDatabase);

        checkSpellingBtn = new JButton("Check Spelling");
        checkSpellingBtn.addActionListener(new CheckSpellingActionListener(this));

        southPanel.add(checkSpellingBtn);

    }

    public String getDocumentNameFromUser() {
        return JOptionPane.showInputDialog(this, "Enter document name:", "Document Name", JOptionPane.PLAIN_MESSAGE);
    }
    public void display(final String docName){
        this.documentName = docName;
        this.setVisible(true);
    }

    public JTextPane getTextPane(){
        return this.textPane;
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

    public void setWindowTitle(boolean isSaved) {
        if (isSaved) {
            setTitle("File - Saved Changes");
        } else {
            setTitle("File - Unsaved Changes");
        }
    }



}
