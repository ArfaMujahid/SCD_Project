package View;

import Controller.OptionsFrameActionListener;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import javax.swing.border.LineBorder;

public class OptionsFrame extends JFrame {
    private Connection connection;
    private String userName;
    private JButton openDocuments;
    private JButton openFavouriteDocuments;
    private JPanel panel1;
    private JPanel panel2;
    private OptionsFrameActionListener optionsFrameActionListener;

    public OptionsFrame(Connection connection, final String userName) {
        this.connection = connection;
        this.userName = userName;


        this.optionsFrameActionListener = new OptionsFrameActionListener(this.connection, userName);
        this.openDocuments = createStyledButton("Open Documents", "Open Documents");
        this.openFavouriteDocuments = createStyledButton("Open Favourite Documents", "Open Favourite Documents");


        setLayout(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;


        JLabel smartScriptLabel = new JLabel("SmartScript", JLabel.CENTER);
        smartScriptLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24)); // Larger and in italics
        smartScriptLabel.setForeground(Color.WHITE); // White color
        gbcMain.insets = new Insets(20, 0, 10, 0); // Top margin for the label
        add(smartScriptLabel, gbcMain);


        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(20, 0, 0, 0); // Top margin for the buttons


        panel1 = createStyledPanel(openDocuments);
        panel2 = createStyledPanel(openFavouriteDocuments);
        add(panel1, gbcMain);
        gbcMain.gridy = 2;
        add(panel2, gbcMain);


        setSize(new Dimension(600, 400));
        getContentPane().setBackground(new Color(39, 66, 122));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JButton createStyledButton(String text, String actionCommand) {
        JButton button = new JButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(this.optionsFrameActionListener);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(128, 128, 128));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createStyledPanel(JButton button) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(39, 66, 122));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(button, gbc);
        return panel;
    }

    public void display() {
        setVisible(true);
    }
}
