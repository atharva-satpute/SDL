package Assignment4;

import java.io.* ;
import java.net.*;
import java.util.*;

public class Server extends Thread
{
	private static ArrayList<ServerHandle> connectedList = new ArrayList<>();
	private int serverport;
	private ServerSocket serverSocket;

	public Server(int serverport) {
		this.serverport = serverport;
	}
	
	public List<ServerHandle> get_connectedList()
	{
		return connectedList;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			// creating server socket
			serverSocket = new ServerSocket(serverport);
			
			while(true)
			{
				System.out.println("Waiting for connection....");
				Socket clientsocket = serverSocket.accept();
				System.out.println("Connected to: " + clientsocket);
				ServerHandle handle = new ServerHandle(this,clientsocket);
 				connectedList.add(handle);	
 				System.out.println(handle + "added to the list");
 				handle.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				serverSocket.close();
				Thread.currentThread().stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
