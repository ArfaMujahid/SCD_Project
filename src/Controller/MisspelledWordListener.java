
        package Controller;

        import com.swabunga.spell.engine.SpellDictionaryHashMap;
        import com.swabunga.spell.event.SpellCheckEvent;
        import com.swabunga.spell.event.SpellCheckListener;
        import com.swabunga.spell.event.SpellChecker;
        import com.swabunga.spell.event.StringWordTokenizer;
        import com.swabunga.spell.event.TeXWordFinder;

        import javax.swing.*;
        import javax.swing.text.AttributeSet;
        import javax.swing.text.SimpleAttributeSet;
        import javax.swing.text.StyleConstants;
        import javax.swing.text.StyleContext;
        import javax.swing.text.StyledDocument;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

        public class MisspelledWordListener implements SpellCheckListener {

        private JTextPane textPane;

        private SpellChecker spellChecker;
        private List<String> misspelledWords;

        public MisspelledWordListener(JTextPane textPane) {
        this.textPane = textPane;
        misspelledWords = new ArrayList<>();
        initialize();
        }

        public List<String> getMisspelledWords(String text) {
        StringWordTokenizer texTok = new StringWordTokenizer(text, new TeXWordFinder());
        spellChecker.checkSpelling(texTok);
        return misspelledWords;
        }

        private static SpellDictionaryHashMap dictionaryHashMap;

        static {
        // Provide the full path to your dictionary file
        File dict = new File("C:\\Users\\Admin\\Music\\OneDrive\\Documents\\Sem 5\\SCD\\Project\\scdproject5\\TextEditor\\TextEditor\\src\\Controller\\dictionary.txt");
        try {
        dictionaryHashMap = new SpellDictionaryHashMap(dict);
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }
        }
        public String getCorrectedLine(String line) {
        List<String> misSpelledWords = getMisspelledWords(line);

        for (String misSpelledWord : misSpelledWords) {
        List<String> suggestions = getSuggestions(misSpelledWord);
        if (!suggestions.isEmpty()) {
        line = line.replace(misSpelledWord, suggestions.get(0));
        }
        }
        return line;
        }

        public List<String> getSuggestions(String misspelledWord) {
        @SuppressWarnings("unchecked")
        List<com.swabunga.spell.engine.Word> su99esti0ns = spellChecker.getSuggestions(misspelledWord, 0);
        List<String> suggestions = new ArrayList<>();
        for (com.swabunga.spell.engine.Word suggestion : su99esti0ns) {
        suggestions.add(suggestion.getWord());
        }
        return suggestions;
        }

        @Override
        public void spellingError(SpellCheckEvent event) {
        event.ignoreWord(true);
        String misspelledWord = event.getInvalidWord();
        misspelledWords.add(misspelledWord);
        highlightMisspelledWord(misspelledWord);

        // Print misspelled word to the console
        System.out.println("Misspelled word: " + misspelledWord);
        }


        private void initialize() {
        spellChecker = new SpellChecker(dictionaryHashMap);
        spellChecker.addSpellCheckListener(this);
        }

        private void highlightMisspelledWord(String word) {
        StyledDocument doc = textPane.getStyledDocument();
        String text = textPane.getText();
        int startPosition = 0;

        while (startPosition < text.length() && (startPosition = text.indexOf(word, startPosition)) >= 0) {
        int endPosition = startPosition + word.length();
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet underline = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Underline, true);
        doc.setCharacterAttributes(startPosition, endPosition - startPosition, underline, false);
        startPosition = endPosition;
        }
        }

        }
