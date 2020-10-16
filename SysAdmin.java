package Assignment4;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.sql.*;

public class SysAdmin implements SQL_Detail,ItemListener,ActionListener
{
	
	//MySQl
	private static Connection connection;
	private static String items[] = {"Choose any one....","Insert","Edit","Display","Delete","Return"};
	private static String outputQuery = "select * from studentdetails";
	public JPanel cards;
	String[] db_columns = {"UserId","Roll_No","Name","User_Name","Email_Id","Phone_No","Group-Name","Experience"};
	static JFrame frame;
	JComboBox<String> dropbox,grpnames;
	private String UserId,Name,User_Name,Email_Id,Group_Name;
	private int Roll_No,Phone_No,Experience;
	private JFrame display_frame;
	
	// Insert Card variables
	private JLabel rollno_label,name_label;
	private JLabel uname_label, email_label, phoneno_label, grp_label, experience_label;
	private JTextField rollno_text,name_text;
	private JTextField uname_text, email_text, phoneno_text, experience_text;
	Button[] buttons2 = {new Button("Cancel"),new Button("Clear"),new Button("Register")};
	
	// Edit card variables
	private JRadioButton new_name,new_uname;
	private JTextField edit_name_text;
	
	// Delete Card variables
	private JLabel del_name;
	private JTextField del_text;
	private JButton delButton;
	
