package View;

import Controller.LoadFileActionListener;
import Model.DatabaseWork;
import Model.GetFileData;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.List;

public class FavouriteDocumentsList extends JFrame {
    private JButton createNewDocumentBtn;
    private JTable documentTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    private Connection connection;
    private String name;
    private LoadFileActionListener loadFileActionListener;
    private GetFileData getFileData; // Add this line

    public FavouriteDocumentsList(Connection conn, final String name) {
        getFileData = new GetFileData(); // Initialize GetFileData
        getFileData.setConnection(conn); // Set the connection for GetFileData
        DatabaseWork DB = new DatabaseWork(conn);
        int count = DB.getTotalDocuments(name);

        this.name = name;
        this.connection = conn;
        this.setLocationRelativeTo(null);

        // Create a table model and set column names
        String[] columnNames = {"Document Name"};
        tableModel = new DefaultTableModel(columnNames, 0);
        documentTable = new JTable(tableModel);

        // Set up the table with a scroll pane
        tableScrollPane = new JScrollPane(documentTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        createNewDocumentBtn = new JButton("Create Document");
        createNewDocumentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewDocument(FavouriteDocumentsList.this.connection, name).display("");
            }
        });
        createNewDocumentBtn.setBackground(new Color(128, 179, 255));
        buttonPanel.add(createNewDocumentBtn);

        // Set up the layout using BorderLayout
        this.setLayout(new BorderLayout());
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.PAGE_END);  // Use PAGE_END for the button panel

        if (count != 0) {
            loadFileActionListener = new LoadFileActionListener(this.connection, this.name);
            List<String> documentNames = DB.getFavouriteDocumentNames(this.name);

            for (String name2 : documentNames) {
                // Add each document name to the table model
                tableModel.addRow(new Object[]{name2});
            }

            documentTable.getSelectionModel().addListSelectionListener(e -> {
                int selectedRow = documentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String selectedDocumentName = (String) tableModel.getValueAt(selectedRow, 0);
                    loadFileActionListener.actionPerformed(new ActionEvent(documentTable, ActionEvent.ACTION_PERFORMED, selectedDocumentName));
                }
            });




        }


        this.setSize(new Dimension(500, 500));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    public void displayList() {
        this.setVisible(true);
    }

}
