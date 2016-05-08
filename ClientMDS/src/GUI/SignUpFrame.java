package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.Client;
import server.SignUpClient;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;

public class SignUpFrame extends JFrame 
{

	private JPanel contentPane;
	private JPanel panel;
	private JPanel panelErrorUsername;
	private JPanel panelUsername;
	private JPanel panelPassword;
	private JPanel panelConfirm;
	private JPanel panelEmail;
	
	private JLabel lblLogo;
	private JLabel lblSignUp;
	private JLabel lblErrorUsername1;
	private JLabel lblErrorUsername2;
	private JLabel lblUsername;
	private JLabel lblErrorPassword;
	private JLabel lblPassword;
	private JLabel lblConfirm;
	private JLabel lblErrorEmail;
	private JLabel lblEmail;
	
	private JTextField usernameField;
	private JTextField emailField;
	
	private JPasswordField passwordField1;
	private JPasswordField passwordField2;
	
	private JButton btnSignUp;
	
	Client client;
	SwingWorker<Void, String> workerReader;
	String lineReceived;
	private final String SIGNUPID = "signup";

	public SignUpFrame(Client client)
	{
		this.client = client;
	}
	
	public void close()
	{
		WindowEvent winclosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winclosing);
		
	}
	
	public void start()
	{
		initialize();
		signUpHandler();
	}
	
	public void initialize()
	{
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 550);
		setMinimumSize(new Dimension(350, 550));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(255, 255, 102));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		panel = new JPanel();
		panel.setBackground(new Color(255, 255, 102));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		
		URL url = LoginFrame.class.getResource("/resources/ChatUp!.png");
		ImageIcon imageIcon = new ImageIcon(url);
		Image scaledImage = imageIcon.getImage().getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
		ImageIcon logo = new ImageIcon(scaledImage);
		lblLogo = new JLabel(logo);
		panel.add(lblLogo);
		
		lblSignUp = new JLabel("Sign Up to ChatUp!");
		lblSignUp.setFont(new Font("MS UI Gothic", Font.BOLD, 20));
		panel.add(lblSignUp);
		
		panelErrorUsername = new JPanel();
		panelErrorUsername.setBackground(new Color(255, 255, 102));
		GridBagConstraints gbc_panelErrorUsername = new GridBagConstraints();
		gbc_panelErrorUsername.insets = new Insets(0, 0, 5, 5);
		gbc_panelErrorUsername.fill = GridBagConstraints.BOTH;
		gbc_panelErrorUsername.gridx = 1;
		gbc_panelErrorUsername.gridy = 1;
		contentPane.add(panelErrorUsername, gbc_panelErrorUsername);
		
		lblErrorUsername1 = new JLabel("The username you have entered is taken or invalid.");
		lblErrorUsername1.setForeground(Color.RED);
		lblErrorUsername1.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
		panelErrorUsername.add(lblErrorUsername1);
		
		lblErrorUsername2 = new JLabel("Enter a username containing only alphanumeric characters.");
		lblErrorUsername2.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
		lblErrorUsername2.setForeground(Color.RED);
		panelErrorUsername.add(lblErrorUsername2);
		
		panelUsername = new JPanel();
		panelUsername.setBackground(new Color(255, 255, 102));
		GridBagConstraints gbc_panelUsername = new GridBagConstraints();
		gbc_panelUsername.insets = new Insets(0, 0, 5, 5);
		gbc_panelUsername.fill = GridBagConstraints.BOTH;
		gbc_panelUsername.gridx = 1;
		gbc_panelUsername.gridy = 2;
		contentPane.add(panelUsername, gbc_panelUsername);
		
		lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("MS UI Gothic", Font.PLAIN, 16));
		panelUsername.add(lblUsername);
		
		usernameField = new JTextField();
		usernameField.setFont(new Font("MS UI Gothic", Font.PLAIN, 16));
		panelUsername.add(usernameField);
		usernameField.setColumns(20);
		
		lblErrorPassword = new JLabel("Passwords did not match.");
		lblErrorPassword.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
		lblErrorPassword.setForeground(Color.RED);
		GridBagConstraints gbc_lblErrorPassword = new GridBagConstraints();
		gbc_lblErrorPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblErrorPassword.gridx = 1;
		gbc_lblErrorPassword.gridy = 3;
		contentPane.add(lblErrorPassword, gbc_lblErrorPassword);
		
		panelPassword = new JPanel();
		panelPassword.setBackground(new Color(255, 255, 102));
		GridBagConstraints gbc_panelPassword = new GridBagConstraints();
		gbc_panelPassword.insets = new Insets(0, 0, 5, 5);
		gbc_panelPassword.fill = GridBagConstraints.BOTH;
		gbc_panelPassword.gridx = 1;
		gbc_panelPassword.gridy = 4;
		contentPane.add(panelPassword, gbc_panelPassword);
		
		lblPassword = new JLabel("Password: ");
		lblPassword.setFont(new Font("MS UI Gothic", Font.PLAIN, 16));
		panelPassword.add(lblPassword);
		
		passwordField1 = new JPasswordField();
		passwordField1.setFont(new Font("MS UI Gothic", Font.PLAIN, 16));
		panelPassword.add(passwordField1);
		passwordField1.setColumns(20);
		
		panelConfirm = new JPanel();
		panelConfirm.setBackground(new Color(255, 255, 102));
		GridBagConstraints gbc_panelConfirm = new GridBagConstraints();
		gbc_panelConfirm.insets = new Insets(0, 0, 5, 5);
		gbc_panelConfirm.fill = GridBagConstraints.BOTH;
		gbc_panelConfirm.gridx = 1;
		gbc_panelConfirm.gridy = 5;
		contentPane.add(panelConfirm, gbc_panelConfirm);
		
		lblConfirm = new JLabel("Confirm:   ");
		lblConfirm.setFont(new Font("MS UI Gothic", Font.PLAIN, 16));
		panelConfirm.add(lblConfirm);
		
		passwordField2 = new JPasswordField();
		passwordField2.setFont(new Font("MS UI Gothic", Font.PLAIN, 16));
		panelConfirm.add(passwordField2);
		passwordField2.setColumns(20);
		
		lblErrorEmail = new JLabel("The e-mail you have entered is already taken.");
		lblErrorEmail.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
		lblErrorEmail.setForeground(Color.RED);
		GridBagConstraints gbc_lblErrorEmail = new GridBagConstraints();
		gbc_lblErrorEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblErrorEmail.gridx = 1;
		gbc_lblErrorEmail.gridy = 6;
		contentPane.add(lblErrorEmail, gbc_lblErrorEmail);
		
		panelEmail = new JPanel();
		panelEmail.setBackground(new Color(255, 255, 102));
		GridBagConstraints gbc_panelEmail = new GridBagConstraints();
		gbc_panelEmail.insets = new Insets(0, 0, 5, 5);
		gbc_panelEmail.fill = GridBagConstraints.BOTH;
		gbc_panelEmail.gridx = 1;
		gbc_panelEmail.gridy = 7;
		contentPane.add(panelEmail, gbc_panelEmail);
		
		lblEmail = new JLabel("E-mail:     ");
		lblEmail.setFont(new Font("MS UI Gothic", Font.PLAIN, 16));
		panelEmail.add(lblEmail);
		
		emailField = new JTextField();
		emailField.setFont(new Font("MS UI Gothic", Font.PLAIN, 16));
		panelEmail.add(emailField);
		emailField.setColumns(20);
		
		btnSignUp = new JButton("Sign Up");
		btnSignUp.setBackground(Color.GRAY);
		btnSignUp.setFont(new Font("MS UI Gothic", Font.BOLD, 20));
		GridBagConstraints gbc_btnSignUp = new GridBagConstraints();
		gbc_btnSignUp.insets = new Insets(0, 0, 5, 5);
		gbc_btnSignUp.gridx = 1;
		gbc_btnSignUp.gridy = 9;
		contentPane.add(btnSignUp, gbc_btnSignUp);
		
		clearErrorMessages();
	}
	
	public void signUpHandler()
	{
		btnSignUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SwingWorker<Void,String> worker = new SwingWorker<Void,String>()
				{

					@Override
					protected Void doInBackground() throws Exception 
					{
						clearErrorMessages();
						SignUpClient sendObject = new SignUpClient();
						String username = usernameField.getText();
						sendObject.setUsername(username); 
						String password = String.valueOf(passwordField1.getPassword());
						sendObject.setPassword(password);
						String repeatPassword = String.valueOf(passwordField2.getPassword());
						sendObject.setRetypedPassword(repeatPassword);
						String email = emailField.getText();
						sendObject.setEmail(email);
						sendObject.setCode(SIGNUPID);
						client.output.writeObject(sendObject);
						client.output.flush();
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
	
	public void response()
	{
		workerReader = new SwingWorker<Void,String>()
		{
			@Override
			protected Void doInBackground() throws Exception 
			{
				while(true)
				{
					try
					{
						synchronized(client.input)
						{
							lineReceived = (String) client.input.readObject();
						}
						if (lineReceived.equals("okay"))
							break;
						if (lineReceived.equals("Invalid username"))
							panelErrorUsername.setVisible(true);
						if (lineReceived.equals("Password mismatch"))
							lblErrorPassword.setVisible(true);
						if (lineReceived.equals("Email already taken"))
							lblErrorEmail.setVisible(true);
						passwordField1.setText("");
						passwordField2.setText("");
					}
					catch(ClassNotFoundException cnfe)
					{
						cnfe.printStackTrace();
						client.close();
						break;
					}
					catch(IOException ioe)
					{
						ioe.printStackTrace();
						client.close();
						break;
					}
				}
				return null;
			}
			
			protected void done()
			{
				close();
			}
			
		};
		workerReader.execute();
	}
	
	public void clearErrorMessages()
	{
		panelErrorUsername.setVisible(false);
		lblErrorPassword.setVisible(false);
		lblErrorEmail.setVisible(false);
	}

}
