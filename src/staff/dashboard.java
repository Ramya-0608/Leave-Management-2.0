package staff;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Panel;

import javax.swing.*;

public class dashboard extends JFrame {
 dashboard(){
	setSize(800,800);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLayout(new BorderLayout());
	
	JPanel panel=new JPanel();
	panel.setLayout(null);
	JLabel title=new JLabel("WELCOME");
	title.setFont(new Font("Arial",Font.BOLD,21));
	title.setBounds(150,100,200,30);
	
	panel.add(title);
	
	JButton staffbutton=new JButton("Staff");
	staffbutton.setBounds(100,300,100,30);
	panel.add(staffbutton);
	staffbutton.addActionListener(e->{
		new staffLogin();
	});
	
	JButton studentsbutton=new JButton("Students");
	studentsbutton.setBounds(250,300,100,30);
	panel.add(studentsbutton);
	studentsbutton.addActionListener(e->{
		new studentpanel();
	});
	
	add(panel);
	setVisible(true);
	
 }
  public static void main(String a[]) {
	 new dashboard();
 }
}
