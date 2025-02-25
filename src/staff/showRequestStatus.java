package staff;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import database.DBConnect;

public class showRequestStatus extends JFrame {
    private String staffid;

    public showRequestStatus(String staffid,String name) {
        this.staffid = staffid;
        setTitle("Request Status");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close this frame when disposed

        // Set the layout manager to BorderLayout
        setLayout(new BorderLayout());

        // Create and add the back button (top-right corner)
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(80, 30));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.addActionListener(e -> {
            dispose();
            new staffdashboard(staffid,name);// Close this frame and return to the previous screen
        });
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.NORTH);

        // Fetch and display the requests table
        displayRequestsTable();

        setVisible(true);
    }

    // Method to fetch requests from the database and display them in a table
    private void displayRequestsTable() {
        // Query the database to fetch all requests for this staff member
        try (Connection cn = DBConnect.getConnection()) {
            String query = "SELECT leave_date, reason, is_viewed, response FROM staff_requests WHERE staff_id=?";
            PreparedStatement pt = cn.prepareStatement(query);
            pt.setString(1, staffid);
            ResultSet rs = pt.executeQuery();

    

            // Create a table model with columns
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("S.No");
            model.addColumn("Leave Date");
            model.addColumn("Reason");
            model.addColumn("Is Viewed");
            model.addColumn("Response");

            // Populate the table model with data from the database
            int serialNumber = 1;
            while (rs.next()) {
                String leaveDate = rs.getString("leave_date");
                String reason = rs.getString("reason");
                boolean isViewed = rs.getBoolean("is_viewed");
                String response = rs.getString("response");

                // If not viewed, show "Pending" in the response field
                if (!isViewed) {
                    response = "Pending";
                }

                // Add a row with the serial number and other details
                model.addRow(new Object[]{serialNumber++, leaveDate, reason, isViewed ? "Yes" : "No", response});
            }

            // If no data found, show a message
            if (model.getRowCount() == 0) {
               
                JOptionPane.showMessageDialog(this, "No requests found for this staff.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

            // Create the table and add it to a scroll pane
            JTable table = new JTable(model);
            table.setRowHeight(30);  // Set row height for better readability

            // Create a scroll pane to make the table scrollable
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // Ensure the table fills the available space in the frame
            revalidate();  // Ensure layout updates correctly
            repaint();     // Repaint the frame to update the display

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading request status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
   
}
