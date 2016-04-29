package server;

import gui.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class Server {

	public static ServerSocket serverSocket;
	public static Socket clientSocket;
	public ArrayList<ClientThread> threads = new ArrayList<ClientThread>();
	public ServerFrame frame;
	public final int portNumber = 9999;
	public boolean running;
	
	public Server()
	{
		frame = new ServerFrame();
		frame.setVisible(true);
		frame.stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				running = false;
				try {
					serverSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
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
	
	public void start()
	{
		while(running)
		{
			try{
				clientSocket = serverSocket.accept();
				
				threads.add(new ClientThread(clientSocket, threads, this));
				showMessage("The user " + threads.get(threads.size() - 1).clientSocket.getInetAddress().getHostAddress() + " has connected");
				if(threads.size() >= 1)
					threads.get(threads.size() - 1).start();
				
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	public void showMessage(String m)
	{
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				frame.info.append(m + "\n");
				
			}
			
		});
	}
}
