
package staff;

import javax.swing.*;

import com.mysql.cj.protocol.Resultset;

import database.DBConnect;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

 class staffLeaveRequestForm {
	 public JFrame frame;
	 String name,id;
    public staffLeaveRequestForm(String username, String password) {
    	this.name=username;
    	this.id=password;
         frame = new JFrame("Leave Request Form");
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(7, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(username);
        userField.setEditable(false);

        JLabel idLabel = new JLabel("Staff ID:");
        JTextField idField = new JTextField(password);
        idField.setEditable(false);

        JLabel dateLabel = new JLabel("Leave Date:");
        JTextField dateField = new JTextField(LocalDate.now().toString());

        JCheckBox reason1 = new JCheckBox("Personal");
        JCheckBox reason2 = new JCheckBox("Medical");
        JCheckBox reason3 = new JCheckBox("Vacation");

        JLabel otherReasonLabel = new JLabel("Other Reason:");
        JTextField otherReasonField = new JTextField();

        JButton submitButton = new JButton("Submit");

        frame.add(userLabel);
        frame.add(userField);
        frame.add(idLabel);
        frame.add(idField);
        frame.add(dateLabel);
        frame.add(dateField);
        frame.add(reason1);
        frame.add(reason2);
        frame.add(reason3);
        frame.add(otherReasonLabel);
        frame.add(otherReasonField);
        frame.add(new JLabel()); // Empty space
        frame.add(submitButton);

        submitButton.addActionListener(e -> {
            String leaveDate = dateField.getText().trim();
            String reason = "";
            if (reason1.isSelected()) reason += "Personal ";
            if (reason2.isSelected()) reason += "Medical ";
            if (reason3.isSelected()) reason += "Vacation ";
            if (!otherReasonField.getText().trim().isEmpty()) reason += otherReasonField.getText().trim();

            if (!reason.isEmpty()) {
                submitLeaveRequest(id,name, leaveDate, reason);
               // Redirect to login after submitting leave request
            } 
        });

        frame.setVisible(true);
    }

    private void submitLeaveRequest(String password,String username, String leaveDate, String reason) {
        // Insert a new leave request into the leave_requests table
        String insertLeaveRequestQuery = "INSERT INTO staff_requests (staff_id,name,leave_date,reason) VALUES (?, ?, ?,?)";
        
        

        try (Connection con = DBConnect.getConnection();
             PreparedStatement pstmtInsert = con.prepareStatement(insertLeaveRequestQuery);
             ) {

            // Insert the leave request into the leave_requests table
        	pstmtInsert.setString(1,password);
            pstmtInsert.setString(2, username);
            pstmtInsert.setString(3, leaveDate);
            pstmtInsert.setString(4, reason);
            int i=pstmtInsert.executeUpdate();
           
            
            
            if(i>0) {
            	  JOptionPane.showMessageDialog(null, "Leave Request Submitted Successfully");
            	  frame.dispose();
                  new staffdashboard(username,password);
            }
            else {
                JOptionPane.showMessageDialog(null, "Please select or enter a reason", "Error", JOptionPane.ERROR_MESSAGE);
            }
           
           

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
