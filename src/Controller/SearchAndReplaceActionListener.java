package Controller;

import View.NewDocument;
import View.SearchAndReplaceDialog;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;

public class SearchAndReplaceActionListener implements KeyListener {
    private NewDocument document;
    private SearchAndReplaceDialog searchAndReplaceDialog;
    public SearchAndReplaceActionListener(NewDocument doc){
        this.document = doc;
        this.searchAndReplaceDialog = new SearchAndReplaceDialog(this.document, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Control+F pressed, show dialog here to take input for search and replace.
        if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown())  {
            this.searchAndReplaceDialog.display();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    public int getTotalSearchesAppear(final String dataToSearch){
        String text = this.document.getTextPane().getText();
        int countOccurrences = 0;
        int index = text.indexOf(dataToSearch);

        while(index != -1){
            countOccurrences++;
            index = text.indexOf(dataToSearch, index + 1); //to start searching after the previous occurrence.
        }
        return countOccurrences;
    }
    public boolean replace(final String dataToSearch, final String dataToInsert){
        String Original = this.document.getTextPane().getText();
        String updated = Original.replaceAll(dataToSearch, dataToInsert);
        this.document.getTextPane().setText(updated);
        return (!(Original.equals(updated)));
    }

}
