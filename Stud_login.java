package Assignment4;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.SwingUtilities;


public class Stud_login implements SQL_Detail
{
	public static Connection connection;
	static JFrame mainFrame;
	static Login login = new Login();
	static SignIn signIn;
	

	public static void dialogBox(String messge)
	{
		JOptionPane pane = new JOptionPane(messge,JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION,null,new Object[]{},null);
		
		JDialog dialog = pane.createDialog(null, "Message");
		dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		new Timer(1000,new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		}).start();
		dialog.setVisible(true);
	}
	public Stud_login()
	{
		mainFrame = new JFrame("Student Login");
		mainFrame.setBounds(340, 150, 600, 450);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try 
		{
			Class.forName(driverString);
			connection = DriverManager.getConnection(urlString, user_idString, passwordString);
			
			// Custom Dialog box with 1000ms closing time
			dialogBox("Connected to the DataBase!!!");
			
		} catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e) 
		{
			JOptionPane.showMessageDialog(null,"Not Connected to the DataBase!!!","Error", JOptionPane.OK_OPTION);
			mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
		}
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		
		
		mainFrame.add(login);
		signIn = new SignIn(connection);
		mainFrame.setVisible(true);		
	}
	static class Login extends JPanel implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		public Login() {
			JButton signIn,signUp,returnButton;
			
			signIn = new JButton("Sign In");
			signIn.setActionCommand("SignIn");
			signIn.addActionListener(this);
			signUp = new JButton("Sign Up");
			signUp.setActionCommand("SignUp");
			signUp.addActionListener(this);
			returnButton = new JButton("Return");
			returnButton.setActionCommand("Return");
			returnButton.addActionListener(this);
			
			this.add(signIn);
			this.add(signUp);
			this.add(returnButton);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equalsIgnoreCase("SignIn"))
			{
				SwingUtilities.invokeLater(new Runnable() {				
					@Override
					public void run() {
						mainFrame.remove(login);
						mainFrame.add(signIn);
						mainFrame.invalidate();
						mainFrame.revalidate();
						mainFrame.repaint();
					}
				});
			}
			else if(e.getActionCommand().equalsIgnoreCase("SignUp"))
			{
				try {
					Student.main(null);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			else if(e.getActionCommand().equalsIgnoreCase("Return"))
			{
				mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
			}
		}
	}
	static class SignIn extends JPanel implements SQL_Detail,ActionListener
	{
		private Connection connection;
		private JTextField sign_name_text,sign_uname_text;
		private JLabel sign_name_label,sign_uname_label;
		private JButton logIn,cancel;
		public JPanel signButtonsPanel;
		private static final long serialVersionUID = 1L;
		public SignIn(Connection connection) {
			this.connection = connection;
			
			signButtonsPanel = new JPanel();
			GridLayout gridLayout = new GridLayout(3,2);
			gridLayout.setHgap(5);
			gridLayout.setVgap(10);
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(10, 10, 10, 10);
			signButtonsPanel.setLayout(gridLayout);
			
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 0;
			constraints.gridy = 0;
			sign_name_label = new JLabel("Enter your Name:");
			sign_name_label.setHorizontalAlignment((int) CENTER_ALIGNMENT);
			signButtonsPanel.add(sign_name_label,constraints);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 0;
			constraints.gridy = 2;
			sign_name_text = new JTextField(20);
			sign_name_text.setText("");
			signButtonsPanel.add(sign_name_text,constraints);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 2;
			constraints.gridy = 0;
			sign_uname_label = new JLabel("Enter User Name:");
			sign_uname_label.setHorizontalAlignment((int)CENTER_ALIGNMENT);
			signButtonsPanel.add(sign_uname_label,constraints);			
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 2;
			constraints.gridy = 2;
			sign_uname_text = new JTextField(20);
			sign_uname_text.setText("");
			signButtonsPanel.add(sign_uname_text,constraints);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 0;
			constraints.gridy = 3;
			cancel = new JButton("Cancel");
			cancel.setActionCommand("Cancel");
			signButtonsPanel.add(cancel,constraints);
			cancel.addActionListener(this);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 2;
			constraints.gridy = 3;
			logIn = new JButton("Log In");
			logIn.setActionCommand("LogIn");
			signButtonsPanel.add(logIn,constraints);
			logIn.addActionListener(this);
			
			
			this.add(signButtonsPanel);
		}
		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equalsIgnoreCase("Cancel"))
			{
				mainFrame.remove(signIn);
				mainFrame.add(login);
				mainFrame.invalidate();
				mainFrame.revalidate();
				mainFrame.repaint();
			}
			else if(e.getActionCommand().equalsIgnoreCase("LogIn"))
			{
				try {
					if(!sign_name_text.getText().isBlank() && !sign_uname_text.getText().isBlank())
					{
						PreparedStatement pstmt = connection.prepareStatement("select exists(select * from studentdetails where Name=? and User_Name=?)");
						pstmt.setString(1, sign_name_text.getText());
						pstmt.setString(2, sign_uname_text.getText());
						ResultSet rSet = pstmt.executeQuery();
						Stud_login.dialogBox("Checking.......");
						if(rSet.next() && rSet != null) {
							Stud_login.dialogBox("Logged In Successfully!!");
							this.setVisible(false);
							new Existing_Student(connection, sign_name_text.getText());
							this.setVisible(true);
							sign_name_text.setText("");
							sign_uname_text.setText("");
						}
						else {
							Stud_login.dialogBox("Record not Found!!!!");
							mainFrame.dispose();
						}
					}
					else {
						Stud_login.dialogBox("Enter Correct Data!!!");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} 
				catch (NullPointerException e2) {
					e2.printStackTrace();
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}			
		}
	}
}

		
				
