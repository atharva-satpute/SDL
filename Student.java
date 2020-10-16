package Assignment4;
import java.util.*;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;

public class Student implements SQL_Detail,ActionListener
{
	static JFrame mainFrame;
	
	// Insert variables
	private JLabel rollno_label,name_label;
	private JLabel uname_label, email_label, phoneno_label, grp_label, experience_label;
	private JTextField rollno_text,name_text;
	private JTextField uname_text, email_text, phoneno_text, experience_text;
	Button[] buttons2 = {new Button("Cancel"),new Button("Clear"),new Button("Register")};
	JComboBox<String> grpnames;
	
	//SQL
	private static Connection connection;
	private static String insertQuery ="insert into studentdetails(Roll_No,Name,User_Name,Email_Id,Phone_No,Group_Name,Experience)"
			+ " values(?,?,?,?,?,?,?)";
	
		
	public boolean verify() throws Exception
	{		
		boolean isBlankName = false;
		if(name_label.getText().isBlank()) {
			isBlankName = true;
			//dialogBox("Try Again!!!");
		}
		
		boolean isBlankUsrName = false;
		if(uname_label.getText().isBlank()) {
			isBlankUsrName = true;
			//dialogBox("Try Again!!");
		}
		
		boolean isnumeric = false;
		try
		{
			Integer.parseInt(rollno_text.getText());
			isnumeric = true;
		}catch(NumberFormatException e)
		{
			isnumeric = false;
			//dialogBox("Invalid Input!!! Please try again!!");
		}
		
		// Accepting Phone No.
		boolean isPhoneNo = false;
		try
		{
			if(phoneno_text.getText().length() == 10)
			{
				isPhoneNo = true;
				Integer.parseInt(phoneno_text.getText());
			}
			else if(phoneno_text.getText().length() > 10 || phoneno_text.getText().length() < 10)
			{
				isPhoneNo = false;
				//dialogBox("Invalid Phone number!!!! Please Try Again!!");
			}
		}catch(NumberFormatException e)
		{
			isPhoneNo = false;
			//dialogBox("Please enter numeric data!!!");
		}
		
		// Accepting Experience
		boolean isExperience = false;
		try
		{
			if(Integer.parseInt(experience_text.getText()) >=1 &&  Integer.parseInt(experience_text.getText()) <=3)
				isExperience= true;
			else
			{
				isExperience = false;
				//dialogBox("Wrong Input!!! Try Again!!");
			}
		}catch(NumberFormatException e)
		{
			isExperience = false;
			//dialogBox("Numeric input required!!!!");
		}
		catch(InputMismatchException e)
		{
			//dialogBox("Invalid Input!!!! Please try again!!");
		}
		
		return (!isBlankName && !isBlankUsrName && isnumeric && isExperience && isPhoneNo);
	}
	private void dialogBox(String message)
	{
		JOptionPane pane = new JOptionPane(message,JOptionPane.WARNING_MESSAGE,
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
	private static void Student_createGUI() {
		mainFrame = new JFrame("New Data");
		mainFrame.setBounds(340, 150, 600, 450);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE | JFrame.DISPOSE_ON_CLOSE);
		
		try 
		{
			Class.forName(driverString);
			connection = DriverManager.getConnection(urlString, user_idString, passwordString);
			
			// Custom Dialog box with 700ms closing time
			JOptionPane pane = new JOptionPane("Connected to the DataBase!!!",JOptionPane.INFORMATION_MESSAGE,
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
		} catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e) 
		{
			JOptionPane.showMessageDialog(null,"Not Connected to the DataBase!!!","Error", JOptionPane.OK_OPTION);
			mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
		}
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		
		Student student = new Student();
		student.addCoponentToPane(mainFrame.getContentPane());
		mainFrame.setVisible(true);		
	}
	private void addCoponentToPane(Container contentPane) {
		JPanel insert_card = new JPanel();
		GridBagLayout gridLayout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 5, 5);
		insert_card.setLayout(gridLayout);
		
		// Roll No
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		rollno_label = new JLabel("Enter Roll no:");
		insert_card.add(rollno_label,constraints);	
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 2;
		constraints.gridy = 0;
		rollno_text = new JTextField(20);
		insert_card.add(rollno_text,constraints);
		
		//Name
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		name_label = new JLabel("Enter Name:");
		insert_card.add(name_label,constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 2;
		constraints.gridy = 2;
		name_text = new JTextField(2);
		insert_card.add(name_text,constraints);
		
		// User Name
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 3;
		uname_label = new JLabel("Enter User Name:");
		insert_card.add(uname_label,constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 2;
		constraints.gridy = 3;
		uname_text = new JTextField(2);
		insert_card.add(uname_text,constraints);
		
		// Email Id
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 4;
		email_label = new JLabel("Enter Email Id:");
		insert_card.add(email_label,constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 2;
		constraints.gridy = 4;
		email_text = new JTextField(2);
		insert_card.add(email_text,constraints);
		
		// Phone No
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 5;
		phoneno_label = new JLabel("Enter Phone No:");
		insert_card.add(phoneno_label,constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 2;
		constraints.gridy = 5;
		phoneno_text = new JTextField(2);
		insert_card.add(phoneno_text,constraints);
		
		// Group Name
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 6;
		grp_label = new JLabel("Enter Group Name:");
		insert_card.add(grp_label,constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 2;
		constraints.gridy = 6;		
		grpnames = new JComboBox<String>(new String[] {"ML","Cloud","BlockChain"});
		grpnames.setEditable(false);
		insert_card.add(grpnames,constraints);
		
		// Experience
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 7;
		experience_label = new JLabel("Enter Experience:");
		insert_card.add(experience_label,constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 2;
		constraints.gridy = 7;
		experience_text = new JTextField(2);
		insert_card.add(experience_text,constraints);
		
		// Buttons
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 8;
		JPanel buttons = new JPanel();
		GridLayout gridLayout_buttonGridLayout = new GridLayout(1, 3); 
		gridLayout_buttonGridLayout.setHgap(5);
		buttons.setLayout(gridLayout_buttonGridLayout);
		buttons2[0].setActionCommand("Cancel");
		buttons2[0].addActionListener(this);
		buttons2[1].setActionCommand("Clear");
		buttons2[1].addActionListener(this);
		buttons2[2].setActionCommand("Register Student");
		buttons2[2].addActionListener(this);
		buttons.add(buttons2[0]);
		buttons.add(buttons2[1]);
		buttons.add(buttons2[2]);
		buttons2[2].setBackground(new Color(0, 200, 0));
		insert_card.add(buttons,constraints);
		
		contentPane.add(insert_card,BorderLayout.CENTER);		
	}
	public static void main(String args[]) throws SQLException//throws IOException
	{
		Student_createGUI();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("Cancel"))
		{
			mainFrame.dispose();
		}
		else if(e.getActionCommand().equalsIgnoreCase("Clear"))
		{
			rollno_text.setText("");
			name_text.setText("");
			uname_text.setText("");
			email_text.setText("");
			phoneno_text.setText("");
			experience_text.setText("");
		}
		else if(e.getActionCommand().equalsIgnoreCase("Register Student"))
		{
			try 
			{
				if(verify())
				{
					PreparedStatement preparedstmt = connection.prepareStatement(insertQuery);
					preparedstmt.setInt(1, Integer.parseInt(rollno_text.getText()));
					preparedstmt.setString(2, name_text.getText());
					preparedstmt.setString(3, uname_text.getText());
					preparedstmt.setString(4, email_text.getText());
					preparedstmt.setInt(5, Integer.parseInt(phoneno_text.getText()));
					preparedstmt.setString(6, grpnames.getSelectedItem().toString());
					preparedstmt.setInt(7, Integer.parseInt(experience_text.getText()));
					preparedstmt.executeUpdate();
					dialogBox("Data inserted!!!");
					preparedstmt.close();
					connection.close();
				}
				else
					dialogBox("Enter correct data!!");					
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}				
		}		
	}
}
