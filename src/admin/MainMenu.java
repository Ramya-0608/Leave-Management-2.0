package admin;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import database.DBConnect;

public class MainMenu extends JFrame {
	private static boolean hasnewrequests() {
		 boolean hasrequests=false;
		  try {
			  Connection cn=DBConnect.getConnection();
			  String q="SELECT count(*) FROM staff_requests WHERE is_viewed=FALSE";
			  PreparedStatement pt=cn.prepareStatement(q);
			  ResultSet rs=pt.executeQuery();
			  if(rs.next()&&rs.getInt(1)>0) {
				  hasrequests=true;
           }
		  }catch(SQLException e) {
	                  e.printStackTrace();
          }
		  return hasrequests;
		  }

    public MainMenu() {
        // Frame setup
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        // Buttons for navigation
        JButton manageClassButton = new JButton("Manage Class");
        JButton manageStudentButton = new JButton("Manage Student");
        JButton manageStaffButton = new JButton("Manage Staff");
        JButton studentRequestsButton = new JButton("Students Requests");
        JButton staffRequestsButton=new JButton("Staff Requests");

        // Add buttons to panel
        panel.add(manageClassButton);
        panel.add(manageStudentButton);
        panel.add(manageStaffButton);
        panel.add(studentRequestsButton);
        panel.add(staffRequestsButton);

        // Add panel to frame
        add(panel);

        // Button actions
        manageClassButton.addActionListener(e -> {
            new ManageClass();
            dispose(); // Close the main menu
        });

        manageStudentButton.addActionListener(e -> {
            new ManageStudent();
            dispose(); // Close the main menu
        });

        manageStaffButton.addActionListener(e -> {
            new ManageStaff();
            dispose(); // Close the main menu
        });
        
        staffRequestsButton.addActionListener(e->{
        	new HandleStaffRequests();
        	dispose();
        });

//        manageRequestsButton.addActionListener(e -> {
//            new ManageRequests();
//            dispose(); // Close the main menu
//        });
        checkRequests();

        setVisible(true);
    }
    private void checkRequests() {
		boolean newrequests=hasnewrequests();
		if(newrequests) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, " You have New Staff Leave Requests","New requests",JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
	}

    public static void main(String[] args) {
        new MainMenu();
    }
}
