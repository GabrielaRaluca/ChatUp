package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
		try
		{
			clientSocket = new Socket(serverIP, portNumber);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
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
	
	public void showMessage(final String m)
	{
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						convo.messages.append(m);
					}
				}
		);
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
