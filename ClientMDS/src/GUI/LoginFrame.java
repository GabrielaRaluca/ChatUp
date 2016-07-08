package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.Font;
import java.awt.Image;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.awt.event.ActionEvent;
import client.*;
import server.SignUpClient;
import systemtrays.LoggedSystemTray;
import systemtrays.NotLoggedSystemTray;

public class LoginFrame extends JFrame implements Runnable{

	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField userInput;
	
	private JLabel lblChatup;
	private JLabel lblWouldYouJust;
	private JLabel lblPassword;
	private JLabel lblUsername;
	private JLabel lblLogo;
	private JLabel lblWrongInfo;
	private JButton btnSignUp;
	private JButton btnLogin;
	
	private final String LOGINID = "login";
	
	public static String getUsername() {
		return username;
	}

	private static String username;
	public static boolean connected;
	private String password;
	String lineReceived;
	SignUpClient receivedObject;
	
	Client client;
	SwingWorker<Void, String> workerReader;
	static FriendsFrame friendsFrame;
	NotLoggedSystemTray notLoggedTray;
	
	private final String OKAY = "okay";
	
	public LoginFrame(Client client)
	{
		this.client = client;
	}
	
	public static FriendsFrame getFriendsFrame()
	{
		return friendsFrame;
	}
	
	public void run()
	{
		initialize();
		setUpHandlers();
	}
	
	public void start()
	{
		initialize();
		setUpHandlers();
	}
	
	public void close()
	{
		WindowEvent winclosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winclosing);
	}

	public void initialize() 
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/resources/ChatUp!.png")));
		setTitle("ChatUp!");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 102));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setVisible(true);
		
		lblChatup = new JLabel("ChatUp!");
		lblChatup.setFont(new Font("MS UI Gothic", Font.BOLD, 60));
		
		URL url = LoginFrame.class.getResource("/resources/ChatUp!.png");
		ImageIcon imageIcon = new ImageIcon(url);
		Image scaledImage = imageIcon.getImage().getScaledInstance(120, 130, java.awt.Image.SCALE_SMOOTH);
		ImageIcon logo = new ImageIcon(scaledImage);
		lblLogo = new JLabel(logo);
		
		lblWouldYouJust = new JLabel("Would you just, please ...");
		lblWouldYouJust.setFont(new Font("MS UI Gothic", Font.ITALIC, 25));
		
		lblWrongInfo = new JLabel("Wrong username/password");
		lblWrongInfo.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		lblWrongInfo.setForeground(Color.RED);
		lblWrongInfo.setVisible(false);
		
		
		passwordField = new JPasswordField();
		
		lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		
		userInput = new JTextField();
		userInput.setColumns(10);
		
		lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		
		btnSignUp = new JButton("Sign Up");
		btnSignUp.setBackground(Color.GRAY);
		btnSignUp.setFont(new Font("MS UI Gothic", Font.BOLD, 20));
		
		btnLogin = new JButton("Login");
		getRootPane().setDefaultButton(btnLogin);
		
		btnLogin.setBackground(Color.GRAY);
		btnLogin.setFont(new Font("MS UI Gothic", Font.BOLD, 20));
		lblWrongInfo = new JLabel("Wrong username/password");
		lblWrongInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblWrongInfo.setFont(new Font("MS UI Gothic", Font.PLAIN, 18));
		lblWrongInfo.setForeground(Color.RED);
		lblWrongInfo.setVisible(false);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(154, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblWouldYouJust, GroupLayout.PREFERRED_SIZE, 296, GroupLayout.PREFERRED_SIZE)
							.addGap(220))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnLogin)
							.addGap(27)
							.addComponent(btnSignUp)
							.addGap(266))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(lblLogo, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
								.addGap(46)
								.addComponent(lblChatup, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
								.addGap(114))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
											.addComponent(lblWrongInfo, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
											.addComponent(userInput))))
								.addGap(207)))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(55)
					.addComponent(lblWouldYouJust, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLogo, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblChatup, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
							.addGap(20)
							.addComponent(lblWrongInfo)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(userInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(21)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLogin)
						.addComponent(btnSignUp))
					.addContainerGap(59, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		notLoggedTray = new NotLoggedSystemTray(client);
	}
	
	public void setUpHandlers()
	{
		//while (connected)
		//{
			loginHandler();
			signUpHandler();
		//}
		//JOptionPane.showMessageDialog(null, "Server connection failed. Please try again later!", "Connection failure", JOptionPane.ERROR_MESSAGE);
		//Client.close();
		//dispose();
		//NotLoggedSystemTray.removeSystemTrayIcon();
	}
	public void loginHandler()
	{
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SwingWorker<Void, String> worker = new SwingWorker<Void, String>()
				{
					@Override
					protected Void doInBackground() throws Exception
					{
						SignUpClient sendObject = new SignUpClient();
						username = userInput.getText();
						sendObject.setUsername(username);
						password = String.valueOf(passwordField.getPassword());
						sendObject.setPassword(password);
						sendObject.setCode(LOGINID);
						Client.output.writeObject(sendObject);
						Client.output.flush();
						return null;
					}
					
					protected void done()
					{
						response();
					}
				};
				worker.execute();
			}	
		});

	}
	
	public void signUpHandler()
	{
		btnSignUp.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				SignUpFrame signUpFrame = new SignUpFrame(client);
				signUpFrame.start();
			}
			
		});
	}
	
	public void response()
	{
		workerReader = new SwingWorker<Void, String>()
		{

			@Override
			protected Void doInBackground() throws Exception 
			{
				// TODO Auto-generated method stub
					try
					{
						synchronized(Client.input)
						{
							receivedObject = new SignUpClient();
							receivedObject = (SignUpClient) Client.input.readObject();
						}
					
					}
					catch(ClassNotFoundException cnfe)
					{
						cnfe.printStackTrace();
						Client.close();
					}
					catch(IOException ioe)
					{
						ioe.printStackTrace();
						JOptionPane.showMessageDialog(null, "Server connection failed. Please try again later!", "Connection failure", JOptionPane.ERROR_MESSAGE);
						Client.close();
						dispose();
						notLoggedTray.removeSystemTrayIcon();
					}
						
				return null;
			}
			
			protected void done()
			{
				if(receivedObject.getCode().equals(OKAY))
				{
					dispose();
					NotLoggedSystemTray.removeSystemTrayIcon();
					friendsFrame = new FriendsFrame(client);
					friendsFrame.setTitle("ChatUp! - " + username);
					friendsFrame.start();
				}
				else
						lblWrongInfo.setVisible(true);
			}
		};
		workerReader.execute();
	}
	
	

}
