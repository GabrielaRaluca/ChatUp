package client;

import javax.swing.SwingUtilities;
import GUI.*;
import client.*;

public class Main 
{

	private final static String SERVERIP = "192.168.0.13";
	private final static int PORT = 9999;
	
	static Client client;
	
	public static void main(String[] args) 
	{
		client = new Client(SERVERIP, PORT);
		SwingUtilities.invokeLater(new LoginFrame(client));
	}

}
