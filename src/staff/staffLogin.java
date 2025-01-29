package staff;

import java.awt.*;
import database.DBConnect;
import java.sql.*;

import javax.swing.*;

public class staffLogin {

	public staffLogin() {
		JFrame f = new JFrame("login");
		f.setSize(800, 800);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(null);
		f.add(panel);

		JLabel staffnamelabel = new JLabel("Name:");
		staffnamelabel.setBounds(400, 50, 100, 30);
		JTextField staffname = new JTextField();
		staffname.setBounds(460, 50, 200, 30);
		panel.add(staffnamelabel);
		panel.add(staffname);

		JLabel staffidlabel = new JLabel("ID:");
		staffidlabel.setBounds(400, 100, 100, 30);
		JTextField staffid = new JTextField();
		staffid.setBounds(460, 100, 200, 30);
		panel.add(staffidlabel);
		panel.add(staffid);

		JButton loginbutton = new JButton("Login");
		loginbutton.setBounds(430, 200, 200, 30);
		panel.add(loginbutton);

		loginbutton.addActionListener(e -> {
			String name = staffname.getText().trim();
			String id = staffid.getText().trim();
			if (validate(name, id)) {
				JOptionPane.showMessageDialog(null, "Login Successfull");
				new staffdashboard(name, id);
				f.setVisible(false);

			} else {
				JOptionPane.showMessageDialog(null, "Incorrect name or ID", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		});
		f.setVisible(true);
	}

	private static boolean validate(String name, String id) {
		boolean is_valid = false;
		String query = "select * from staff where staff_id=? and name=?";
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement pmt = con.prepareStatement(query);
			pmt.setString(1, id);
			pmt.setString(2, name);
			ResultSet rs = pmt.executeQuery();
			is_valid = rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return is_valid;
	}
	

}
