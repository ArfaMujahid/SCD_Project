package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//Class to show the list of documents that logged in user has made
// and show option to create new document.
public class ListDocuments extends JFrame {
    private JButton createBtn;
    private JPanel panel;//implementation is remaining from initilization.

    public ListDocuments(){
        this.setLocationRelativeTo(null);
        this.createBtn = new JButton("Create Document");
        this.setSize(new Dimension(500, 500));

        this.addWindowListener(new WindowAdapter() { //to remove this frame on click cross button.
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        this.createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewDocument().display();
            }
        });
        this.add(this.createBtn);

    }

    public void displayList(){
        this.setVisible(true);
    }
}
