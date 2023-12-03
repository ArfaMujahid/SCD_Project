package View;
import Controller.LoadFileActionListener;
import Model.DatabaseWork;
import Model.GetFileData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.List;

//Class to show the list of documents that logged in user has made
// and show option to create new document.
public class FavouriteDocumentsList extends JFrame {
    private JButton createNewDocumentBtn;
    private JButton[] createdDocumentListBtn;
    private JPanel panel;//implementation is remaining from initilization.
    private Connection connection;
    private String name;
    LoadFileActionListener loadFileActionListener;

    public FavouriteDocumentsList(Connection conn, final String name){
        GetFileData getFileData = new GetFileData();
        getFileData.setConnection(conn);
        DatabaseWork DB = new DatabaseWork(conn);
        int count = DB.getTotalFavouriteDocuments(name);//Make buttons equal to this count;
        //Count shows total document created by a user.
        this.name = name;
        this.connection = conn;
        this.setLocationRelativeTo(null);
        this.createNewDocumentBtn = new JButton("Create Document");
        this.setSize(new Dimension(500, 500));

        this.addWindowListener(new WindowAdapter() { //to remove this frame on click cross button.
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        this.createNewDocumentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewDocument(FavouriteDocumentsList.this.connection, name).display("");
            }
        });

        this.setLayout(new GridLayout(count + 1, 1));
        this.add(this.createNewDocumentBtn);
        if(count != 0){
            this.createdDocumentListBtn = new JButton[count]; //initialize array of buttons
            //now add buttons of list in the frame.
            List<String> documentNames = DB.getFavouriteDocumentNames(this.name); //Getting names of documents that
            //logged in user made recently.
            int i = 0;
            loadFileActionListener = new LoadFileActionListener(this.connection, this.name);
            for(String names : documentNames){ //Loop initilizing each index of button array with
                //new created Button and adding action listener to each button.
                JButton btn = new JButton("Open " + names);
                btn.addActionListener(this.loadFileActionListener);
                createdDocumentListBtn[i++] = btn;
            }
            for(int j = 0; j < count; j++){ //Loop adding buttons to open recent made docuemnts
                this.add(createdDocumentListBtn[j]);
            }
        }

    }

    public void displayList(){
        this.setVisible(true);
    }
}
