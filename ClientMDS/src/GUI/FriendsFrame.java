package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import client.Client;
import server.SignUpClient;
import systemtrays.LoggedSystemTray;

public class FriendsFrame extends JFrame {

	private JPanel contentPane;
	
	private JButton btnAddFriend;
	private JButton btnBlockFriend;
	private JButton btnLogout;
	
	private JLabel lblFriends;
	private JList<String> list;
	
	private JSeparator separator;
	private JScrollPane scrollPane;
	
	private DefaultListModel<String> model;
	
	private final String MESSAGEID = "message";
	private final String NEWUSER = "newUser";
	private final String USEROFFLINE = "userOffline";
	private final String LOGOUT = "logout";
	private final String IESI = "youcanleave";

	private ArrayList<String> onlineFriends;
	private ArrayList<String> offlineFriends;
	Client client;
	public static LoggedSystemTray loggedTray;
	SignUpClient friendsList;
	SignUpClient objectReceived;
	static LoginFrame frameLogin;
	public static HashMap<String, Conversation> conversationMap;
	
	private InputMap inputMap;
	private ActionMap actionMap;
	
	public FriendsFrame(Client client)
	{
		this.client = client;
	}
	
	public static LoginFrame getLoginFrame()
	{
		return frameLogin;
	}
	
