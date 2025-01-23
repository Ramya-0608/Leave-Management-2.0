package staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class showAllRequests extends JFrame {
	String staffid;

	private static List<Object[]> getrequests(String staffid, String date) {
		List<Object[]> requests = new ArrayList<>();

		String query = "SELECT sno,stu_id,name,leave_date,reason from requests WHERE staff_id=? and leave_date=?";
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/details", "root", "muthu@123")) {
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, staffid);
			stmt.setString(2, date);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				requests.add(new Object[] { rs.getInt("sno"), rs.getString("stu_id"), rs.getString("name"),
						rs.getDate("leave_date"), rs.getString("reason"),

				});

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return requests;

	}

	showAllRequests(String id) {
		this.staffid = id;
		setTitle("New requests");
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);

		JLabel dateLabel = new JLabel("Select Date:");
		dateLabel.setBounds(100, 50, 100, 30);

		JTextField datefield = new JTextField(20);
		datefield.setBounds(200, 50, 200, 30);
		String placeholder = "yyy-mm-dd";
		datefield.setText(placeholder);
		datefield.setForeground(Color.GRAY);
		datefield.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (datefield.getText().equals(placeholder)) {
					datefield.setText("");
					datefield.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (datefield.getText().isEmpty()) {
					datefield.setText(placeholder);
					datefield.setForeground(Color.GRAY);
				}
			}
		});

		add(dateLabel);
		add(datefield);

		JButton get = new JButton("Get");
		get.setBounds(450, 50, 80, 30);
		add(get);

		SwingUtilities.invokeLater(() -> {
			requestFocus();
		});

		String[] cols = { "Request ID", "Student ID", "Name", "leave date", "Reason" };
		DefaultTableModel tble = new DefaultTableModel(cols, 0);
		JTable table = new JTable(tble);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setBounds(50, 100, 700, 200);
		scrollpane.setVisible(false);
		add(scrollpane);

		get.addActionListener(e -> {
			String date = (datefield.getText().trim());
			List<Object[]> requests = getrequests(staffid, date);
			if (!requests.isEmpty()) {
				tble.setRowCount(0);
				for (Object[] row : requests) {
					tble.addRow(row);
				}
				scrollpane.setVisible(true);
				scrollpane.revalidate();
				scrollpane.repaint();
			} else {
				JOptionPane.showMessageDialog(null, "No requests");

			}
		});

		JButton back = new JButton("<-- Back");
		back.setBounds(700, 50, 80, 30);
		add(back);
		back.addActionListener(e -> {
			scrollpane.revalidate();
			new staffdashboard("", staffid);
			dispose();

		});
		setVisible(true);
	}

}
