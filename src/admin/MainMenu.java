package admin;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        // Frame setup
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        // Buttons for navigation
        JButton manageClassButton = new JButton("Manage Class");
        JButton manageStudentButton = new JButton("Manage Student");
        JButton manageStaffButton = new JButton("Manage Staff");
        JButton manageRequestsButton = new JButton("Manage Requests");

        // Add buttons to panel
        panel.add(manageClassButton);
        panel.add(manageStudentButton);
        panel.add(manageStaffButton);
        panel.add(manageRequestsButton);

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

//        manageRequestsButton.addActionListener(e -> {
//            new ManageRequests();
//            dispose(); // Close the main menu
//        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
