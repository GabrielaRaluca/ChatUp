package server;

import gui.*;


import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

public class Server {

	public static ServerSocket serverSocket;
	public static Socket clientSocket;
	private ExecutorService threadPool;
	public ArrayList<ClientThread> threads = new ArrayList<ClientThread>();
	private static Vector<ClientThread> connectedUsers = new Vector<ClientThread>();
	public static ServerFrame frame;
	public final int portNumber = 9999;
	public volatile boolean  running;
	
	public Server()
	{
		frame = new ServerFrame(this);
		frame.setVisible(true);
		threadPool = Executors.newCachedThreadPool();
		running = true;
		
		try
		{
			serverSocket = new ServerSocket(portNumber);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public ExecutorService getThreadPool()
	{
		return threadPool;
	}
	
	public Vector<ClientThread> getConnectedUsers()
	{
		return connectedUsers;
	}
	public ArrayList<ClientThread> getThreads()
	{
		return this.threads;
	}
	public void start()
	{
		while(running)
		{
			try{
				clientSocket = serverSocket.accept();
				
				threads.add(new ClientThread(clientSocket, threads, connectedUsers, this));
				showMessage("The user " + threads.get(threads.size() - 1).clientSocket.getInetAddress().getHostAddress() + " has connected");
				if(threads.size() >= 1)
					threadPool.execute(threads.get(threads.size() - 1));
					//threads.get(threads.size() - 1).start();
				
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	public static void addConnectedUser(ClientThread newConnectedUser)
	{
		connectedUsers.add(newConnectedUser);
	}
	public static void showMessage(String m)
	{
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				ServerFrame.info.append(m + "\n");
				
			}
			
		});
	}
}
