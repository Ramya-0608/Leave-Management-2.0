package admin;

import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import database.DBConnect;

public class ManageStaff extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel buttonPanel, contentPanel;
    JButton addButton, updateButton, deleteButton, viewButton;
    JTable staffTable;
    DefaultTableModel tableModel;
    ArrayList<String> classes = new ArrayList<>();
        
    public ManageStaff() {
        // Frame setup
        setTitle("Manage Staff");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        viewButton = new JButton("View");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Content panel
        contentPanel = new JPanel(new CardLayout());

        // View Panel
        JPanel viewPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"Staff Name", "Staff ID", "Class"}, 0);
        staffTable = new JTable(tableModel);
        viewPanel.add(new JScrollPane(staffTable), BorderLayout.CENTER);

        // Add Panel
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel("Enter Staff Name:");
        JTextField nameField = new JTextField();
        JLabel staffIdLabel = new JLabel("Enter Staff ID:");
        JTextField staffIdField = new JTextField();
        
        ArrayList<String> classList = new ArrayList<>();
        classList.add("select");
		try (Connection con = DBConnect.getConnection();
				PreparedStatement stmt = con.prepareStatement("Select * from classes")) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				classList.add(rs.getString("classname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Object[] ob = classList.toArray();
		
        JLabel classLabel = new JLabel("Enter Class:");
        JComboBox<Object> classField = new JComboBox<>(ob);
        JButton addSubmitButton = new JButton("Add Staff");
        nameField.setMaximumSize(new Dimension(300, 30));
        staffIdField.setMaximumSize(new Dimension(300, 30));
        classField.setMaximumSize(new Dimension(300, 30));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        staffIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        staffIdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        classField.setAlignmentX(Component.CENTER_ALIGNMENT);
        addSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addPanel.add(Box.createVerticalGlue());
        addPanel.add(nameLabel);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(nameField);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(staffIdLabel);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(staffIdField);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(classLabel);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(classField);
        addPanel.add(Box.createVerticalStrut(20));
        addPanel.add(addSubmitButton);
        addPanel.add(Box.createVerticalGlue());

        // Delete Panel
        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
        JLabel deleteLabel = new JLabel("Enter Staff ID to Delete:");
        JTextField deleteField = new JTextField();
        JButton deleteSubmitButton = new JButton("Delete Staff");
        deleteField.setMaximumSize(new Dimension(300, 30));
        deleteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteField.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deletePanel.add(Box.createVerticalGlue());
        deletePanel.add(deleteLabel);
        deletePanel.add(Box.createVerticalStrut(10));
        deletePanel.add(deleteField);
        deletePanel.add(Box.createVerticalStrut(20));
        deletePanel.add(deleteSubmitButton);
        deletePanel.add(Box.createVerticalGlue());

        // Update Panel
        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));
        JLabel updateIdLabel = new JLabel("Enter Staff ID:");
        JTextField updateIdField = new JTextField();
        JLabel updateNameLabel = new JLabel("Enter New Name:");
        JTextField updateNameField = new JTextField();		
        JLabel updateClassLabel = new JLabel("Enter New Class:");
        JComboBox<Object> updateClassField = new JComboBox<>(ob);
        JButton updateSubmitButton = new JButton("Update Staff");
        updateIdField.setMaximumSize(new Dimension(300, 30));
        updateNameField.setMaximumSize(new Dimension(300, 30));
        updateClassField.setMaximumSize(new Dimension(300, 30));
        updateIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateIdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateClassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateClassField.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updatePanel.add(Box.createVerticalGlue());
        updatePanel.add(updateIdLabel);
        updatePanel.add(Box.createVerticalStrut(10));
        updatePanel.add(updateIdField);
        updatePanel.add(Box.createVerticalStrut(10));
        updatePanel.add(updateNameLabel);
        updatePanel.add(Box.createVerticalStrut(10));
        updatePanel.add(updateNameField);
        updatePanel.add(Box.createVerticalStrut(10));
        updatePanel.add(updateClassLabel);
        updatePanel.add(Box.createVerticalStrut(10));
        updatePanel.add(updateClassField);
        updatePanel.add(Box.createVerticalStrut(20));
        updatePanel.add(updateSubmitButton);
        updatePanel.add(Box.createVerticalGlue());

        // Add panels to content panel
        contentPanel.add(viewPanel, "View");
        contentPanel.add(addPanel, "Add");
        contentPanel.add(deletePanel, "Delete");
        contentPanel.add(updatePanel, "Update");

        // Add components to frame
        add(buttonPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Show the "View" panel by default
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "View");

        // Button actions
        addButton.addActionListener(e -> cardLayout.show(contentPanel, "Add"));
        viewButton.addActionListener(e -> {
            loadStaff();
            cardLayout.show(contentPanel, "View");
        });

        deleteButton.addActionListener(e -> cardLayout.show(contentPanel, "Delete"));
        updateButton.addActionListener(e -> cardLayout.show(contentPanel, "Update"));

        addSubmitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String staffId = staffIdField.getText().trim();
            String staffClass = (String)classField.getSelectedItem();
            if(staffClass=="select") {
            	JOptionPane.showMessageDialog(null, "Please select class");
				return;
            }
            if (addStaff(name, staffId, staffClass)) {
                JOptionPane.showMessageDialog(this, "Staff added successfully!");
                nameField.setText("");
                staffIdField.setText("");
               // classField.setText("");
                loadStaff();
                cardLayout.show(contentPanel, "View");
            }
        });

        deleteSubmitButton.addActionListener(e -> {
            String staffId = deleteField.getText().trim();
            if (deleteStaff(staffId)) {
                JOptionPane.showMessageDialog(this, "Staff deleted successfully!");
                deleteField.setText("");
                loadStaff();
                cardLayout.show(contentPanel, "View");
            }
        });

        updateSubmitButton.addActionListener(e -> {
            String staffId = updateIdField.getText().trim();
            String newName = updateNameField.getText().trim();
            String newClass = (String) updateClassField.getSelectedItem();
            if(newClass=="select") {
            	JOptionPane.showMessageDialog(null, "Please select class to update");
				return;
            }
            if (updateStaff(staffId, newName, newClass)) {
                JOptionPane.showMessageDialog(this, "Staff updated successfully!");
                updateIdField.setText("");
                updateNameField.setText("");
                //updateClassField.setText("");
                loadStaff();
                cardLayout.show(contentPanel, "View");
            }
        });

        setVisible(true);
    }

    private void loadStaff() {
        tableModel.setRowCount(0);
        String query = "SELECT * FROM staff";
        try (Connection con = DBConnect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getString("staff_id"),
                    rs.getString("clas_id")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading staff: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean addStaff(String name, String staffId, String staffClass) {
        if (name.isEmpty() || staffId.isEmpty() || staffClass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String query = "INSERT INTO staff (name, staff_id, clas_id) VALUES (?, ?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, staffId);
            pstmt.setString(3, staffClass);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean deleteStaff(String staffId) {
        String query = "DELETE FROM staff WHERE staff_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, staffId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean updateStaff(String staffId, String newName, String newClass) {
        if (staffId.isEmpty() || newName.isEmpty() || newClass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String query = "UPDATE staff SET name = ?, clas_id = ? WHERE staff_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newClass);
            pstmt.setString(3, staffId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

//    public static void main(String[] args) {
//        new ManageStaff();
//    }
}
