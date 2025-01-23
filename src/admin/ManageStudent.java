package admin;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import database.DBConnect;

public class ManageStudent extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel buttonPanel, contentPanel;
    JButton addButton, updateButton, deleteButton, viewButton;
    JTable studentTable;
    DefaultTableModel tableModel;

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
        tableModel = new DefaultTableModel(new Object[] { "Student ID", "Student Name", "Class", "Staff Name" }, 0);
        studentTable = new JTable(tableModel);
        viewPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Add Panel
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
        JLabel studentIdLabel = new JLabel("Enter Student ID:");
        JTextField studentIdField = new JTextField();
        JLabel studentNameLabel = new JLabel("Enter Student Name:");
        JTextField studentNameField = new JTextField();
        JLabel classLabel = new JLabel("Enter Staff:");
        JComboBox<String> classField = new JComboBox<>();
        JButton addSubmitButton = new JButton("Add Student");

        studentIdField.setMaximumSize(new Dimension(300, 30));
        studentNameField.setMaximumSize(new Dimension(300, 30));
        classField.setMaximumSize(new Dimension(300, 30));
        
        studentIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        studentIdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        studentNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        studentNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        classField.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        addPanel.add(classLabel);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(classField);
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
        JLabel updateClassLabel = new JLabel("Enter New Class:");
        JComboBox<String> updateClassField = new JComboBox<>();
        JButton updateSubmitButton = new JButton("Update Student");

        updateIdField.setMaximumSize(new Dimension(300, 30));
        updateNameField.setMaximumSize(new Dimension(300, 30));
        updateClassField.setMaximumSize(new Dimension(300, 30));
        updateIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateIdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
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
            loadStudents();
            cardLayout.show(contentPanel, "View");
        });
        deleteButton.addActionListener(e -> cardLayout.show(contentPanel, "Delete"));
        updateButton.addActionListener(e -> cardLayout.show(contentPanel, "Update"));

        addSubmitButton.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            String studentName = studentNameField.getText().trim();
            String className = (String)classField.getSelectedItem();

            if (addStudent(studentId, studentName, className)) {
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
            String className =(String) updateClassField.getSelectedItem();
            if (updateStudent(studentId, studentName, className)) {
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

    private void loadStudents() {
        tableModel.setRowCount(0); // Clear existing rows
        String query = "SELECT s.studentid, s.studentname, s.class, st.staffname " +
                       "FROM students s " +
                       "JOIN staff st ON s.class = st.class";

        try (Connection con = DBConnect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tableModel.addRow(new Object[] {
                    rs.getString("studentid"),
                    rs.getString("studentname"),
                    rs.getString("class"),
                    rs.getString("staffname")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean addStudent(String studentId, String studentName, String staffId) {
        String query = "INSERT INTO students(studentid, studentname, staffId) VALUES(?, ?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, studentName);
            pstmt.setString(3, staffId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean deleteStudent(String studentId) {
        String query = "DELETE FROM students WHERE studentid = ?";
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

    private boolean updateStudent(String studentId, String studentName, String className) {
        String query = "UPDATE students SET studentname = ?, class = ? WHERE studentid = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, studentName);
            pstmt.setString(2, className);
            pstmt.setString(3, studentId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        new ManageStudent();
    }
}
