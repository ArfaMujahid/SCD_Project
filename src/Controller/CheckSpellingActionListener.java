package Controller;

import View.NewDocument;

import javax.swing.*;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CheckSpellingActionListener implements ActionListener {
    private NewDocument newDocument;
    private MisspelledWordListener misspelledWordListener;

    public CheckSpellingActionListener(NewDocument newDocument) {
        this.newDocument = newDocument;
        this.misspelledWordListener = new MisspelledWordListener(newDocument.getTextPane());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clearPreviousHighlights();
        checkSpelling();
    }

    private void clearPreviousHighlights() {
        StyledDocument doc = newDocument.getTextPane().getStyledDocument();
        doc.setCharacterAttributes(0, newDocument.getTextPane().getText().length(),StyleContext.getDefaultStyleContext().getEmptySet(), true);
    }

    private void checkSpelling() {
        JTextPane textPane = newDocument.getTextPane();
        String text = textPane.getText();
        List<String> misspelledWords = misspelledWordListener.getMisspelledWords(text);

        for (String misspelledWord : misspelledWords) {
            List<String> suggestions = misspelledWordListener.getSuggestions(misspelledWord);
            if (!suggestions.isEmpty()) {
                StringBuilder message = new StringBuilder("Misspelled Word: " + misspelledWord + "\nSuggestions:\n");
                for (String suggestion : suggestions) {
                    message.append("- ").append(suggestion).append("\n");
                }

                JOptionPane.showMessageDialog(
                        newDocument,
                        message.toString(),
                        "Spelling Suggestions",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
}
