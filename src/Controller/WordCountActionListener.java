package Controller;
import View.NewDocument;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class WordCountActionListener implements ActionListener {

    private NewDocument newDocument;

    public WordCountActionListener(NewDocument newDocument) {
        this.newDocument = newDocument;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int wordCount = countWords();
        JOptionPane.showMessageDialog(newDocument, "Word Count: " + wordCount, "Word Count", JOptionPane.INFORMATION_MESSAGE);
    }

    private int countWords() {
        String text = newDocument.getTextPane().getText();
        String[] words = text.split("\\s+");
        return words.length;
    }
}

