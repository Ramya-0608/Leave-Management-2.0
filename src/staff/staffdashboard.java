package staff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

import javax.swing.JOptionPane;


public class staffdashboard {
	String staffid;
	private static boolean hasnewrequests(String sid) {
		 boolean hasrequests=false;
		  try {
			  Connection cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/details","root","muthu@123");
			  String q="SELECT count(*) FROM requests WHERE staff_id=? AND is_viewed=FALSE";
			  PreparedStatement pt=cn.prepareStatement(q);
			  pt.setString(1, sid);
			  ResultSet rs=pt.executeQuery();
			  if(rs.next()&&rs.getInt(1)>0) {
				  hasrequests=true;
            }
		  }catch(SQLException e) {
	                  e.printStackTrace();
           }
		  return hasrequests;
		  }

	staffdashboard(String name,String id){
	    this.staffid=id;
		JFrame frame2=new JFrame();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setLayout(null);
		frame2.setSize(800,800);
		
		JButton bn=new JButton("Show new requests");
		bn.setBounds(400,100,180,30);
		frame2.add(bn);
		bn.addActionListener(e->{
			new showRequests(staffid);
			frame2.setVisible(false);
		});
		
		JButton bn3=new JButton("Generate Report");
		bn3.setBounds(400,200,180,30);
		frame2.add(bn3);
		bn3.addActionListener(e->{
			new report_generate(staffid);
			frame2.dispose();
		});
		
		JButton bn2=new JButton("Show all requests");
		bn2.setBounds(400,300,180,30);
		frame2.add(bn2);
		bn2.addActionListener(e->{
			new showAllRequests(staffid);
			frame2.setVisible(false);
		});
		
		JButton back=new JButton("Log out");
		back.setBounds(700,30,80,30);
		frame2.add(back);
		back.addActionListener(e->{
			new dashboard();
			frame2.dispose();
		});
		checkRequests();
		
		
		frame2.setVisible(true);
		
	}
	private void checkRequests() {
		boolean newrequests=hasnewrequests(staffid);
		if(newrequests) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "You have new requests","New requests",JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
	}

	}