	public void start()
	{
		initialize();
		setUpFriendsList();
		whileConnected();
	}
	
	
	public void initialize()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(FriendsFrame.class.getResource("/resources/ChatUp!.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 500);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnAddFriend = new JButton("");
		btnAddFriend.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		ImageIcon imageIcon = new ImageIcon(FriendsFrame.class.getResource("/resources/adduser.png"));
		btnAddFriend.setOpaque(false);
		btnAddFriend.setContentAreaFilled(false);
		btnAddFriend.setBorderPainted(false);
		btnAddFriend.setFocusPainted(false);
		btnAddFriend.setIcon(imageIcon);
		
		btnBlockFriend = new JButton("Block Friend");
		btnBlockFriend.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		
		btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		
		separator = new JSeparator();
		separator.setForeground(new Color(255, 255, 102));
		
		lblFriends = new JLabel("Friends");
		lblFriends.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		lblFriends.setForeground(new Color(255, 255, 102));
		lblFriends.setBackground(new Color(255, 255, 102));
		
		scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblFriends, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
							.addGap(188))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnAddFriend, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnBlockFriend, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnLogout, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
							.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnAddFriend)
							.addComponent(btnBlockFriend))
						.addComponent(btnLogout))
					.addGap(8)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(9)
					.addComponent(lblFriends)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		list = new JList<String>();
		list.setBackground(new Color(255, 255, 102));
		list.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		model = new DefaultListModel<String>();
		list.setModel(model);
		list.setSelectionBackground(Color.GRAY);
		list.setSelectionForeground(new Color(255, 255, 102));
		
		scrollPane.setViewportView(list);
		contentPane.setLayout(gl_contentPane);
		setVisible(true);
		
		loggedTray = new LoggedSystemTray(client);
		conversationMap = new HashMap<String,Conversation>();
		
		inputMap = list.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = list.getActionMap();
	}

	public void initializeFriendsList(SignUpClient friends)
	{
		onlineFriends.addAll(friends.getOnlineFriends());
		offlineFriends.addAll(friends.getOfflineFriends());
		updateList(onlineFriends, offlineFriends);
	}
	
	public void updateList(ArrayList<String> onlineFriends, ArrayList<String> offlineFriends)
	{
		
		int i;
		model.clear();
		model.addElement("<html><b>----------Online----------</b></html>");
		for (i = 0; i < onlineFriends.size(); i++)
		{
			String boldFriend = "<html><b>" + onlineFriends.get(i) + "</b></html>";
			model.addElement(boldFriend);
		}
		model.addElement("<html><b>----------Offline----------</b></html>");
		
		for (i = 0; i < offlineFriends.size(); i++)
		{
			String italicFriend = "<html><i>" + offlineFriends.get(i) + "</i></html>";
			model.addElement(italicFriend);
		}
	}
	
	public void setUpFriendsList()
	{
		onlineFriends = new ArrayList<String>();
		offlineFriends = new ArrayList<String>();
		SwingWorker<Void, String> worker = new SwingWorker<Void, String>()
		{

			@Override
			protected Void doInBackground() throws Exception 
			{
				try
				{
					synchronized(Client.input)
					{
						friendsList = (SignUpClient) Client.input.readObject();
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
					Client.close();
				}
				return null;
			}
			
			protected void done()
			{
				initializeFriendsList(friendsList);
			}
		};
		worker.execute();
	}
	
	public void setUpHandlers()
	{
		mouseHandler();
		logoutHandler();
	}
	
	public void mouseHandler()
	{
		MouseListener mouseListener = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					String selectedItem = (String) list.getSelectedValue();
					if (selectedItem.startsWith("<"))
					{
						String content = selectedItem.substring(9);
						int index = content.indexOf("<");
						String actualName = content.substring(0, index);
						selectedItem = actualName;
					}
					if (!conversationMap.containsKey(selectedItem))
					{
						Conversation conversation = new Conversation(client, selectedItem);
						conversationMap.put(selectedItem, conversation);
						conversation.start();
					}
				}
				else if (e.getClickCount() == 3)
				{
					String selectedItem = (String) list.getSelectedValue();
					if (selectedItem.startsWith("<"))
					{
						String content = selectedItem.substring(9);
						int index = content.indexOf("<");
						String actualName = content.substring(0, index);
						selectedItem = actualName;
					}
					if (!conversationMap.containsKey(selectedItem))
					{
						Conversation conversation = new Conversation(client, selectedItem);
						conversationMap.put(selectedItem, conversation);
						conversation.start();
					}
				}
			}
		};
		list.addMouseListener(mouseListener);
	}
	
	public void keyboardHandler()
	{
		Action openConversation = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e) 
			{	
				String selectedItem = (String) list.getSelectedValue();
				if (selectedItem.startsWith("<"))
				{
					String content = selectedItem.substring(9);
					int index = content.indexOf("<");
					String actualName = content.substring(0, index);
					selectedItem = actualName;
				}
				if (!conversationMap.containsKey(selectedItem))
				{
					Conversation conversation = new Conversation(client, selectedItem);
					conversationMap.put(selectedItem, conversation);
					conversation.start();
				}
			}
		};
		
		inputMap.put(KeyStroke.getKeyStroke("ENTER"),"newFrame");
		actionMap.put("newFrame", openConversation);
	}
	
	public void whileConnected()
	{
		setUpHandlers();
		
		SwingWorker<Void,SignUpClient> worker = new SwingWorker<Void,SignUpClient>()
		{
			@Override
			protected Void doInBackground() throws Exception 
			{
				while(true)
				{
					try
					{
						synchronized(Client.input)
						{
							objectReceived = (SignUpClient) Client.input.readObject();
							if (objectReceived.getCode().equals(IESI))
								break;
						}
						publish(objectReceived);
					}
					catch(ClassNotFoundException cnfe)
					{
						cnfe.printStackTrace();
						Client.close();
						break;
					}
					catch(IOException ioe)
					{
						ioe.printStackTrace();
						Client.close();
						break;
					}
				}
				return null;
			}
			
			protected void process(List<SignUpClient> list)
			{
				if(objectReceived.getCode().equals(MESSAGEID))
				{
					if (!conversationMap.containsKey(objectReceived.getUsername()))
					{
						Conversation conversation = new Conversation(client, objectReceived.getUsername());
						conversationMap.put(objectReceived.getUsername(), conversation);
						conversation.start();
						conversation.showMessage(objectReceived.getMessage());
					}
					else
					{
						Conversation conversation = conversationMap.get(objectReceived.getUsername());
						conversation.showMessage(objectReceived.getMessage());
					}
				}
				
				if(objectReceived.getCode().equals(NEWUSER))
				{
					onlineFriends.add(objectReceived.getUsername());
					Collections.sort(onlineFriends);
					for (int i = 0; i < offlineFriends.size(); i++)
						if(offlineFriends.get(i).equals(objectReceived.getUsername()))
							offlineFriends.remove(i);
					updateList(onlineFriends, offlineFriends);
				}
				
				if(objectReceived.getCode().equals(USEROFFLINE))
				{
					offlineFriends.add(objectReceived.getUsername());
					Collections.sort(offlineFriends);
					for (int i = 0; i < onlineFriends.size(); i++)
						if(onlineFriends.get(i).equals(objectReceived.getUsername()))
							onlineFriends.remove(i);
					updateList(onlineFriends, offlineFriends);
				}
				
			}
		};
		worker.execute();
	}

	public static void removeMap(Conversation conversation) 
	{
		conversationMap.remove(conversation.getDestinationUsername());
	}
	
	public void logoutHandler()
	{
		btnLogout.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				SignUpClient sendObject = new SignUpClient();
				SwingWorker<Void, String> worker = new SwingWorker<Void, String>()
				{
					@Override
					protected Void doInBackground() throws Exception 
					{
						sendObject.setCode(LOGOUT);
						Client.output.reset();
						Client.output.writeObject(sendObject);
						Client.output.flush();
						return null;
					}
					
					protected void done()
					{
						ArrayList<Conversation> removeConversations = new ArrayList<Conversation>();
		              	removeConversations.addAll(conversationMap.values());
		              	
		              	for (int i = 0; i < removeConversations.size(); i++)
		              	{
		              		removeConversations.get(i).dispose();
		              	}
		              	
		              	dispose();
		              	LoggedSystemTray.removeSystemTrayIcon();
		              	frameLogin = new LoginFrame(client);
		              	frameLogin.start();
					}
				};
				worker.execute();
				
				
			}		
			
		});
	}
	
}