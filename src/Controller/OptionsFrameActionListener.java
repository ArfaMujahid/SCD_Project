//This class is to manage that which list of documents is to shown to the user.
//Do we have to show fav. documents list or simple documents list that user has made recently.
package Controller;

import Model.DatabaseWork;
import View.FavouriteDocumentsList;
import View.ListDocuments;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class OptionsFrameActionListener implements ActionListener {
    private ListDocuments listDocuments;
    private Connection connection;
    private String userName; // To keep record that which user's favourite documents have to be shown.
    private DatabaseWork databaseWork;
    public OptionsFrameActionListener(Connection connection, final String userName){
        this.connection = connection;
        this.userName = userName;
        this.databaseWork = new DatabaseWork(this.connection);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Open Documents")){
            this.listDocuments = new ListDocuments(this.connection, userName);
            this.listDocuments.displayList();
        }
        else if(e.getActionCommand().equals("Open Favourite Documents")){
            if(this.databaseWork.isFavouriteDocumentExists(this.userName)){
                FavouriteDocumentsList favouriteDocumentsList = new FavouriteDocumentsList(this.connection, this.userName);
                favouriteDocumentsList.displayList();
            }
            else{
                JOptionPane.showMessageDialog(null, "You have no favourite Documents");
            }
        }
    }
}
