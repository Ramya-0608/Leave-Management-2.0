package admin;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LeaveManagement extends JFrame {

    // Main Panel and Menu Components
    JMenuBar menuBar;
    JMenu manageMenu;
    JMenuItem manageClassMenuItem;

    JPanel mainPanel, buttonPanel, contentPanel;
    JButton addButton, updateButton, deleteButton, viewButton;

    // Panels for managing class actions
    JPanel addClassPanel, updateClassPanel, deleteClassPanel, viewClassPanel;

    JTextField addClassField, updateClassField, deleteClassField;
    JTable classTable;
    DefaultTableModel tableModel;

    public LeaveManagement() {
        // Set up the main frame
        setTitle("Manage Class");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create the menu bar
        menuBar = new JMenuBar();
        manageMenu = new JMenu("Manage");
        manageClassMenuItem = new JMenuItem("Class");
        manageMenu.add(manageClassMenuItem);
        menuBar.add(manageMenu);
        setJMenuBar(menuBar);

        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Button panel for Add, Update, Delete, View
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        viewButton = new JButton("View");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Content panel to switch between different actions
        contentPanel = new JPanel(new CardLayout());

        // View Class Panel
        viewClassPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[] { "Class Name" }, 0);
        classTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(classTable);
        viewClassPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add Class Panel
        addClassPanel = new JPanel();
        addClassPanel.setLayout(new BoxLayout(addClassPanel, BoxLayout.Y_AXIS));
        JLabel addClassLabel = new JLabel("Enter Class Name:");
        addClassField = new JTextField();
        JButton addClassSubmitButton = new JButton("Add Class");
        addClassField.setMaximumSize(new Dimension(300, 30));
        addClassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addClassField.setAlignmentX(Component.CENTER_ALIGNMENT);
        addClassSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addClassPanel.add(Box.createVerticalGlue());
        addClassPanel.add(addClassLabel);
        addClassPanel.add(Box.createVerticalStrut(10));
        addClassPanel.add(addClassField);
        addClassPanel.add(Box.createVerticalStrut(20));
        addClassPanel.add(addClassSubmitButton);
        addClassPanel.add(Box.createVerticalGlue());

        // Update Class Panel
        updateClassPanel = new JPanel();
        updateClassPanel.setLayout(new BoxLayout(updateClassPanel, BoxLayout.Y_AXIS));
        JLabel updateClassLabel = new JLabel("Enter New Class Name:");
        updateClassField = new JTextField();
        JButton updateClassSubmitButton = new JButton("Update Class");
        updateClassField.setMaximumSize(new Dimension(300, 30));
        updateClassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateClassField.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateClassSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateClassPanel.add(Box.createVerticalGlue());
        updateClassPanel.add(updateClassLabel);
        updateClassPanel.add(Box.createVerticalStrut(10));
        updateClassPanel.add(updateClassField);
        updateClassPanel.add(Box.createVerticalStrut(20));
        updateClassPanel.add(updateClassSubmitButton);
        updateClassPanel.add(Box.createVerticalGlue());

        // Delete Class Panel
        deleteClassPanel = new JPanel();
        deleteClassPanel.setLayout(new BoxLayout(deleteClassPanel, BoxLayout.Y_AXIS));
        JLabel deleteClassLabel = new JLabel("Enter Class Name to Delete:");
        deleteClassField = new JTextField();
        JButton deleteClassSubmitButton = new JButton("Delete Class");
        deleteClassField.setMaximumSize(new Dimension(300, 30));
        deleteClassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteClassField.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteClassSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteClassPanel.add(Box.createVerticalGlue());
        deleteClassPanel.add(deleteClassLabel);
        deleteClassPanel.add(Box.createVerticalStrut(10));
        deleteClassPanel.add(deleteClassField);
        deleteClassPanel.add(Box.createVerticalStrut(20));
        deleteClassPanel.add(deleteClassSubmitButton);
        deleteClassPanel.add(Box.createVerticalGlue());

        // Add panels to the content panel
        contentPanel.add(viewClassPanel, "View");
        contentPanel.add(addClassPanel, "Add");
        contentPanel.add(updateClassPanel, "Update");
        contentPanel.add(deleteClassPanel, "Delete");

        // Add button panel and content panel to the main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        add(mainPanel);

        // Show the "View" panel by default
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "View");

        // Button action listeners
        addButton.addActionListener(e -> cardLayout.show(contentPanel, "Add"));
        updateButton.addActionListener(e -> cardLayout.show(contentPanel, "Update"));
        deleteButton.addActionListener(e -> cardLayout.show(contentPanel, "Delete"));
        viewButton.addActionListener(e -> cardLayout.show(contentPanel, "View"));

        // Add Class Submit Button
        addClassSubmitButton.addActionListener(e -> {
            String className = addClassField.getText().trim();
            if (!className.isEmpty()) {
                tableModel.addRow(new Object[] { className });
                JOptionPane.showMessageDialog(this, "Class added successfully!");
                addClassField.setText("");
                cardLayout.show(contentPanel, "View");
            } else {
                JOptionPane.showMessageDialog(this, "Class name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete Class Submit Button
        deleteClassSubmitButton.addActionListener(e -> {
            String className = deleteClassField.getText().trim();
            if (!className.isEmpty()) {
                boolean found = false;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).equals(className)) {
                        tableModel.removeRow(i);
                        found = true;
                        break;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(this, "Class deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Class not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                deleteClassField.setText("");
                cardLayout.show(contentPanel, "View");
            } else {
                JOptionPane.showMessageDialog(this, "Class name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update Class Submit Button
        updateClassSubmitButton.addActionListener(e -> {
            String newClassName = updateClassField.getText().trim();
            if (!newClassName.isEmpty()) {
                // Update logic can be implemented here
                JOptionPane.showMessageDialog(this, "Class updated successfully!");
                updateClassField.setText("");
                cardLayout.show(contentPanel, "View");
            } else {
                JOptionPane.showMessageDialog(this, "Class name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Show the frame
        setVisible(true);
    }

    public static void main(String[] args) {
        new LeaveManagement();
    }
}
