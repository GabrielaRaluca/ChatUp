package gui;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;


public class ServerFrame extends JFrame {

	private JPanel contentPane;
	public JScrollPane scrollPane;
	public JButton stopButton;
	public static JTextArea info;


	public ServerFrame(Server server) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 509);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		scrollPane = new JScrollPane();
		
		
		stopButton = new JButton("STOP");
		stopButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(server.running)
				{
					server.running = false;
					server.getThreads().clear();
					server.getConnectedUsers().clear();
					server.getThreadPool().shutdown();
					int i;
					try {
						//Server.clientSocket.close();
						for(i = 0; i < server.getThreads().size(); i++)
						{
							server.getThreads().get(i).close();
						}
						Server.serverSocket.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				
			}
			
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
					.addGap(2))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(153)
					.addComponent(stopButton, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
					.addGap(9)
					.addComponent(stopButton))
		);
		
		info = new JTextArea();
		info.setWrapStyleWord(true);
		info.setLineWrap(true);
		info.setEditable(false);
		scrollPane.setViewportView(info);
		contentPane.setLayout(gl_contentPane);
	}
}
