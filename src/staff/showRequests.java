package staff;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import database.DBConnect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.*;

public class showRequests extends JFrame {
	static String staffid=null;
	public static  Connection Connect() {
		Connection cn=null;
		try {
		cn=DBConnect.getConnection();
	}catch(SQLException e) {
		e.printStackTrace();
	}
		return cn;
	}
		
	public static List<Object[]> getrequests(String staffid)
	{
		List<Object[]> requests=new ArrayList<>();

		String query="SELECT sno,stu_id,name,leave_date,reason from requests WHERE staff_id=? and is_viewed=FALSE";
		try (Connection con=Connect()){
			PreparedStatement stmt=con.prepareStatement(query);
			stmt.setString(1,staffid);
			ResultSet rs= stmt.executeQuery();
			while(rs.next()) {
				requests.add(new Object[] {
						rs.getInt("sno"),
						rs.getString("stu_id"),
						rs.getString("name"),
						rs.getDate("leave_date"),
						rs.getString("reason"),
						
						
				});
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return requests;
	
	}
	
	public static void markRequest(int requestid) {
		String updatequery="UPDATE requests SET is_viewed=TRUE WHERE sno=?";
		try(Connection con=Connect()){
			PreparedStatement stmt=con.prepareStatement(updatequery);
			stmt.setInt(1,requestid);
			stmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
     showRequests(String id){
    	 this.staffid=id;
    	 setTitle("New requests");
    	 setSize(800,800);
    	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	 setLocationRelativeTo(null);
    	 JPanel toppanel=new JPanel();
    	 toppanel.setLayout(new BoxLayout(toppanel,BoxLayout.X_AXIS));
    	 JLabel heading=new JLabel("NEW REQUESTS");
    	 heading.setHorizontalAlignment(SwingConstants.LEFT);
    	 toppanel.add(heading);
    	 toppanel.add(Box.createHorizontalGlue());
    	 
    	 JButton back=new JButton("<-- Back");
    	 back.setBounds(750,0,80,30);
    	 toppanel.add(back,BorderLayout.EAST);
    	 back.addActionListener(e->{
    		 
    		 new staffdashboard("",staffid);
    		 dispose();
    		
    	 });
    	 
    	 toppanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    	 JTable requestTable=createTable();
    	 if(requestTable.getRowCount()==0) {
   		  JOptionPane.showMessageDialog(null, "No Requests");
   	     }
    	 else {
    	 JScrollPane scrollPane=new JScrollPane(requestTable);
    	 add(scrollPane,BorderLayout.CENTER);
    	 }
    	 
    	 //scrollPane.setBounds(0,0,700,0);
    	 
    	 add(toppanel,BorderLayout.NORTH);
    	
    	 
    	 setVisible(true);
    	 
     }
      static JTable createTable() {
    	  String[] columns= {"RequestID","StudentID","Name","Date","Reason","Status"};
    	  List<Object[]> requests=getrequests(staffid);
    	  DefaultTableModel tble=new DefaultTableModel(columns,0);
          for(Object[] row :requests) {
        	  tble.addRow(row);
          }
    	  JTable table=new JTable(tble); 
    	  table.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column) {
			JLabel acceptlable=new JLabel("Accept");
			acceptlable.setOpaque(true);
  			  acceptlable.setBackground(isSelected? Color.LIGHT_GRAY:new Color(0,128,128));
  			  acceptlable.setForeground(Color.WHITE);
			  acceptlable.setBorder(BorderFactory.createLineBorder(Color.GREEN,1));
			  return acceptlable;
			}
    	  });
    	  
    	  table.addMouseListener(new MouseAdapter(){
    		  public void mouseClicked(MouseEvent e) {
    		  int row=table.rowAtPoint(e.getPoint());
    		  int col=table.columnAtPoint(e.getPoint());
    		  
    		  if(col==5) {
    			  int requestid=(Integer)table.getValueAt(row,0);
    			  markRequest(requestid);
    			  ((DefaultTableModel) table.getModel()).removeRow(row);
    		  }
    		  }
    	  });
    	return table;
    	  }
      }

