package Assignment4;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;


public class Existing_Student extends JFrame implements SQL_Detail,ItemListener,ActionListener
{
	private static final long serialVersionUID = 1L;
	static JFrame mainFrame;
	JComboBox<String> dropbox;
	private static String items[] = {"Choose any one....","Direct Message","Group Message","Return"};
	private Connection connection;
	private String clientName;
	public JPanel cards;
	
	//DM variables
	private JLabel name_label;
	private JTextField name_text;
	private JButton chatButton,returnButton;
	
	/*public static boolean isServerRunning()throws IOException
	{
		boolean isRunning = false;
		SocketAddress socketAddress = new InetSocketAddress("127.0.0.1",4000);
		Socket trialSocket = new Socket();
		try {
			trialSocket.connect(socketAddress);
			isRunning = true;
		} catch (IOException e) {
			System.out.println("Server already running!!!!");
		}
		trialSocket.close();
		return isRunning;
	}*/
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
	public Existing_Student(Connection connection,String clientName) {
		this.connection = connection;
		this.clientName = clientName;
		this.setTitle("Welcome");
		this.setBounds(340, 150, 600, 450);
		addComponentPane(this.getContentPane());
		this.setVisible(true);		
	}
	private void addComponentPane(Container contentPane) {
		JPanel comboBoxPanel = new JPanel();
		dropbox = new JComboBox<String>(items);
		dropbox.setEditable(false);
		dropbox.addItemListener(this);
		comboBoxPanel.add(dropbox);
		
		// Select option card (Card 1)
		JPanel select_Panel = new JPanel();
		select_Panel.add(new JLabel("Select one option!!"));
		
		
		// Direct Message Card (Card 2)
		JPanel directmsg_cardJPanel = new JPanel();
		directmsg_cardJPanel.setBorder(new EmptyBorder(10, 40, 280, 40));
		//directmsg_cardJPanel.setBounds(380, 200, 100, 50);
		GridLayout gridLayout = new GridLayout(2,2);
		gridLayout.setHgap(10);
		gridLayout.setVgap(10);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 50, 50);
		directmsg_cardJPanel.setLayout(gridLayout);
		
		// DM
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		name_label = new JLabel("Enter Name of the Student to be connected:");
		directmsg_cardJPanel.add(name_label,constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 2;
		constraints.gridy = 0;
		name_text = new JTextField(20);
		directmsg_cardJPanel.add(name_text,constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		chatButton = new JButton("Chat");
		chatButton.setActionCommand("Message");
		chatButton.addActionListener(this);
		directmsg_cardJPanel.add(chatButton,constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		returnButton = new JButton("Return");
		returnButton.setActionCommand("Return");
		returnButton.addActionListener(this);
		// Group Chat (Card 3)
		JPanel groupChat = new JPanel();
		
		cards = new JPanel(new CardLayout());
		cards.add(select_Panel,"Choose any one....");
		cards.add(directmsg_cardJPanel,"Direct Message");
		contentPane.add(comboBoxPanel,BorderLayout.NORTH);
		contentPane.add(cards,BorderLayout.CENTER);
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource().equals(dropbox)) {
			CardLayout cardLayout = (CardLayout)cards.getLayout();
			cardLayout.show(cards, (String)e.getItem());
		}
		if(e.getStateChange() == ItemEvent.SELECTED)
		{
			if(e.getItem().equals("Return")){
				dialogBox("Logging Out....");
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("Message"))
		{
			PreparedStatement pstmt;
			try {
				pstmt = connection.prepareStatement("select exists(select * from studentdetails where Name=?)");
				pstmt.setString(1, name_text.getText());
				ResultSet rSet = pstmt.executeQuery();
				if(rSet != null && rSet.next())
				{
					if(rSet.getInt(1)==1)
					{
						Stud_login.dialogBox(name_text.getText() + " found!!");
						new Client(clientName);
					}
				}
				else {
					Stud_login.dialogBox("Record not found!!!!!");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		if(e.getActionCommand().equalsIgnoreCase("Return"))
		{
			
		}
		
	}	
}
