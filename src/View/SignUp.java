package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;

public class SignUp extends JFrame implements ActionListener {
    private JPanel p1;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private JTextField uName;
    private JPasswordField pass;
    private JPasswordField cPass;
    private JButton signIn;
    private JButton backToLogin; // Added button to go back to login page
    private static Connection connection;

    //---------------------------------------------------------------------------------------------------------
    public void createSignUpPage(final Connection conn) {
        this.connection = conn;
        l1 = new JLabel("Create Username: ");
        l2 = new JLabel("Create Password: ");
        l3 = new JLabel("Confirm Password: ");
        uName = new JTextField(20);
        pass = new JPasswordField(20);
        cPass = new JPasswordField(20);
        signIn = new JButton("SignUp");
        signIn.addActionListener(this);
        signIn.setBackground(new Color(39, 66, 122));

        // Added backToLogin button
        backToLogin = new JButton("Login");
        backToLogin.addActionListener(this);
        backToLogin.setBackground(new Color(39, 66, 122));
        backToLogin.setForeground(Color.WHITE);
        signIn.setForeground(Color.WHITE);

        p1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 20, 0); // Add spacing between components

        p1.add(l1, gbc);
        p1.add(uName, gbc);
        p1.add(l2, gbc);
        p1.add(pass, gbc);
        p1.add(l3, gbc);
        p1.add(cPass, gbc);
        p1.add(signIn, gbc);
        p1.add(backToLogin, gbc); // Added backToLogin button

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
        smartScriptPanel.setBackground(new Color(39, 66, 122));
        smartScriptPanel.add(smartScriptLabel);
        rightPanel.add(smartScriptPanel, BorderLayout.CENTER);

        // Create a JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, rightPanel);
        splitPane.setDividerLocation(300);

        // Frame
        setLayout(new GridLayout(1, 1));
        add(splitPane);
        setTitle("SignUp Page");
        setSize(600, 600);
        addWindowListener(new MyWindowListener());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //---------------------------------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SignUp")) {
            try {
                saveData();
                Login loginPage = new Login();
                loginPage.createLoginPage(connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getActionCommand().equals("Login")) {
            dispose();
            Login loginPage = new Login();
            loginPage.createLoginPage(connection);
        }
    }

    //---------------------------------------------------------------------------------------------------------
    class MyWindowListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            dispose();
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }

    //---------------------------------------------------------------------------------------------------------
    private void saveData() throws SQLException {
        String query = "select username from users where username = '" + uName.getText() + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next()) {
            String st1 = new String(pass.getPassword());
            String st2 = new String(cPass.getPassword());
            if (uName.getText().equals("") || st1.equals("") || st2.equals("")) {
                JOptionPane.showMessageDialog(this, "Fields empty!");
                return;
            }
            if (st1.equals(st2)) {
                query = "insert into users values (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, uName.getText());
                    preparedStatement.setString(2, st1);
                    preparedStatement.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "SignUp Successful!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Passwords not matched!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "This username already exists!", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    //---------------------------------------------------------------------------------------------------------
}
