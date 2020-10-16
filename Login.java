package Assignment4;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;


public class Login extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static JButton adminButton,studentButton;
	public Login()
	{
		this.setTitle("Login Page");
		this.setBounds(340, 150, 600, 450);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Admin"))
		{
			try {
				SysAdmin.main(null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("Student"))
		{
			try {
				new Stud_login();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	public static void main(String args[]) throws SQLException
	{
		
		Login loginFrame = new Login();
		adminButton = new JButton("Admin Login");
		studentButton = new JButton("Student Login");
		
		loginFrame.setLayout(null);
		loginFrame.add(adminButton);
		loginFrame.add(studentButton);
		
		// Admin
		adminButton.setBounds(200, 150, 200, 50);
		adminButton.setActionCommand("Admin");
		adminButton.addActionListener(loginFrame);
		
		// Student 
		studentButton.setBounds(200, 210, 200, 50);
		studentButton.setActionCommand("Student");
		studentButton.addActionListener(loginFrame);
	}
}
