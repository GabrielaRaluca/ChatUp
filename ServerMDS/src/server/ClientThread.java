package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import JDBC.*;



public class ClientThread implements Runnable{

	public ObjectInputStream input;
	public ObjectOutputStream output;
	public Socket clientSocket;
	public ArrayList<ClientThread> threads;
	protected Vector<ClientThread> connectedUsers;
	public ArrayList<ClientThread> friends; //used to send that i'm online to all my friends
	protected ArrayList<String> friendsNames;
	public String message;
	public SignUpClient clientObject;
	public static Server server;
	public String messageName;
	public String name;
	
	public final String MESSAGEID = "message";
	public final String LOGINID = "login";
	public final String DELIM = "?";
	public final String SIGNUPID = "signup";
	private final String NEWUSER = "newUser";
	private final String EXITID = "exit";
	
	public boolean correct;
	public boolean loggedIn = false;
	public boolean hasSentFriendsList = false;
	
	public ClientThread(Socket clientSocket, ArrayList<ClientThread> threads, Vector<ClientThread> connectedUsers, Server server)
	{
		this.clientSocket = clientSocket;
		this.threads = threads;
		this.connectedUsers = connectedUsers;
		ClientThread.server = server;
		this.correct = false;
	}
	
	public void run()
	{
		try
		{
			input = new ObjectInputStream(clientSocket.getInputStream());
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			
			
			//Start the conversation
			while(true)
			{
				
				synchronized(input)
				{	
					clientObject = (SignUpClient) input.readObject();
				}
				
				
				message = clientObject.getMessage();
				String code = clientObject.getCode();
				
				if(!loggedIn)
				{
					ClientThreadHandlers.authenticationMessage(clientObject, this);
					
					if(loggedIn)
					{	//send the list of friends only when you have just logged in, and announce friends that you are online
						ClientThreadHandlers.sendFriendListWhenJustEntered(this);
						ClientThreadHandlers.announceFriendsWhenOnline(this);
					}
					
				}
				
					
				//if we have to send a message
				if(code.equals(Codes.MESSAGEID))
				{
					
					String destination = clientObject.getDestinationUsername();
					//if the message is private send it to the given client
					if(destination != null)
					{
						
						synchronized(this)
						{
							for(int i = 0; i < connectedUsers.size(); i++)
							{
								if(connectedUsers.get(i) != null && connectedUsers.get(i) != this && connectedUsers.get(i).name.equals(destination))
								{
									SignUpClient objectToSend = new SignUpClient();
									objectToSend.setCode(Codes.MESSAGEID);
									objectToSend.setUsername(this.name);
								
									objectToSend.setDestinationUsername(destination);
									objectToSend.setMessage(this.name + ": " + message);
									connectedUsers.get(i).output.reset();
									connectedUsers.get(i).output.writeObject(objectToSend);
									connectedUsers.get(i).output.flush(); 
									
									break;
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
									threads.get(i).output.reset();
									threads.get(i).output.writeObject(this.name + ": " + message);
									System.out.println("s");
									threads.get(i).output.flush();
									
								}
							}
						}
					}
				}
				
				if (code.equals(Codes.LOGGEDOUT)) {
					// check the list with the names of the friends again 
					// Look through the connected users list and see which users are
					// the ones with the usernames given from the
					// list of friends and then send a message to them so that they
					// know that you are OFFLINE now ( because u have logged out)
				 	
				 	ClientThreadHandlers.announceFriendsWhenOffline(this);
				 	synchronized(this)
				 	{
				 		connectedUsers.remove(this);
						Server.showMessage("THE USER " + this.name + " HAS LOGGED OUT");
				 	}
					
				}
				
				if(code.equals(EXITID))
				{
					break;
				}
			
			}
			
			
			// check the list with the names of the friends again
			// Look through the connected users list and see which users are
			// the ones with the usernames given from the
			// list of friends and then send a message to them so that they
			// know that you are OFFLINE now ( because u have exited)
			
			ClientThreadHandlers.announceFriendsWhenExited(this);
			synchronized(this)
			{
				threads.remove(this);
				connectedUsers.remove(this);
				
				Server.showMessage("The user " + clientSocket.getInetAddress().getHostAddress() + " has disconnected");
				
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
