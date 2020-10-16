package Assignment4;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class Client extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static final int port = 4000;
	private Socket socket = null;
	private BufferedReader server_input;
	private PrintWriter output;
	private String clientName,fromServer,toServer;
	//private Scanner scanner;
	
	
	private JPanel display;
	private JTextField senderText;
	private JTable display_table;
	private DefaultTableModel model;
	private JButton sendButton;
	private Thread thread;
	
	public static void dialogBox(String messge)
	{
		JOptionPane pane = new JOptionPane(messge,JOptionPane.WARNING_MESSAGE,
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
	public class ServerInput extends Thread
	{
		@Override
		public void run() {
			try 
			{
				do 
				{
					fromServer = (String)server_input.readLine();
					System.out.println(fromServer);
					model.addRow(new String[] {fromServer,null});
					display_table.revalidate();
					display_table.repaint();
				}while(fromServer != null && fromServer.length() != 0);
				
			} catch (IOException e) 
			{
				try {
					server_input.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	public Client(String clientName)throws IOException
	{
		this.clientName = clientName;
		this.setTitle("Chat");
		this.setBounds(450, 150, 300, 450);
		this.setResizable(false);
		try {
			Stud_login.dialogBox("Connecting to the Server....");
			socket = new Socket("127.0.0.1", port);
			Stud_login.dialogBox("Connected....");
			this.server_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.output = new PrintWriter(socket.getOutputStream(),true);
		} catch (ConnectException e) {
			dialogBox("Server not connected");
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}catch (Exception e) {
			e.printStackTrace();
		}
		addComponentPane(this.getContentPane());
		this.setVisible(true);
		thread = new Thread(new ServerInput());
		thread.start();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private void addComponentPane(Container contentPane) {
		// Area where msg will be displayed
		display = new JPanel(new BorderLayout());
		this.model = new DefaultTableModel();
		model.setColumnCount(2);
		display_table = new JTable();
		display_table.setModel(model);
		display_table.setTableHeader(null);
		display_table.setPreferredSize(new Dimension(280, 370));
		display_table.setEnabled(false);
		display_table.setRowHeight(25);
		DefaultTableCellRenderer left = new DefaultTableCellRenderer();
		left.setHorizontalAlignment(SwingConstants.LEFT);
		display_table.getColumnModel().getColumn(0).setCellRenderer(left);
		
		DefaultTableCellRenderer right = new DefaultTableCellRenderer();
		right.setHorizontalAlignment(SwingConstants.RIGHT);
		display_table.getColumnModel().getColumn(1).setCellRenderer(right);
		
		display_table.setFillsViewportHeight(true);
		display_table.setPreferredScrollableViewportSize(display_table.getPreferredSize());
		display_table.setShowGrid(false);
		//display_table.setShowHorizontalLines(false);
		//display_table.setShowVerticalLines(false);
		display_table.setBackground(new Color(255, 255, 255));
		JScrollPane scrollPane = new JScrollPane(display_table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		display.add(scrollPane,BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		senderText = new JTextField();
		senderText.setPreferredSize(new Dimension(200, 28));
		senderText.addKeyListener(new KeyListener() {			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub				
			}			
			@Override
			public void keyReleased(KeyEvent e) {
				sendButton.setEnabled(senderText.getText().length()!= 0);
			}			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub			
			}
		});
		sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		sendButton.setEnabled(false);
		panel.add(senderText);
		panel.add(sendButton);
		display.add(panel,BorderLayout.SOUTH);
		
		contentPane.add(scrollPane);
		
	}
	/*public static void main(String args[]) throws IOException {
		new Client(null);
	}*/
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		toServer = senderText.getText();
		//System.out.println("ToServer:" + toServer);
		if(toServer != null)  {
			model.addRow(new String[] {null,toServer});			
			try {
				//System.out.println("You:" + toServer);
				output.println(String.format("%s",toServer));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if(toServer.equalsIgnoreCase("Quit"))
			{
				output.println(clientName + " left....");
				thread.stop();
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				//this.dispose();
			}
			this.senderText.setText("");
			//display_table.revalidate();
			//display_table.repaint();
		}		
	}
}