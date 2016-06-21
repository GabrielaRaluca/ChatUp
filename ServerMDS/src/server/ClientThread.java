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
	
	public SignUpClient clientObject;
	
	public static Server server;
	
	public String message;
	public String messageName;
	public String name;
	
	public boolean loggedIn = false;

	
	public ClientThread()
	{}
	
	public ClientThread(Socket clientSocket, ArrayList<ClientThread> threads, Vector<ClientThread> connectedUsers, Server server)
	{
		this.clientSocket = clientSocket;
		this.threads = threads;
		this.connectedUsers = connectedUsers;
		ClientThread.server = server;

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
					JDBCHandlers.insertMessages(this.name, destination, message);
				/*	else if(message != null)//public message
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
					}*/
				}
				if(code.equals(Codes.ADDFRIEND))
				{
					ClientThreadHandlers.addNewFriend(this, clientObject);	
					
				}
				
				if(code.equals(Codes.ACCEPT))
				{
					JDBCHandlers.confirmFriendRequest(this.name, clientObject.getUsername());
					ClientThreadHandlers.updateOnlineFriendsList(this, clientObject);
					
					
				}
				if(code.equals(Codes.DECLINE))
				{
					JDBCHandlers.declineFriendRequest(this.name, clientObject.getUsername());
				}
				if(code.equals(Codes.BLOCK))
				{
					ClientThreadHandlers.blockFriend(this, clientObject);
					ClientThread blockedUser = ClientThreadHandlers.getFriendByUsername(clientObject.getUsername(), connectedUsers);
					if(blockedUser != null)//daca era online cel caruai i-am dat block, o sa ii aparem offline
					{
						ClientThreadHandlers.announceBlockedFriendOffline(this, blockedUser);
					}
				}
				if(code.equals(Codes.REMOVEFROMBLOCK))
				{
					String friendsName = clientObject.getUsername();
					
					JDBCHandlers.unblockFriend(this.name, friendsName);
				}
				if(code.equals(Codes.GETMESSAGES))
				{
					SignUpClient response = new SignUpClient();
					String friendsName = clientObject.getUsername();
					response.setCode(Codes.GETMESSAGES);
					response.setUsername(friendsName);
					ArrayList<String> messages = JDBCHandlers.getMessages(this.name, friendsName);
					response.setOldMessages(messages);
					synchronized(this)
					{
						output.reset();
						output.writeObject(response);
						output.flush();
					}
				}
				/*PENTRU BLOCK, SI MESAJE TREBUIE DATE DOAR NUMELE NU SI OBIECTELE!!!!*/
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
				
				if(code.equals(Codes.EXITID))
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
