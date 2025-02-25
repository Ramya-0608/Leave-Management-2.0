package staff;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import database.DBConnect;

public class staffdashboard {
    String staffid;
    String name;

    // Method to check if there are new requests for the staff member
    private static boolean hasnewrequests(String sid) {
        boolean hasrequests = false;
        try {
            Connection cn = DBConnect.getConnection();
            String q = "SELECT count(*) FROM requests WHERE staff_id=? AND is_viewed=FALSE";
            PreparedStatement pt = cn.prepareStatement(q);
            pt.setString(1, sid);
            ResultSet rs = pt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                hasrequests = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasrequests;
    }

    staffdashboard(String name, String id) {
        this.staffid = id;
        this.name=name;

        // Create JFrame for the main dashboard
        JFrame frame2 = new JFrame("Staff Dashboard");
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(800, 800);

        // Set layout manager (use BorderLayout)
        frame2.setLayout(new BorderLayout());

        // Create a panel for the side menu (using BoxLayout for vertical arrangement)
        JPanel sideMenuPanel = new JPanel();
        sideMenuPanel.setLayout(new BoxLayout(sideMenuPanel, BoxLayout.Y_AXIS));  // Arrange components vertically
        sideMenuPanel.setBackground(Color.white);  // Light blue background for the side menu

        // Create menu items with uniform size
        JButton showRequestsMenu = createMenuButton("Show New Requests");
        JButton showAllRequestsMenu = createMenuButton("Show All Requests");
        JButton generateReportMenu = createMenuButton("Generate Report");
        JButton requestLeaveMenu = createMenuButton("Request Leave");
        JButton requestStatusMenu = createMenuButton("Request Status");
        JButton logoutMenu = createMenuButton("Log Out");
        
        
        showRequestsMenu.addActionListener(e->{
        	new showRequests(id);
        	frame2.dispose();
        });
        
        showAllRequestsMenu.addActionListener(e->{
        	new showAllRequests(id);
        	frame2.dispose();
        });
        
        generateReportMenu.addActionListener(e->{
        	new report_generate(id);
        
        });
        
        logoutMenu.addActionListener(e->{
        	frame2.dispose();
        	new dashboard();
        	
        });
        
        requestStatusMenu.addActionListener(e->{
        	new showRequestStatus(id,this.name);
        	frame2.dispose();
        });
        
        requestLeaveMenu.addActionListener(e->{
        	new staffLeaveRequestForm(this.name,staffid);
        	frame2.dispose();
        });
        

        // Add buttons to the panel (they will be arranged vertically due to BoxLayout)
        sideMenuPanel.add(showRequestsMenu);
        sideMenuPanel.add(Box.createVerticalStrut(20));
        sideMenuPanel.add(showAllRequestsMenu);
        sideMenuPanel.add(Box.createVerticalStrut(20));
        sideMenuPanel.add(generateReportMenu);
        sideMenuPanel.add(Box.createVerticalStrut(20));
        sideMenuPanel.add(requestLeaveMenu);
        sideMenuPanel.add(Box.createVerticalStrut(20));
        sideMenuPanel.add(requestStatusMenu);
        sideMenuPanel.add(Box.createVerticalStrut(20));
        sideMenuPanel.add(logoutMenu);

        // Add the side menu panel to the left side of the frame
        frame2.add(sideMenuPanel, BorderLayout.WEST);

        // Create a panel to hold the staff details box (name and ID with icon)
        JPanel staffDetailsPanel = createStaffDetailsPanel(name, id);

        // Add the staff details panel to the center of the frame
        frame2.add(staffDetailsPanel, BorderLayout.CENTER);

        // Call the checkRequests method to see if there are new requests
        checkRequests();

        // Make the frame visible
        frame2.setVisible(true);
    }

    // Helper method to create the staff details box (name, ID, and icon)
    private JPanel createStaffDetailsPanel(String name, String id) {
        JPanel staffDetailsPanel = new JPanel();
        staffDetailsPanel.setLayout(new BoxLayout(staffDetailsPanel, BoxLayout.Y_AXIS));  // Arrange components in a column
        staffDetailsPanel.setBackground(new Color(240, 240, 240));  // Light gray background
        staffDetailsPanel.setPreferredSize(new Dimension(300, 200));  // Identity card-like size
        staffDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.black, 2),  // Darker border
            BorderFactory.createEmptyBorder(20, 20, 20, 20)  // Padding inside the panel
        ));

       

        // Create labels for the name and ID
        JLabel nameLabel = new JLabel("Name: " + name);
        JLabel idLabel = new JLabel("Staff ID: " + id);

        // Add icon and labels to the staff details panel
        
        staffDetailsPanel.add(Box.createVerticalStrut(10));
        staffDetailsPanel.add(nameLabel);
        staffDetailsPanel.add(Box.createVerticalStrut(5));
        staffDetailsPanel.add(idLabel);

        return staffDetailsPanel;
    }

    // Helper method to create styled menu buttons with uniform width and height
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 100));  // Uniform button size (width x height)
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(135, 230, 250)); // Light sky blue color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Adds padding around text

        // Change the background color when hovered over
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Cornflower blue when hovered
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(135, 206, 250)); // Original light blue color
            }
        });

        return button;
    }

    // Method to check if there are new requests for the staff member
    private void checkRequests() {
        boolean newrequests = hasnewrequests(staffid);
        if (newrequests) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "You have new requests", "New requests", JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

   
}
