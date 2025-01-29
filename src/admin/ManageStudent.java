package admin;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import database.DBConnect;

public class ManageStudent extends JFrame {

    JPanel buttonPanel, contentPanel;
    JButton addButton, updateButton, deleteButton, viewButton;
    JTable studentTable;
    DefaultTableModel tableModel;
    JComboBox<String> staffNameField,updateStaffNameField;
    ArrayList<String> stafflist=new ArrayList<>();

    public ManageStudent() {
        // Frame setup
        setTitle("Manage Students");
        setSize(900, 600);
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
        tableModel = new DefaultTableModel(new Object[] { "Student ID", "Student Name", "Staff Id", "No.of leaves" }, 0);
        studentTable = new JTable(tableModel);
        viewPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Add Panel
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
        JLabel studentIdLabel = new JLabel("Enter Student ID:");
        JTextField studentIdField = new JTextField();
        JLabel studentNameLabel = new JLabel("Enter Student Name:");
        JTextField studentNameField = new JTextField();
        JLabel staffnameLable = new JLabel("Enter Staff Name:");
        staffnamelist();
        staffNameField = new JComboBox<>(new Vector<>(stafflist));

        JButton addSubmitButton = new JButton("Add Student");

        studentIdField.setMaximumSize(new Dimension(300, 30));
        studentNameField.setMaximumSize(new Dimension(300, 30));
        staffNameField.setMaximumSize(new Dimension(300, 30));
        
        studentIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        studentIdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        studentNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        studentNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        staffnameLable.setAlignmentX(Component.CENTER_ALIGNMENT);
       staffNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        addSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addPanel.add(Box.createVerticalGlue());
        addPanel.add(studentIdLabel);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(studentIdField);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(studentNameLabel);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(studentNameField);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(staffnameLable);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(staffNameField);
        addPanel.add(Box.createVerticalStrut(20));
        addPanel.add(addSubmitButton);
        addPanel.add(Box.createVerticalGlue());

        // Delete Panel
        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
        JLabel deleteLabel = new JLabel("Enter Student ID to Delete:");
        JTextField deleteField = new JTextField();
        JButton deleteSubmitButton = new JButton("Delete Student");

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
        JLabel updateIdLabel = new JLabel("Enter Student ID:");
        JTextField updateIdField = new JTextField();
        JLabel updateNameLabel = new JLabel("Enter New Student Name:");
        JTextField updateNameField = new JTextField();
        JLabel updatestaffnameLable = new JLabel("Enter New Staffid:");
        updateStaffNameField=new JComboBox<>(new Vector<>(stafflist));
        JButton updateSubmitButton = new JButton("Update Student");

        updateIdField.setMaximumSize(new Dimension(300, 30));
        updateNameField.setMaximumSize(new Dimension(300, 30));
        updateStaffNameField.setMaximumSize(new Dimension(300, 30));
        updateIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateIdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updatestaffnameLable.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateStaffNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        updatePanel.add(updatestaffnameLable);
        updatePanel.add(Box.createVerticalStrut(10));
        updatePanel.add(updateStaffNameField);
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
            loadStudents();
            cardLayout.show(contentPanel, "View");
        });
        deleteButton.addActionListener(e -> cardLayout.show(contentPanel, "Delete"));
        updateButton.addActionListener(e -> cardLayout.show(contentPanel, "Update"));

        addSubmitButton.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            String studentName = studentNameField.getText().trim();
            String staffid =(String) staffNameField.getSelectedItem();

            if (addStudent(studentId, studentName, staffid)) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                studentIdField.setText("");
                studentNameField.setText("");
               // classField.setText("");
                loadStudents();
                cardLayout.show(contentPanel, "View");
            }
        });

        deleteSubmitButton.addActionListener(e -> {
            String studentId = deleteField.getText().trim();
            if (deleteStudent(studentId)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                deleteField.setText("");
                loadStudents();
                cardLayout.show(contentPanel, "View");
            }
        });

        updateSubmitButton.addActionListener(e -> {
            String studentId = updateIdField.getText().trim();
            String studentName = updateNameField.getText().trim();
            String staffname =updateStaffNameField.getSelectedItem().toString();
            if (updateStudent(studentId, studentName, staffname)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                updateIdField.setText("");
                updateNameField.setText("");
               // updateClassField.setText("");
                loadStudents();
                cardLayout.show(contentPanel, "View");
            }
        });

        setVisible(true);
    }

    private void staffnamelist() {
        // SQL Query to fetch names from the staff table
        String query = "SELECT name FROM staff";

        // Use try-with-resources for auto-closing connections
        try (Connection con = DBConnect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Initialize or ensure `stafflist` exists
            if (stafflist == null) {
                stafflist = new ArrayList<>();
            }

            // Loop through the result set and add each name to the list
            while (rs.next()) {
                stafflist.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            // Print the exception stack trace for debugging
            e.printStackTrace();
        }
    }


	private void loadStudents() {
        tableModel.setRowCount(0); // Clear existing rows
        String query = "SELECT * FROM students";

        try (//Connection con = DBConnect.getConnection();
        		Connection con=DBConnect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tableModel.addRow(new Object[] {
                    rs.getString("stud_id"),
                    rs.getString("name"),
                    rs.getString("staff_id"),
                    rs.getString("no_of_leave_days")
                    
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean addStudent(String studentId, String studentName, String staffid)
    {
    	String staff_id = null;
    	String staff_name=staffNameField.getSelectedItem().toString();
    	String query1="SELECT staff_id FROM staff WHERE name=?";
    	try (Connection con = DBConnect.getConnection();
    			PreparedStatement pstmt = con.prepareStatement(query1))
   	 {

           // Set the value for the WHERE condition
           pstmt.setString(1, staff_name);
           try (ResultSet rs = pstmt.executeQuery()) {
               // Check if a result exists
               if (rs.next()) {
                   // Retrieve the value of the `name` column
                   staff_id = rs.getString("staff_id");
               }
           }
    	}
    	catch(SQLException e)
    	{e.printStackTrace();}
        String query = "INSERT INTO students(stud_id, name, staff_id) VALUES(?, ?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, studentName);
            pstmt.setString(3, staff_id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean deleteStudent(String studentId) {
        String query = "DELETE FROM students WHERE stud_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean updateStudent(String studentId, String studentName, String staff_name) {
    	String staff_id = null;
    	String query1="SELECT staff_id FROM staff WHERE name=?";
    	 try (Connection con = DBConnect.getConnection();
    			PreparedStatement pstmt = con.prepareStatement(query1))
    	 {

            // Set the value for the WHERE condition
            pstmt.setString(1, staff_name);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                // Check if a result exists
                if (rs.next()) {
                    // Retrieve the value of the `name` column
                    staff_id = rs.getString("staff_id");
                }
            }
    	 }
    	catch(SQLException e)
    	{e.printStackTrace();}
        String query = "UPDATE students SET name = ?, staff_id = ? WHERE stud_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, studentName);
            pstmt.setString(2, staff_id);
            pstmt.setString(3, studentId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

//    public static void main(String[] args) {
//        new ManageStudent();
//    }
}
