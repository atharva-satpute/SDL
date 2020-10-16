package Assignment4;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ServerHandle extends Thread
{
	private Socket clientsocket;
	private ObjectOutputStream output;
	private BufferedReader input;
	private Server server;
	private String fromClient,clientName;
	private int count = 1;
	private HashMap<Socket,String> connected_client ;
	
	public ServerHandle(Server server,Socket clientsocket)throws IOException{
		
		this.server = server;
		this.clientsocket = clientsocket;
		this.output = new ObjectOutputStream(clientsocket.getOutputStream());
		this.input = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
		this.connected_client = new HashMap<Socket,String>();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			/*send current user all other logins
			for(ServerHandle obj: server.get_connectedList())
			{
				if((obj.clientsocket != clientsocket) && clientsocket != null )
				{
					String msgString = clientName + " online!!\n";
					obj.send(msgString);
				}
			}*/
			do
			{
				fromClient = input.readLine();
				//System.out.println("In Server");
				//System.out.println("HI");
				//System.out.println(fromClient);
				if(fromClient != null && fromClient.length()!=0)
				{
					int index = fromClient.indexOf(" ");
					if(count == 1)
					{
						this.clientName = fromClient.substring(0,index);
						count--;
					}
					System.out.println("Client Name:" + clientName);
					connected_client.put(clientsocket,clientName);
					System.out.println(fromClient);
					int index1 = fromClient.lastIndexOf(" ");
					// send message to all connected clients
					for(ServerHandle obj: server.get_connectedList())
					{
						if((obj.clientsocket != clientsocket)&& obj.clientsocket!=null)
							obj.send(fromClient);
					}
					if(fromClient.substring(index1+1,fromClient.length()).equalsIgnoreCase("Quit"))
					{
						server.get_connectedList().remove(this);
						break;
					}					
				}
			}while(true);
		} catch (SocketException e) {
			System.out.println(connected_client.get(this.clientsocket) + " disconnected abruptly....!!");
		}catch (IOException e) {
			e.printStackTrace();
		}
		catch (NoSuchElementException e) {
			System.out.println("Quitting!!!");
		}finally {
			try {
				this.clientsocket.close();
				this.input.close();
				this.output.close();
				Thread.currentThread().stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void send(String msg)throws IOException {
		output.writeObject(msg);
	}
}
