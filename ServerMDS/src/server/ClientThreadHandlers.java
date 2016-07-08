package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import JDBC.JDBCHandlers;


public class ClientThreadHandlers {
	private ClientThreadHandlers()
	{}
	
	public static void sendFriendListWhenJustEntered(ClientThread client)
	{ //when the client has just connected this function will send him the list of his online and offline friends
		synchronized (client) {
			SignUpClient objectToSend = new SignUpClient();
			ArrayList<String> online = new ArrayList<String>();
			ArrayList<String> offline = new ArrayList<String>();
			ArrayList<String> pending = new ArrayList<String>();
			ArrayList<String> blockedFriends = new ArrayList<String>();
			int i;

			// create an object with 2 lists: the online friends and the
			// offline friends
			// look through the connected users list and see which of them
			// are already connected -> they are online friends
			// if they are not in that list, it means they are offline, so
			// add them to that list, then send the object

			client.friendsNames = JDBCHandlers.getFriends(client.name);
			pending = JDBCHandlers.getPendingRequests(client.name);
			blockedFriends = JDBCHandlers.getBlockedFriends(client.name);
			
			for (i = 0; i < client.friendsNames.size(); i++) {
				if (ClientThreadHandlers.isOnline(client.friendsNames.get(i), client.connectedUsers)) {
					online.add(client.friendsNames.get(i));
				} else {
					offline.add(client.friendsNames.get(i));
				}
			}
			objectToSend.setBlockedFriends(blockedFriends);
			objectToSend.setPending(pending);
			objectToSend.setOnlineFriends(online);
			objectToSend.setOfflineFriends(offline);
			try {
				client.output.reset();
				client.output.writeObject(objectToSend);
				
				client.output.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void announceFriendsWhenOnline(ClientThread client)
	{
		synchronized (client) {
			int i;
			ClientThread friend;
			// check the list with the names of the friends again
			// look through the connected users list and see which users are
			// the ones with the usernames given from the
			// list of friends and then send a message to them so that they
			// know that you are online now

			client.friends = new ArrayList<ClientThread>();
			client.friendsNames = JDBCHandlers.getFriends(client.name);

			for (i = 0; i < client.friendsNames.size(); i++) {
				friend = ClientThreadHandlers.getFriendByUsername(client.friendsNames.get(i), client.connectedUsers);
				if(friend != null)
					client.friends.add(friend);
			}
			SignUpClient objectToSend = new SignUpClient();
			objectToSend.setCode(Codes.NEWUSER);
			objectToSend.setUsername(client.name);
			for (i = 0; i < client.friends.size(); i++) {
				if (client.friends.get(i) != null)
					try {
						synchronized(client.friends.get(i).output)
						{
							client.friends.get(i).output.reset();
							client.friends.get(i).output.writeObject(objectToSend);
							client.friends.get(i).output.flush();
						}
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}
	
	public static void announceFriendsWhenOffline(ClientThread client)
	{
		synchronized(client)
	 	{
	 		client.loggedIn = false;
	 		int i;
			ClientThread friend;
			

			client.friends = new ArrayList<ClientThread>();
			client.friendsNames = JDBCHandlers.getFriends(client.name);

			for (i = 0; i < client.friendsNames.size(); i++) {
				friend = ClientThreadHandlers.getFriendByUsername(client.friendsNames.get(i), client.connectedUsers);
				if(friend != null)
					client.friends.add(friend);
			}
			SignUpClient objectToSend = new SignUpClient();
			objectToSend.setCode(Codes.SIGNEDOUT);
			objectToSend.setUsername(client.name);
			for (i = 0; i < client.friends.size(); i++) 
			{
				if (client.friends.get(i) != null)
					{
						try {
							synchronized(client.friends.get(i).output)
							{
								client.friends.get(i).output.reset();
								client.friends.get(i).output.writeObject(objectToSend);
								client.friends.get(i).output.flush();
							}
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					
					}
					
			}
			
			SignUpClient confirmation = new SignUpClient();
			confirmation.setCode(Codes.LEAVE);
			try {
				client.output.reset();
				client.output.writeObject(confirmation);
				client.output.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
	 	}
	 	
	}
	
	public static void announceFriendsWhenExited(ClientThread client)
	{
		synchronized(client)
	 	{
	 		client.loggedIn = false;
	 		int i;
			ClientThread friend;
			

			client.friends = new ArrayList<ClientThread>();
			client.friendsNames = JDBCHandlers.getFriends(client.name);

			for (i = 0; i < client.friendsNames.size(); i++) {
				friend = ClientThreadHandlers.getFriendByUsername(client.friendsNames.get(i), client.connectedUsers);
				if(friend != null)
					client.friends.add(friend);
			}
			SignUpClient objectToSend = new SignUpClient();
			objectToSend.setCode(Codes.SIGNEDOUT);
			objectToSend.setUsername(client.name);
			for (i = 0; i < client.friends.size(); i++) 
			{
				if (client.friends.get(i) != null)
					{
						try {
							client.friends.get(i).output.reset();
							client.friends.get(i).output.writeObject(objectToSend);
							client.friends.get(i).output.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						
					}
					
			}

	 	}
	 	
	}
	
	public static void addNewFriend(ClientThread client, SignUpClient newFriend)
	{
		String myName = client.name;
		String friendsName = newFriend.getUsername();
		ClientThread friendUser = new ClientThread();
		

		synchronized(client)
		{
			String result = JDBCHandlers.addFriend(myName, friendsName);
			boolean announceFriend = false;
			SignUpClient confirmation = new SignUpClient();
			SignUpClient confirmationForNewFriend = new SignUpClient();
			if(result == null)
			{
				confirmation.setCode(Codes.REQUESTSENT);
				announceFriend = true;
			}
			else if(result.equals("Username does not exist in the database"))
			{
				
				confirmation.setCode(Codes.INVALID);
				
			}
			else
			{
				confirmation.setCode(Codes.ALREADYFRIENDS);
			}
			try {
				client.output.reset();
				client.output.writeObject(confirmation);
				client.output.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//trimit si celui care a primit cererea de prietenie ca are o cerere noua, daca e online
			if(announceFriend)
			{
				friendUser = getFriendByUsername(friendsName, client.connectedUsers);
				if(friendUser != null)
				{
					confirmationForNewFriend.setCode(Codes.NEWPENDINGUSER);
					confirmationForNewFriend.setUsername(myName);
					try {
						synchronized(friendUser.output)
						{
							friendUser.output.reset();
							friendUser.output.writeObject(confirmationForNewFriend);
							friendUser.output.flush();
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
			
		}
		
		//confirmation.setCode();
		
	}
	public static void authenticationMessage(SignUpClient clientObject, ClientThread client) throws IOException, ClassNotFoundException
	{
	
			String code = clientObject.getCode();
			SignUpClient response = new SignUpClient();
			
			if(code.equals(Codes.SIGNUPID))
			{
				String username = clientObject.getUsername();
				String password = clientObject.getPassword();
				String email = clientObject.getEmail();
				String retypedPassword = clientObject.getRetypedPassword();
				boolean send = true;
				
				synchronized(client)
				{
					if(!password.equals(retypedPassword))
						{
							response.setCode("Password mismatch");
							client.output.reset();
							client.output.writeObject(response);
							client.output.flush();
							send = false;
						}
					
					for(int i = 0; i < username.length(); i++)
					{
						if(!Character.isDigit(username.charAt(i)) && !Character.isLetter(username.charAt(i)))
							{
								response.setCode("Invalid username");
								client.output.reset();
								client.output.writeObject(response);
								client.output.flush();
								send = false;
							}
					}
					if(send)
					{
						String result = JDBCHandlers.signUp(username, email, password);
						if(result == null)
						{
							response.setCode("okay");
							client.output.reset();
							client.output.writeObject(response);
							client.output.flush();
							
							
						}
						else
						{
							response.setCode(result);
							client.output.reset();
							client.output.writeObject(response);
							client.output.flush();
							
						}
					}
					
					
				}
				
			}
			
			if(code.equals(Codes.LOGINID))
			{
				
				String username = clientObject.getUsername();
				String password = clientObject.getPassword();
				
				synchronized(client)
				{
					if(JDBCHandlers.authentication(username, password) == null)
						//CHANGE THIS!!!
					{//if we wrote the username and the password correctly, get out of while and set the name
						client.name = username;
						client.loggedIn = true;
						
						Server.showMessage(username + " " + password);
						Server.addConnectedUser(client);
						Server.showMessage("THE USER " + username + " HAS LOGGED IN");
						response.setCode("okay");
						client.output.reset();
						client.output.writeObject(response);//send confirmation message only when got out while
						client.output.flush();
						
					}
					else
					{//if username and password are wrong, send message to let him know
						response.setCode("wrong");
						client.output.reset();
						client.output.writeObject(response);
						client.output.flush();
					
					}
				}
			}
				
		}
	
		public static void updateOnlineFriendsList(ClientThread client, SignUpClient newFriend)
		{
			synchronized(client)
			{
				String me = client.name;
				String friend = newFriend.getUsername();
				ClientThread friendUser = new ClientThread();
				SignUpClient confirmation = new SignUpClient();
				SignUpClient confirmationForNewFriend = new SignUpClient();
				//verificam daca prietenul caruia i-am dat accept e online si daca da, anuntam ca e online, si il anuntam
				//si pe el ca sunt online
				friendUser = getFriendByUsername(friend, client.connectedUsers);
				if(isOnline(friend, client.connectedUsers)) //doar daca e online cel nou ii anuntam 
				{
					confirmation.setCode(Codes.NEWUSER);
					confirmation.setUsername(friend);
					confirmationForNewFriend.setCode(Codes.NEWUSER);
					confirmationForNewFriend.setUsername(me);
					try {
						client.output.reset();
						client.output.writeObject(confirmation);
						client.output.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						synchronized(friendUser)
						{
							friendUser.output.reset();
							friendUser.output.writeObject(confirmationForNewFriend);
							friendUser.output.flush();
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			
				}
			}
		
		}
		public static void blockFriend(ClientThread client, SignUpClient friendToBlock)
		{
			String clientUsername = client.name;
			String friendUsername = friendToBlock.getUsername();
			SignUpClient response = new SignUpClient();
			synchronized(client)
			{
				String result = JDBCHandlers.blockFriend(clientUsername, friendUsername);
				if(result != null)
					response.setCode(Codes.INVALIDFORBLOCK);
				else
				{
					response.setCode(Codes.BLOCKEDUSER);
					response.setUsername(friendUsername);
				}
					
				try {
					client.output.reset();
					client.output.writeObject(response);
					client.output.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		public static void announceBlockedFriendOffline(ClientThread client, ClientThread blockedFriend)
		{//daca am dat block cuiva, il anuntam pe acela ca suntem offline, dupa ce va intra din nou oricum nu o sa
			//ii mai aparem in lista
			SignUpClient objectToSend = new SignUpClient();
			objectToSend.setCode(Codes.SIGNEDOUT);
			objectToSend.setUsername(client.name);
			synchronized(client)
			{
				try
				{	synchronized(blockedFriend.output)
					{
						blockedFriend.output.reset();
						blockedFriend.output.writeObject(objectToSend);
						blockedFriend.output.flush();
					}
					
				}
				 catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
		public static ClientThread getFriendByUsername(String username, Vector<ClientThread> connectedUsers)
		{ //returns the user that has the username = username
			int i;
			for(i = 0; i < connectedUsers.size(); i++)
			{
				if(connectedUsers.get(i).name.equals(username))
					return connectedUsers.get(i);
			}
			return null;
		}
		
		public static boolean isOnline(String username, Vector<ClientThread> connectedUsers)
		{
			int i;
			for(i = 0; i < connectedUsers.size(); i++)
			{
				if(connectedUsers.get(i).name.equals(username))
					return true;
			}
			
			return false;
		}
	}
