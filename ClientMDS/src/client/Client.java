package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import GUI.*;

public class Client
{
	public static Socket clientSocket;
	public static ObjectOutputStream output;
	public static ObjectInputStream input;
	public static boolean closed = false;
	
	public static Conversation convo;
	public String serverIP;
	public int portNumber;
	public static String message;
	

	public Client(String IP, int port)
	{
		this.serverIP = IP;
		this.portNumber = port;
		
	}
	
	public void connect()
	{
		try
		{
			clientSocket = new Socket(serverIP, portNumber);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null, "You cannot connect to the server right now. Please try again later!", "Connection Refused", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void close()
	{
		try
		{
			output.close();
			input.close();
			clientSocket.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	

	public void setUpStreams()
	{
		try
		{
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(clientSocket.getInputStream());
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
