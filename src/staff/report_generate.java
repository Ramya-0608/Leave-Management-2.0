package staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
class report_generate{

	    report_generate(String staffid) {
	        JFrame frame = new JFrame("Absentees Data");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(600, 400);


	        DefaultTableModel model = new DefaultTableModel();

	        // Define column names
	        model.addColumn("Staff Id");
	        model.addColumn("Students Name");
	        model.addColumn("Register Number");
	        model.addColumn("Total Absent Days");
	        try 
	        { 
	        
	        String url = "jdbc:mysql://localhost:3306/details";  
	        String user = "root";  
	        String password = "muthu@123";  
	        String query = "SELECT staff_id, name, stud_id, no_of_leave_days FROM students where staff_id=?";

	        
	             Connection conn = DriverManager.getConnection(url, user, password);
	         	
	             PreparedStatement stmt = conn.prepareStatement(query);
	             stmt.setString(1, staffid);
	             ResultSet rs = stmt.executeQuery(); {

	             while (rs.next()) {
	                Vector<Object> row = new Vector<>();
	                row.add(rs.getString("staff_id"));
	                row.add(rs.getString("name"));
	                row.add(rs.getString("stud_id"));
	                row.add(rs.getInt("no_of_leave_days"));

	                
	                model.addRow(row);
	            }
	             }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        // Create a JTable and set the model
	        JTable table = new JTable(model);

	        // Add the table to a scroll pane
	        JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setBounds(50,100,500,200);
	        frame.add(scrollPane);

	        // Make the frame visible
	        frame.setVisible(true);
		    }
	    }


