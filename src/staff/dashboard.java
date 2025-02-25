package staff;

import java.awt.Font;
import javax.swing.*;

public class dashboard extends JFrame {
    dashboard() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null); 

       
        JPanel panel = new JPanel();
        panel.setLayout(null);  

        JLabel title = new JLabel("WELCOME");
        title.setFont(new Font("Arial", Font.BOLD, 21));
        title.setBounds(150, 100, 200, 30);
        panel.add(title);

        // Staff Button
        JButton staffButton = new JButton("Staff");
        staffButton.setBounds(100, 300, 100, 30);  
        panel.add(staffButton);
        staffButton.addActionListener(e -> {
            new staffLogin();
            dispose();
        });

        // Students Button
        JButton studentsButton = new JButton("Students");
        studentsButton.setBounds(250, 300, 100, 30); 
        panel.add(studentsButton);
        studentsButton.addActionListener(e -> {
            new studentpanel();
            dispose();
        });

        // Add the panel to the frame
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new dashboard();
    }
}
