package admin;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import database.DBConnect;

public class Login extends JFrame {

	JLabel title, usernameLabel, passwordLabel;
	JPanel panel, usernamePanel, passwordPanel;
	JTextField username;
	JPasswordField password;
	JButton loginBtn;

	public Login() {

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		title = new JLabel("Login");
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setFont(new Font("Arial", Font.BOLD, 25));

		usernameLabel = new JLabel("Username");
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

		username = new JTextField();
//		username.setAlignmentX(CENTER_ALIGNMENT);
		username.setMaximumSize(new Dimension(250, 30));
		username.setFont(new Font("Arial", Font.PLAIN, 16));

		usernamePanel = new JPanel();
		usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
		usernamePanel.add(usernameLabel);
		usernamePanel.add(Box.createHorizontalStrut(10));
		usernamePanel.add(username);
		usernamePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		passwordLabel = new JLabel("Password");
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));

		password = new JPasswordField();
//		password.setAlignmentX(CENTER_ALIGNMENT);
		password.setMaximumSize(new Dimension(250, 30));
		password.setFont(new Font("Arial", Font.PLAIN, 16));

		passwordPanel = new JPanel();
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(Box.createHorizontalStrut(10));
		passwordPanel.add(password);
		passwordPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		loginBtn = new JButton("Login");
		loginBtn.setAlignmentX(CENTER_ALIGNMENT);
		loginBtn.setMaximumSize(new Dimension(150, 30));
		loginBtn.setFont(new Font("Arial", Font.PLAIN, 16));

		panel.add(Box.createVerticalGlue());
		panel.add(title);
		panel.add(usernamePanel);
		panel.add(passwordPanel);
		panel.add(loginBtn);
		panel.add(Box.createVerticalGlue());

		add(panel);

		setTitle("Login");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);

		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String usernameData = username.getText().trim();
				char passwordData[] = password.getPassword();

				if (validateAdmin(usernameData, passwordData)) {
					JOptionPane.showMessageDialog(null, "Login Successful!");
					
					dispose();
					new MainMenu();
				} else {
					JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private boolean validateAdmin(String username, char[] password) {

		String query = "select * from admin where username=? AND password=?";
		try (Connection con = DBConnect.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, username);
			stmt.setString(2, new String(password));
			
			System.out.println(stmt.toString());

			ResultSet rs = stmt.executeQuery();

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static void main(String args[]) {
		new Login();
	}
}
