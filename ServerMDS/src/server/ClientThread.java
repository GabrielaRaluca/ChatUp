package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import JDBC.*;

import gui.*;

public class ClientThread extends Thread{

	public ObjectInputStream input;
	public ObjectOutputStream output;
	public Socket clientSocket;
	public ArrayList<ClientThread> threads;
	public String message;
	public SignUpClient clientObject;
	public static Server server;
	public String messageName;
	public String name;
	
	public final String MESSAGEID = "message";
	public final String LOGINID = "login";
	public final String DELIM = "?";
	public final String SIGNUPID = "signup";
	
	public boolean correct;
	
	public ClientThread(Socket clientSocket, ArrayList<ClientThread> threads, Server server)
	{
		this.clientSocket = clientSocket;
		this.threads = threads;
		this.server = server;
		this.correct = false;
	}
	
	public void run()
	{
		try
		{
			input = new ObjectInputStream(clientSocket.getInputStream());
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			
			//wait for the username and password
			/*while(true)
			{
				messageName = (String) input.readObject();
				if(messageName.startsWith(LOGINID))
				{
					int index = messageName.indexOf(DELIM);
					String username = messageName.substring(LOGINID.length(), index);
					String password = messageName.substring(index + 1, messageName.length());
					//this.name = username;
					synchronized(this)
					{
						if(Authentication.verify(username, password) == null)
						{//if we wrote the username and the password correctly, get out of while and set the name
							this.name = username;
							break;
						}
						else
						{//if username and password are wrong, send message to let him know
							output.writeObject("Wrong");
							output.flush();
						}
					}
					
				}
				
			}*/
			ClientThreadHandlers.authenticationMessage(clientObject, this);
			
			//Start the conversation
			while(true)
			{
				clientObject = (SignUpClient) input.readObject();
				message = clientObject.getMessage();
				String code = clientObject.getCode();
				/*message = (String) input.readObject();*/
				if(message.contains("QUIT"))
					break;
				
				//if we have to send a message
				if(code.equals(MESSAGEID))
				{
					
					
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
								if(threads.get(i).output != null && threads.get(i) != this)
								{
									threads.get(i).output.writeObject(this.name + ": " + message);
									
								}
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
						threads.get(i).output.writeObject("The user " + name + " is leaving the chat");
						//server.showMessage("The user " + clientSocket.getInetAddress().getHostAddress() + " has disconnected");
					}
				}
			}
			
			synchronized(this)
			{
				threads.remove(this);
				server.showMessage("The user " + clientSocket.getInetAddress().getHostAddress() + " has disconnected");
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
