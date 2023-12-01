package View;
import Controller.FontColorActionListener;
import Controller.FontStyleActionListener;
import Controller.MyStyleListener;
import Controller.FontSizeActionListener;
import Controller.InsertImageActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewDocument extends JFrame {
    JPanel southPanel; //At south of frame to add buttons to implement functionalities.
    JButton applyStyle;//to apply font style and font size
    JButton applyColorBtn; // to apply color using color chooser
    JMenuBar menuBar;
    JMenu fileMenu, fontMenu, styleMenu, insertMenu;
    JMenuItem saveAsPDF, saveAsWord;
    JMenuItem font12, font14, font16;
    JMenuItem insertImage;
    MyStyleListener styleListener; // Controller class to manage all changings in the text.
    FontSizeActionListener fontSizeActionListener;
    FontStyleActionListener fontStyleActionListener;
    FontColorActionListener fontColorActionListener;
    InsertImageActionListener insertImageActionListener;
    JMenuItem arialStyle, timesRomanStyle, courierStyle;
    JTextPane textPane; // To write text in the document.
    public NewDocument(){
        this.menuBar = new JMenuBar(); // to make navigation bar.
        this.setJMenuBar(menuBar);

        this.fileMenu = new JMenu("File");//To display option for file selection.
        this.menuBar.add(fileMenu);

        this.saveAsPDF = new JMenuItem("Save As PDF");
        fileMenu.add(this.saveAsPDF);

        this.saveAsWord = new JMenuItem("Save As Word");
        fileMenu.add(this.saveAsWord);

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
        this.textPane.setContentType("text/html");

        this.setLayout(new BorderLayout());
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);
        this.add(new JScrollPane(this.textPane), BorderLayout.CENTER);


        southPanel = new JPanel(new FlowLayout());
        applyStyle = new JButton("Apply Style");
        applyColorBtn = new JButton("Apply Color");

        styleListener = new MyStyleListener(this);
        fontColorActionListener = new FontColorActionListener(this, styleListener);


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



    public void display(){
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
}
