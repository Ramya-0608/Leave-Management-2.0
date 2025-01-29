package admin;

import java.util.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import database.DBConnect;

public class ManageClass extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel buttonPanel, contentPanel;
	JButton addButton, updateButton, deleteButton, viewButton;
	JTable classTable;
	DefaultTableModel tableModel;
	JTextField addClassField;

	public ManageClass() {
		// Frame setup
		setTitle("Manage Class");
		setSize(800, 600);
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
		tableModel = new DefaultTableModel(new Object[] { "ID", "Class Name" }, 0);
		classTable = new JTable(tableModel);
		viewPanel.add(new JScrollPane(classTable), BorderLayout.CENTER);

		// Add Panel
		JPanel addPanel = new JPanel();
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
		JLabel addClassLabel = new JLabel("Enter Class Name:");
		addClassField = new JTextField();
		JButton addSubmitButton = new JButton("Add Class");
		addClassField.setMaximumSize(new Dimension(300, 30));
		addClassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		addClassField.setAlignmentX(Component.CENTER_ALIGNMENT);
		addSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addPanel.add(Box.createVerticalGlue());
		addPanel.add(addClassLabel);
		addPanel.add(Box.createVerticalStrut(10));
		addPanel.add(addClassField);
		addPanel.add(Box.createVerticalStrut(20));
		addPanel.add(addSubmitButton);
		addPanel.add(Box.createVerticalGlue());

		// Update Panel
		JPanel updatePanel = new JPanel();
		updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));
		JLabel updateOldClassLabel = new JLabel("Enter Old Class Name:");
		JTextField updateOldClassField = new JTextField();
		JLabel updateNewClassLabel = new JLabel("Enter New Class Name:");
		JTextField updateNewClassField = new JTextField();
		JButton updateSubmitButton = new JButton("Update Class");
		updateOldClassField.setMaximumSize(new Dimension(300, 30));
		updateNewClassField.setMaximumSize(new Dimension(300, 30));
		updateOldClassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		updateOldClassField.setAlignmentX(Component.CENTER_ALIGNMENT);
		updateNewClassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		updateNewClassField.setAlignmentX(Component.CENTER_ALIGNMENT);
		updateSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		updatePanel.add(Box.createVerticalGlue());
		updatePanel.add(updateOldClassLabel);
		updatePanel.add(Box.createVerticalStrut(10));
		updatePanel.add(updateOldClassField);
		updatePanel.add(Box.createVerticalStrut(10));
		updatePanel.add(updateNewClassLabel);
		updatePanel.add(Box.createVerticalStrut(10));
		updatePanel.add(updateNewClassField);
		updatePanel.add(Box.createVerticalStrut(20));
		updatePanel.add(updateSubmitButton);
		updatePanel.add(Box.createVerticalGlue());

		// Delete Panel
		JPanel deletePanel = new JPanel();
		deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
		JLabel deleteClassLabel = new JLabel("Enter Class Name:");
		ArrayList<String> classList = new ArrayList<>();
		classList.add("select");
		try (Connection con = DBConnect.getConnection();
				PreparedStatement stmt = con.prepareStatement("Select * from classes")) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				classList.add(rs.getString("classname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Object[] ob = classList.toArray();
		JComboBox<Object> deleteClassField = new JComboBox<>(ob);
		JButton deleteSubmitButton = new JButton("Delete Class");
		deleteClassField.setMaximumSize(new Dimension(300, 30));
		deleteClassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteClassField.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		deletePanel.add(Box.createVerticalGlue());
		deletePanel.add(deleteClassLabel);
		deletePanel.add(Box.createVerticalStrut(10));
		deletePanel.add(deleteClassField);
		deletePanel.add(Box.createVerticalStrut(20));
		deletePanel.add(deleteSubmitButton);
		deletePanel.add(Box.createVerticalGlue());

		// Add panels to content panel
		contentPanel.add(viewPanel, "View");
		contentPanel.add(addPanel, "Add");
		contentPanel.add(updatePanel, "Update");
		contentPanel.add(deletePanel, "Delete");

		// Add components to frame
		add(buttonPanel, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);

		// Show the "View" panel by default
		CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
		cardLayout.show(contentPanel, "View");

		// Button actions
		addButton.addActionListener(e -> cardLayout.show(contentPanel, "Add"));
		viewButton.addActionListener(e -> {
			loadClasses();
			cardLayout.show(contentPanel, "View");
		});
		updateButton.addActionListener(e -> cardLayout.show(contentPanel, "Update"));
		deleteButton.addActionListener(e -> cardLayout.show(contentPanel, "Delete"));

		// Add operation
		addSubmitButton.addActionListener(e -> {
//            String staffId = addStaffField.getText().trim();
			String className = (String) addClassField.getText().trim();
			if (insertClass(className)) {
				JOptionPane.showMessageDialog(this, "Class added successfully!");
//                addStaffField.setText("");
				addClassField.setText("");
				loadClasses();
				cardLayout.show(contentPanel, "View");
			}
		});

		// Update operation
		updateSubmitButton.addActionListener(e -> {
			String oldClassName = (String) updateOldClassField.getText().trim();
			String newClassName = (String) updateNewClassField.getText().trim();
			if (updateClass(oldClassName, newClassName)) {
				JOptionPane.showMessageDialog(this, "Class updated successfully!");
				updateOldClassField.setText("");
				updateNewClassField.setText("");
				loadClasses();
				cardLayout.show(contentPanel, "View");
			}
		});

		// Delete operation
		deleteSubmitButton.addActionListener(e -> {
			String className = (String) deleteClassField.getSelectedItem();
			if(className=="select") {
				JOptionPane.showMessageDialog(null, "Select class to Delete");
				return;
			}
			if (deleteClass(className)) {
				JOptionPane.showMessageDialog(this, "Class deleted successfully!");
				loadClasses();
				cardLayout.show(contentPanel, "View");
			}
		});

		setVisible(true);
	}

	private void loadClasses() {
		tableModel.setRowCount(0); // Clear existing rows
		String query = "SELECT id, classname FROM classes";

		try (Connection con = DBConnect.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				tableModel.addRow(new Object[] { rs.getString("id"), rs.getString("classname") });
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading classes: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean insertClass(String className) {
		if (className.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Class Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		String query = "INSERT INTO classes(classname) VALUES(?)";

		try (Connection con = DBConnect.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, className);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	private boolean updateClass(String oldClassName, String newClassName) {
		if (oldClassName.isEmpty() || newClassName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Old Class Name and New Class Name are required.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		String query = "UPDATE classes SET classname = ? WHERE classname = ?";

		try (Connection con = DBConnect.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, newClassName);
			pstmt.setString(2, oldClassName);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	private boolean deleteClass(String className) {
		if (className.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Class Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		String query = "DELETE FROM classes WHERE classname = ?";

		try (Connection con = DBConnect.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, className);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

//	public static void main(String[] args) {
//		new ManageClass();
//	}
}
