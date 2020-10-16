package Assignment4;

public class ServerMain {
	
	private static final int port = 4000;
	public static void main(String args[])
	{		
		Server server = new Server(port);
		server.start();
	}
}
