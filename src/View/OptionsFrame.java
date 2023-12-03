package View;

import Controller.OptionsFrameActionListener;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class OptionsFrame extends JFrame{
    private Connection connection;
    private String userName;
    private JButton openDocuments;
    private JButton openFavouriteDocuments;
    private JPanel panel1;
    private JPanel panel2;
    private OptionsFrameActionListener optionsFrameActionListener;
    public OptionsFrame(Connection connection, final String userName){
        this.connection = connection;
        this.userName = userName;

        //initializing data members.
        this.optionsFrameActionListener = new OptionsFrameActionListener(this.connection, userName);
        this.openDocuments = new JButton("Open Documents");
        this.openDocuments.setActionCommand("Open Documents");
        this.openDocuments.addActionListener(this.optionsFrameActionListener);
        this.openFavouriteDocuments = new JButton("Open Favourite Documents");
        this.openFavouriteDocuments.setActionCommand("Open Favourite Documents");
        this.openFavouriteDocuments.addActionListener(this.optionsFrameActionListener);

        //this.ButtonsPanel = new JPanel(new GridLayout(2, 1));
        this.setLayout(new GridLayout(2, 1));
        panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(this.openDocuments, gbc);
        this.add(panel1);

        panel2 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.fill = GridBagConstraints.BOTH;
        panel2.add(this.openFavouriteDocuments, gbc2);
        this.add(panel2);

        this.setSize(new Dimension(350, 350));
        this.setLocationRelativeTo(null);

        //now Add action Listeners.
        
    }
    public void display(){
        this.setVisible(true);
    }
}
