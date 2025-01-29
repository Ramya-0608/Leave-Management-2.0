package staff;

import javax.swing.*;
import database.DBConnect;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;

 class studentpanel extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField username;
    private JPasswordField password;
    private JButton loginBtn;
    private JLabel label1;
    private JLabel label2;
    private JLabel studentInfoLabel;

     studentpanel() {
        setTitle("Login Task");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        

        // Labels
        label1 = new JLabel("Username");
        label2 = new JLabel("Password");
        label1.setBounds(150, 40, 150, 30);
        label2.setBounds(150, 80, 150, 30);
        add(label1);
        add(label2);

        // Text fields
        username = new JTextField();
        password = new JPasswordField();
        username.setBounds(320, 40, 150, 30);
        password.setBounds(320, 80, 150, 30);
        add(username);
        add(password);

        // Button
        loginBtn = new JButton("Login");
        loginBtn.setBounds(300, 300, 100, 30);
        loginBtn.addActionListener(this);
        add(loginBtn);

        // Label to display student name and ID
        studentInfoLabel = new JLabel("");
        studentInfoLabel.setBounds(150, 350, 500, 30);
        add(studentInfoLabel);

        setSize(800, 800);
        setResizable(false);
        setVisible(true);
    }

    // Login button functionality
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginBtn) {
            String username1 = username.getText().trim();
            String password1 = new String(password.getPassword()).trim(); // Convert char array to string
            
            // Validate username and password against the database
            if (validateLogin(username1, password1)) {
                setVisible(false);  // Hide the login frame
                JOptionPane.showMessageDialog(this, "Login Successful!");

                // Display student details (ID and Name)
                displayStudentDetails(password1);

                // Open Leave Request Form after successful login
                new LeaveRequestForm(password1);  // Pass the student ID to the leave request form
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Validate login credentials with database
    private boolean validateLogin(String username, String password) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Connect to MySQL Database
            connection = DBConnect.getConnection();

            // SQL query to find student with the given username and password
            String query = "SELECT * FROM students WHERE stud_id = ? AND name = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(2, username);
            stmt.setString(1, password);  // In production, make sure passwords are hashed!

            // Execute the query and check if the user exists
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true; // User exists
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;  // User does not exist or incorrect password
    }

    // Display student details (ID and Name) after successful login
    private void displayStudentDetails(String password) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Connect to MySQL Database
            connection = DBConnect.getConnection();

            // SQL query to fetch student details by username (stud_id)
            String query = "SELECT stud_id, name, no_of_leave_days FROM students WHERE stud_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, password);

            // Execute the query and fetch the student details
            rs = stmt.executeQuery();
            if (rs.next()) {
                String studentId = rs.getString("stud_id");
                String studentName = rs.getString("name");
                int noOfLeaveDays = rs.getInt("no_of_leave_days");

                // Update the label to display student info
                studentInfoLabel.setText("Student ID: " + studentId + " | Name: " + studentName + " | Leave Days: " + noOfLeaveDays);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    
}

class LeaveRequestForm extends JFrame {
    private JTextField studIdField, studNameField, leaveDateField;
    private JCheckBox sickCheckBox, personalCheckBox, othersCheckBox;
    private JTextField otherReasonField;
    private JButton submitButton;
    private String studentId, studentName;
    private String staffId;

    public LeaveRequestForm(String studentId) {
        this.studentId = studentId;

        setTitle("Leave Request Form");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the text fields
        studIdField = new JTextField();
        studNameField = new JTextField();
        leaveDateField = new JTextField();
        studIdField.setEditable(false);
        studNameField.setEditable(false);

        // Set today's date as default
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(new Date());
        leaveDateField.setText(todayDate);

        // Set student details (retrieve student name from DB)
        getStudentDetails(studentId);

        // Initialize the checkboxes
        sickCheckBox = new JCheckBox("Sick");
        personalCheckBox = new JCheckBox("Personal");
        othersCheckBox = new JCheckBox("Others");

        otherReasonField = new JTextField();
        otherReasonField.setEnabled(false); // Disable initially

        submitButton = new JButton("Submit Leave Request");

        // Create layout for the form
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));
        panel.add(new JLabel("Student ID:"));
        panel.add(studIdField);
  
        panel.add(new JLabel("Student Name:"));
        panel.add(studNameField);
        panel.add(new JLabel("Leave Date (YYYY-MM-DD):"));
        panel.add(leaveDateField);
        panel.add(sickCheckBox);
        panel.add(personalCheckBox);
        panel.add(othersCheckBox);
        panel.add(new JLabel("Other Reason (if selected):"));
        panel.add(otherReasonField);
        panel.add(submitButton);

        add(panel);

        // Enable/Disable otherReasonField based on "Others" checkbox
        othersCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                otherReasonField.setEnabled(othersCheckBox.isSelected());
            }
        });

        // Handle form submission
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String reason = getSelectedReason();
                String name=studNameField.getText();
                String otherReason = otherReasonField.getText();
                String leaveDate = leaveDateField.getText();

                // Simple date validation
                if (!isValidDate(leaveDate)) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid leave date in the format YYYY-MM-DD.");
                    return;
                }

                if (othersCheckBox.isSelected() && otherReason.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a reason for 'Others'.");
                    return;
                }

                if (submitLeaveRequest(name,studentId, leaveDate, reason,otherReason)) {
                    JOptionPane.showMessageDialog(null, "Leave request submitted successfully!");
                    // Update leave days after submission
                    updateLeaveDays(studentId);
                } else {
                    JOptionPane.showMessageDialog(null, "Error submitting leave request.");
                }
            }
        });

        setVisible(true);
    }

    private void getStudentDetails(String studentId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Connect to MySQL Database
            connection = DBConnect.getConnection();

            // SQL query to fetch student details by username (stud_id)
            String query = "SELECT stud_id, name, staff_id FROM students WHERE stud_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, studentId);

            // Execute the query and fetch the student details
            rs = stmt.executeQuery();
            if (rs.next()) {
                String studId = rs.getString("stud_id");
                String studName = rs.getString("name");
                staffId = rs.getString("staff_id");  // Get staff_id

                // Set the fields with student data
                studIdField.setText(studId);
                studNameField.setText(studName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getSelectedReason() {
        if (sickCheckBox.isSelected()) {
            return "Sick";
        } else if (personalCheckBox.isSelected()) {
            return "Personal";
        } else if (othersCheckBox.isSelected()) {
            return "Others";
        }
        return "";
    }

    // Validate the date string (YYYY-MM-DD format)
    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean submitLeaveRequest(String name,String studentId, String leaveDate, String reason,String otherReason) {
        Connection connection = null;
        PreparedStatement stmt = null;
        boolean success = false;
        try {
            // Connect to MySQL Database
            connection = DBConnect.getConnection();

            // Convert the leaveDate string to java.sql.Date
            java.sql.Date sqlDate = convertStringToSqlDate(leaveDate);
            if (sqlDate == null) {
                JOptionPane.showMessageDialog(null, "Invalid leave date format. Please use YYYY-MM-DD.");
                return false;
            }

            // Prepare SQL query to insert leave request into the "requestsa" table
            String query = "INSERT INTO requests (stu_id, staff_id, name,leave_date, reason) VALUES (?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, studentId);
            stmt.setString(2, staffId); 
            stmt.setString(3,name);// Use the fetched staff_id
            stmt.setDate(4, sqlDate);  // Use converted java.sql.Date

            // Check if 'Others' is selected and insert the otherReason
            if (reason.equals("Others") && !otherReason.isEmpty()) {
                stmt.setString(5, otherReason); // Insert the custom reason if available
            } else {
                stmt.setString(5, reason); // Set as NULL if no custom reason is provided
            }

             // Store the general reason (Sick, Personal, or Others)

            // Execute update and check if successful
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
                System.out.println("Leave request inserted successfully!");

                // After successful insertion, update the leave days in the student table
               // updateLeaveDays(studentId);  // Call the method to update the leave days here
            } else {
                System.out.println("No rows affected. Something went wrong with the query.");
            }

        } catch (SQLException ex) {
            // Log SQL exceptions for better debugging
            System.err.println("SQL Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        return success;
    }

    // Helper method to convert string to java.sql.Date
    private java.sql.Date convertStringToSqlDate(String dateStr) {
        try {
            // SimpleDateFormat to parse the date string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(dateStr);
            
            // Convert to java.sql.Date
            return new java.sql.Date(utilDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to update the number of leave days for the student
 // Method to update the number of leave days for the student
  private void updateLeaveDays(String studentId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Connect to MySQL Database
            connection = DBConnect.getConnection();

            // Query to get the current number of leave days
            String query = "SELECT no_of_leave_days FROM students WHERE stud_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, studentId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int currentLeaveDays = rs.getInt("no_of_leave_days");
                int updatedLeaveDays = currentLeaveDays + 1;  // Increment leave days by 1

                // Update the leave days in the database
                String updateQuery = "UPDATE students SET no_of_leave_days = ? WHERE stud_id = ?";
                stmt = connection.prepareStatement(updateQuery);
                stmt.setInt(1, updatedLeaveDays);
                stmt.setString(2, studentId);

                // Execute the update query
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Leave days updated successfully!");
                } else {
                    System.out.println("Failed to update leave days.");
                }
            } else {
                System.out.println("Student not found for ID: " + studentId);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
