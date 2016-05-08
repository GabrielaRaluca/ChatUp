package server;

import java.io.IOException;

import JDBC.Authentication;
import JDBC.SignUp;

public class ClientThreadHandlers {
	private ClientThreadHandlers()
	{}
	
	public static void authenticationMessage(SignUpClient clientObject, ClientThread client) throws IOException, ClassNotFoundException
	{
		while(true)
		{
			synchronized(client.input)
			{
				clientObject = (SignUpClient) client.input.readObject();
			}
				
			String code = clientObject.getCode();
			
			if(code.equals(client.SIGNUPID))
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
							client.output.writeObject("Password mismatch");
							send = false;
						}
					
					for(int i = 0; i < username.length(); i++)
					{
						if(!Character.isDigit(username.charAt(i)) && !Character.isLetter(username.charAt(i)))
							{
								client.output.writeObject("Invalid username");
								send = false;
							}
					}
					if(send)
					{
						String result = SignUp.verify(username, email, password);
						if(result == null)
						{
							client.output.writeObject("okay");
						}
						else
						{
							client.output.writeObject(result);
						}
					}
					
					
				}
				
			}
			
			if(code.equals(client.LOGINID))
			{
				
				String username = clientObject.getUsername();
				String password = clientObject.getPassword();
				//this.name = username;
				synchronized(client)
				{
					if(Authentication.verify(username, password) == null)
					{//if we wrote the username and the password correctly, get out of while and set the name
						client.name = username;
						break;
					}
					else
					{//if username and password are wrong, send message to let him know
						client.output.writeObject("Wrong");
						client.output.flush();
					}
				}
			}
				
		}
		client.output.writeObject("okay");//send confirmation message only when got out while
		client.output.flush();
	}
	}