	public static void main(String args[])
	{
		createAndShowGUI();
	}
	private static void createAndShowGUI() 
	{
		frame = new JFrame("SysAdmin");
		frame.setBounds(340, 150, 600, 450);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE | JFrame.DISPOSE_ON_CLOSE);
		
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
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		SysAdmin sysAdmin = new SysAdmin();
		sysAdmin.addCoponentToPane(frame.getContentPane());
		frame.setVisible(true);
	}

	private void addCoponentToPane(Container contentPane) 
	{
		JPanel comboBoxPanel = new JPanel();
		dropbox = new JComboBox<String>(items);
		dropbox.setEditable(false);
		dropbox.addItemListener(this);
		comboBoxPanel.add(dropbox);
		
		// Select option card (Card 1)
		JPanel select_Panel = new JPanel();
		select_Panel.add(new JLabel("Select one option!!"));
		
		// Insert (Card 2)
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
		buttons2[1].setActionCommand("Clear");
		buttons2[2].setActionCommand("Register Student");
		buttons.add(buttons2[0]);
		buttons.add(buttons2[1]);
		buttons.add(buttons2[2]);
		buttons2[2].setBackground(new Color(0, 200, 0));
		insert_card.add(buttons,constraints);
		
		// Edit (Card 3)
		JPanel edit_card = new JPanel();
		new_name = new JRadioButton("Change Name",true);
		new_name.setIconTextGap(5);
		new_name.addItemListener(this);
		new_uname = new JRadioButton("Change UserName");
		new_uname.setIconTextGap(5);
		new_uname.addItemListener(this);
		edit_name_text = new JTextField(20);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(new_name);
		buttonGroup.add(new_uname);
		edit_card.add(new_name);
		edit_card.add(new_uname);
		edit_card.add(edit_name_text);
		
		
		
		
		// Display (Card 4)
		JPanel display_card = new JPanel();
		
		// Delete (Card 5)
		JPanel delete_card = new JPanel(null);
		del_name = new JLabel("Enter name to be deleted:");
		del_name.setBounds(80, 50, 180, 20);
		del_text = new JTextField(20);
		del_text.setBounds(240, 50, 200, 20);
		delButton = new JButton("Delete");
		delButton.setBounds(240, 100, 100, 30);
		delButton.addActionListener(this);
		delButton.setActionCommand("Delete Student");
		delete_card.add(del_name);
		delete_card.add(del_text);
		delete_card.add(delButton);
		
		
		// Return card (Card 5)
		JPanel return_Panel = new JPanel(); 
		
		
		
		
		
		cards = new JPanel(new CardLayout());
		cards.add(select_Panel,"Choose any one....");
		cards.add(insert_card,"Insert");
		cards.add(edit_card,"Edit");
		cards.add(display_card,"Display");
		cards.add(delete_card,"Delete");
		cards.add(return_Panel,"Return");
		
		contentPane.add(comboBoxPanel,BorderLayout.NORTH);
		contentPane.add(cards,BorderLayout.CENTER);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("Delete Student"))
		{
			try {
				PreparedStatement pstmt = connection.prepareStatement("delete from studentdetails where Name='?'");
				pstmt.setString(1, del_text.getText().toLowerCase());
				pstmt.executeQuery();
				JOptionPane pane = new JOptionPane("Record Deleted!!!",JOptionPane.INFORMATION_MESSAGE,
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
				pstmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
			final String insertQuery ="insert into studentdetails(Roll_No,Name,User_Name,Email_Id,Phone_No,Group_Name,Experience)"
					+ " values(?,?,?,?,?,?,?)";
			try {
				PreparedStatement preparedstmt = connection.prepareStatement(insertQuery);
				preparedstmt.setInt(1, Integer.parseInt(rollno_text.getText()));
				preparedstmt.setString(2, name_text.getText());
				preparedstmt.setString(3, uname_text.getText());
				preparedstmt.setString(4, email_text.getText());
				preparedstmt.setInt(5, Integer.parseInt(phoneno_text.getText()));
				preparedstmt.setString(6, grpnames.getSelectedItem().toString());
				preparedstmt.setInt(7, Integer.parseInt(experience_text.getText()));
				preparedstmt.executeUpdate();
				JOptionPane pane = new JOptionPane("Data inserted",JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.DEFAULT_OPTION,null,new Object[]{},null);
				JDialog dialog = pane.createDialog(null, "message");
				dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
				new Timer(1000,new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dialog.setVisible(false);
					}
				}).start();
				dialog.setVisible(true);
				preparedstmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}				
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		//System.out.println(e.getStateChange());
		if(e.getSource().equals(dropbox)) {
			CardLayout cardLayout = (CardLayout)cards.getLayout();
			cardLayout.show(cards, (String)e.getItem());
		}
		if(e.getStateChange() == ItemEvent.SELECTED) 
		{
			if(e.getItem().equals("Display")) {
				JOptionPane pane = new JOptionPane("Fetching details from DataBase...",JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.DEFAULT_OPTION,null,new Object[]{},null);
				
				JDialog dialog = pane.createDialog(null, "Message");
				dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
				new Timer(2000,new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dialog.setVisible(false);
					}
				}).start();
				dialog.setVisible(true);
				display_frame = new JFrame("Student Details");
				display_frame.setBounds(50, 50, 800, 300);
				DefaultTableModel model = new DefaultTableModel();
				JTable display_table = new JTable();
				model.setColumnIdentifiers(db_columns);
				display_table.setModel(model);
				display_table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
				display_table.setFillsViewportHeight(true);
				JScrollPane scroll = new JScrollPane(display_table);
			    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			    model.addRow(db_columns);
			    try {
			    	Statement stmt = connection.createStatement();
					ResultSet rSet = stmt.executeQuery(outputQuery);
					if(rSet != null) {
						while(rSet.next()) {
							UserId = rSet.getString(1);
							Roll_No = rSet.getInt(2);
							Name = rSet.getString(3);
							User_Name = rSet.getString(4);
							Email_Id = rSet.getString(5);
							Phone_No = rSet.getInt(6);
							Group_Name = rSet.getNString(7);
							Experience = rSet.getInt(8);
							model.addRow(new Object[] {UserId,Roll_No,Name,User_Name,Email_Id,Phone_No,Group_Name,Experience});					
						}
					}
					else {
						JOptionPane pane1 = new JOptionPane("No record found in DataBase!!!",JOptionPane.ERROR_MESSAGE,
								JOptionPane.DEFAULT_OPTION,null,new Object[]{},null);
						
						JDialog dialog1 = pane1.createDialog(null, "Message");
						dialog1.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
						new Timer(2000,new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								dialog1.setVisible(false);
							}
						}).start();
						dialog1.setVisible(true);
					}
					display_frame.setVisible(true);
					stmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				display_frame.add(display_table);
			}
			
			if(e.getItem().equals("Return"))
			{
				frame.dispose();
			}
		}
		
	}
	
}