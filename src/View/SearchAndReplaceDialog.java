package View;

import Controller.SearchAndReplaceActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchAndReplaceDialog extends JDialog {
    private NewDocument document;
    private JPanel searchPanel;//to contain search label and input field.
    private JPanel replacePanel;//to contain replace label and input field.
    private JPanel buttonsPanel;//to contains search and replace buttons.
    private JLabel searchLabel, replaceLabel;
    private JTextField searchField, replaceField;
    private JButton searchBtn, replaceBtn;
    private SearchAndReplaceActionListener searchAndReplaceActionListener;
    public SearchAndReplaceDialog(NewDocument doc, SearchAndReplaceActionListener searchAndReplaceActionListener){
        this.searchAndReplaceActionListener = searchAndReplaceActionListener;
        this.document = doc;
        this.setLayout(new GridLayout(3, 1));
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        this.searchPanel = new JPanel(new FlowLayout());
        this.replacePanel = new JPanel(new FlowLayout());
        this.buttonsPanel = new JPanel(new FlowLayout());

        this.searchLabel = new JLabel("Data for searching");
        this.replaceLabel = new JLabel("Data for Replacing");

        this.searchField = new JTextField(10);
        this.replaceField = new JTextField(10);

        this.searchBtn = new JButton("Search");
        this.replaceBtn = new JButton("Replace");

        //Now adding JComponents in panel and panels to this dialog box.
        this.searchPanel.add(this.searchLabel);
        this.searchPanel.add(this.searchField);

        this.replacePanel.add(this.replaceLabel);
        this.replacePanel.add(this.replaceField);

        this.buttonsPanel.add(this.searchBtn);
        this.buttonsPanel.add(this.replaceBtn);

        this.add(this.searchPanel);
        this.add(this.replacePanel);
        this.add(this.buttonsPanel);

        //Initialize and add actionListeners.
        this.searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int searches = SearchAndReplaceDialog.this.searchAndReplaceActionListener.getTotalSearchesAppear(
                        SearchAndReplaceDialog.this.searchField.getText() //Giving text of search field.
                );
                SearchAndReplaceDialog.this.showSearchesAppear(searches);
            }
        });
        this.replaceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final boolean hasReplaced = SearchAndReplaceDialog.this.searchAndReplaceActionListener.replace(
                        //Giving text of search field.
                        SearchAndReplaceDialog.this.searchField.getText(), SearchAndReplaceDialog.this.replaceField.getText()
                );
                //To show msg after replacing function called.
                SearchAndReplaceDialog.this.showReplaceMessage(hasReplaced);
            }
        });
    }

    public void display(){
        this.setVisible(true);
    }
    private void showSearchesAppear(final int searches){ //Shows a dialog box in which total searches will be shown.
        JOptionPane.showMessageDialog(this, searches + " searches appear");
    }
    public void showReplaceMessage(final Boolean hasReplaced){
        if(hasReplaced){
            JOptionPane.showMessageDialog(this, "Data has replaced");
        }
        else{
            JOptionPane.showMessageDialog(this, "Data not found for replacement");
        }
    }

}
