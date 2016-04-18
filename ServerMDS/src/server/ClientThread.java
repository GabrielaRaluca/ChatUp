package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import gui.*;

public class ClientThread extends Thread{

	public ObjectInputStream input;
	public ObjectOutputStream output;
	public Socket clientSocket;
	public ArrayList<ClientThread> threads;
	public String message;
	public static Server server;
	
	public ClientThread(Socket clientSocket, ArrayList<ClientThread> threads, Server server)
	{
		this.clientSocket = clientSocket;
		this.threads = threads;
		this.server = server;
	}
	
	public void run()
	{
		try
		{
			input = new ObjectInputStream(clientSocket.getInputStream());
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			
			synchronized(this)
			{
				for(int i = 0; i < threads.size(); i++)
				{
					if(threads.get(i) != null &&  threads.get(i) != this)
					{
						threads.get(i).output.writeObject("The user " + threads.get(i).clientSocket.getInetAddress().getHostAddress() + " entered the room");
						
					}
				}
			}
			
			//Start the conversation
			while(true)
			{
				message = (String) input.readObject();
				if(message.contains("QUIT"))
					break;
				//if the message is private send it to the given client
				if(message.startsWith("@"))
				{
					String words[] = message.split("\\s+", 2); //split the message into the ip + message
					if(words.length > 1 && words[1] != null)
					{
						words[1] = words[1].trim();
						if(!words[1].isEmpty())
						{
							String ip = words[0].substring(1);
							synchronized(this)
							{
								for(int i = 0; i < threads.size(); i++)
								{
									if(threads.get(i) != null && threads.get(i) != this && threads.get(i).clientSocket.getInetAddress().getHostAddress().equals(ip))
									{
										threads.get(i).output.writeObject(this.clientSocket.getInetAddress().getHostAddress() + " " + words[1]);
										break;
									}
								}
							}
						}
					}
					
				}
				
				else if(message != null)//public message
				{
					synchronized(this)
					{
						for(int i = 0; i < threads.size(); i++)
						{
							if(threads.get(i) != null && threads.get(i) != this)
							{
								threads.get(i).output.writeObject(this.clientSocket.getInetAddress().getHostAddress() + " " + message);
								
							}
						}
					}
				}
			}
			//announce if someone is leaving the chat room
			synchronized(this)
			{
				for(int i = 0; i < threads.size(); i++)
				{
					if(threads.get(i) != null && threads.get(i) != this)
					{
						threads.get(i).output.writeObject("The user " + clientSocket.getInetAddress().getHostAddress() + " is leaving the chat");
						server.showMessage("The user " + clientSocket.getInetAddress().getHostAddress() + " has disconnected");
					}
				}
			}
			
			synchronized(this)
			{
				threads.remove(this);
			}
			close();
		}
		
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}
	
	public void close()
	{
		try{
			input.close();
			output.close();
			clientSocket.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	
}
