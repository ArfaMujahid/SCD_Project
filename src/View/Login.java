package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends JFrame implements ActionListener {
    private JPanel p1;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private JTextField uName;
    private JPasswordField pass;
    private JButton login;
    private JButton signUp;
    private static Connection connection;
    private String name;

    //---------------------------------------------------------------------------------------------------------
    public void createLoginPage(final Connection conn) {
        connection = conn;
             l1 = new JLabel("Username: ");
            l2 = new JLabel("Password: ");
            l3 = new JLabel("Don't have an account?");
            uName = new JTextField(20);
            pass = new JPasswordField(20);

            // Set up the left panel with a GridBagLayout
            p1 = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(0, 0, 20, 0); // Add spacing between components

            p1.add(l1, gbc);
            p1.add(uName, gbc);
            p1.add(l2, gbc);
            p1.add(pass, gbc);
            p1.add(l3, gbc);

            // Create a button panel for login and sign up buttons
            JPanel buttonPanel = new JPanel(new FlowLayout());
            login = new JButton("Login");
            login.addActionListener(this);
            signUp = new JButton("SignUp");
            signUp.addActionListener(this);
            buttonPanel.add(login);
            buttonPanel.add(signUp);

            // Add the button panel to the left panel
            p1.add(buttonPanel, gbc);

            // Set background color for the entire content pane
            p1.setBackground(new Color(255, 240, 245)); // Lavender Blush

            // Create a right panel
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setBackground(Color.WHITE);
            JLabel smartScriptLabel = new JLabel("SmartScript");
            smartScriptLabel.setHorizontalAlignment(JLabel.CENTER);
            smartScriptLabel.setFont(new Font("Arial", Font.BOLD, 20));
            smartScriptLabel.setForeground(Color.WHITE);
            JPanel smartScriptPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 180));
            smartScriptPanel.setBackground(new Color(83, 135, 169));
            smartScriptPanel.add(smartScriptLabel);
            rightPanel.add(smartScriptPanel, BorderLayout.CENTER);

            // Create a JSplitPane
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, rightPanel);
            splitPane.setDividerLocation(200);

            // Frame
            setLayout(new GridLayout(1, 1));
            add(splitPane);
            setTitle("Login Page");
            setSize(500, 600);
             setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);


    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "SignUp") {
            SignUp signUp1 = new SignUp();
            signUp1.createSignUpPage(connection);
        }else if (e.getActionCommand().equals("Login")) {
            try {
               if(uName.getText().equals(null)||uName.getText().equals("")){
                   JOptionPane.showMessageDialog(this, "Fields empty!");
              return;
               }
                if(pass.getText().equals(null)||pass.getText().equals("")){
                    JOptionPane.showMessageDialog(this, "Fields empty!");
                    return;
                }
                if (checkLoginCredentials()==true) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    OptionsFrame optionsFrame = new OptionsFrame(connection, name);
                    optionsFrame.display();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect Username or Password!", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------
    private boolean checkLoginCredentials() throws SQLException {
        String query = "select * from users;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String user = resultSet.getString(1);
            String p = resultSet.getString(2);
            char[] passwordChars = pass.getPassword();
            String st = new String(passwordChars);
            if (user.equals(uName.getText()) && p.equals(st)) {
                name = uName.getText();
                return true;
            }
        }
        return false;
    }
    //---------------------------------------------------------------------------------------------------------
}
