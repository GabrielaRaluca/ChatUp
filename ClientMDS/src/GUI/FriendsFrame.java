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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import client.Client;
import emoji.EmojiMap;
import server.SignUpClient;
import systemtrays.LoggedSystemTray;

public class FriendsFrame extends JFrame {

	private JPanel contentPane;
	
	private JButton btnAddFriend;
	private JButton btnBlockFriend;
	private JButton btnLogout;
	
	private JLabel lblFriends;
	private JList<String> list;
	private JList<String> pendingList;
	private JList<String> blockedList;
	
	private JSeparator separator;
	private JScrollPane scrollPane;
	
	private DefaultListModel<String> friendsModel;
	private DefaultListModel<String> pendingModel;
	private DefaultListModel<String> blockedModel;
	
	private final String MESSAGEID = "message";
	private final String NEWUSER = "newUser";
	private final String USEROFFLINE = "userOffline";
	private final String LOGOUT = "logout";
	private final String IESI = "youcanleave";

	private ArrayList<String> onlineFriends;
	private ArrayList<String> offlineFriends;
	private ArrayList<String> requestingFriends;
	private ArrayList<String> blockedFriends;
	Client client;
	public static LoggedSystemTray loggedTray;
	SignUpClient friendsList;
	SignUpClient objectReceived;
	SignUpClient sendObject;
	AddFriend addFriend;
	BlockFriend blockFriend;
	static LoginFrame frameLogin;
	
	private JTabbedPane tabbedPane;


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
		setBounds(100, 100, 310, 500);
		setMinimumSize(new Dimension(310, 500));
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
		
		btnBlockFriend = new JButton("");
		btnBlockFriend.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		imageIcon = new ImageIcon(FriendsFrame.class.getResource("/resources/blockuser.png"));
		btnBlockFriend.setOpaque(false);
		btnBlockFriend.setContentAreaFilled(false);
		btnBlockFriend.setBorderPainted(false);
		btnBlockFriend.setFocusPainted(false);
		btnBlockFriend.setIcon(imageIcon);
		
		btnLogout = new JButton("");
		btnLogout.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		imageIcon = new ImageIcon(FriendsFrame.class.getResource("/resources/exitb.png"));
		btnLogout.setOpaque(false);
		btnLogout.setContentAreaFilled(false);
		btnLogout.setBorderPainted(false);
		btnLogout.setFocusPainted(false);
		btnLogout.setIcon(imageIcon);
		
		separator = new JSeparator();
		separator.setForeground(new Color(255, 255, 102));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		tabbedPane.setForeground(Color.DARK_GRAY);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(2)
							.addComponent(tabbedPane))
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnAddFriend, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnBlockFriend, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnLogout, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
					.addGap(9))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddFriend)
						.addComponent(btnBlockFriend)
						.addComponent(btnLogout))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		onlineFriends = new ArrayList<String>();
		offlineFriends = new ArrayList<String>();
		requestingFriends = new ArrayList<String>();
		blockedFriends = new ArrayList<String>();
		
		friendsModel = new DefaultListModel<String>();
		pendingModel = new DefaultListModel<String>();
		blockedModel = new DefaultListModel<String>();
		
		scrollPane = new JScrollPane();
		tabbedPane.addTab("Friends", null, scrollPane, null);
		
		list = new JList<String>();
		list.setBackground(new Color(255, 255, 102));
		list.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		
		list.setModel(friendsModel);
		list.setSelectionBackground(Color.GRAY);
		list.setSelectionForeground(new Color(255, 255, 102));
		
		scrollPane.setViewportView(list);
		
		pendingList = new JList<String>();
		pendingList.setSelectionForeground(new Color(255, 255, 102));
		pendingList.setSelectionBackground(Color.GRAY);
		pendingList.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		pendingList.setBackground(new Color(255, 255, 102));
		pendingList.setModel(pendingModel);
		tabbedPane.addTab("Pending", null, pendingList, null);
		
		blockedList = new JList<String>();
		blockedList.setSelectionForeground(new Color(255, 255, 102));
		blockedList.setSelectionBackground(Color.GRAY);
		blockedList.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		blockedList.setBackground(new Color(255, 255, 102));
		blockedList.setModel(blockedModel);
		tabbedPane.addTab("Blocked", null, blockedList, null);
		
		contentPane.setLayout(gl_contentPane);
		setVisible(true);
		
		loggedTray = new LoggedSystemTray(client);
		conversationMap = new HashMap<String,Conversation>();
		
		inputMap = list.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = list.getActionMap();
		
		EmojiMap.initialize();
	}

	public void initializeFriendsList(SignUpClient friends)
	{
		if(friends != null)
		{
			onlineFriends.addAll(friends.getOnlineFriends());
			offlineFriends.addAll(friends.getOfflineFriends());
			requestingFriends.addAll(friends.getPending());
			blockedFriends.addAll(friends.getBlockedFriends());
			updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);
		}
	}
	
	public void updateList(ArrayList<String> onlineFriends, ArrayList<String> offlineFriends, ArrayList<String> requestingFriends, ArrayList<String> blockedFriends)
	{
		int i;
		tabbedPane.removeAll();
		
		friendsModel.clear();
		pendingModel.clear();
		blockedModel.clear();
		
		list.setModel(friendsModel);
		pendingList.setModel(pendingModel);
		blockedList.setModel(blockedModel);
		
		friendsModel.addElement("<html><b>--------Online--------</b></html>");
		for (i = 0; i < onlineFriends.size(); i++)
		{
			String boldFriend = "<html><b>" + onlineFriends.get(i) + "</b></html>";
			friendsModel.addElement(boldFriend);
		}
		
		friendsModel.addElement("<html><b>--------Offline--------</b></html>");
		
		for (i = 0; i < offlineFriends.size(); i++)
		{
			String italicFriend = "<html><i>" + offlineFriends.get(i) + "</i></html>";
			friendsModel.addElement(italicFriend);
		}
		
		for (i = 0; i < requestingFriends.size(); i++)
		{
			String possibleFriend = "<html><b>" + requestingFriends.get(i) + "</b></html>";
			pendingModel.addElement(possibleFriend);
		}
		
		for (i = 0; i < blockedFriends.size(); i++)
		{
			String blockedFriend = "<html><b>" + blockedFriends.get(i) + "</b></html>";
			blockedModel.addElement(blockedFriend);
		}
		
		tabbedPane.addTab("Friends", null, scrollPane, null);
		tabbedPane.addTab("Pending", null, pendingList, null);
		tabbedPane.addTab("Blocked", null, blockedList, null);
			
	}
	
	public void setUpFriendsList()
	{
		onlineFriends = new ArrayList<String>();
		offlineFriends = new ArrayList<String>();
		requestingFriends = new ArrayList<String>();
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
					JOptionPane.showMessageDialog(null, "Server connection failed. Please try again later!", "Connection failure", JOptionPane.ERROR_MESSAGE);
					Client.close();
					dispose();
					LoggedSystemTray.removeSystemTrayIcon();
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
		addFriendHandler();
		blockFriendHandler();
	}
	
	public void mouseHandler()
	{
		MouseListener friendsListener = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					//friends list double click
					String selectedItem = (String) list.getSelectedValue();
					String content = selectedItem.substring(9);
					int index = content.indexOf("<");
					final String actualName = content.substring(0, index);
					selectedItem = actualName;
					
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
					String content = selectedItem.substring(9);
					int index = content.indexOf("<");
					String actualName = content.substring(0, index);
					selectedItem = actualName;
					
					if (!conversationMap.containsKey(selectedItem))
					{
						Conversation conversation = new Conversation(client, selectedItem);
						conversationMap.put(selectedItem, conversation);
						conversation.start();
					}
				}
			}
		};
		list.addMouseListener(friendsListener);
		
		
		MouseListener pendingListener = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					String selectedItem = (String) pendingList.getSelectedValue();
					String content = selectedItem.substring(9);
					int index = content.indexOf("<");
					String actualName = content.substring(0, index);
					selectedItem = actualName;
						
					String answer;
					int option = JOptionPane.showConfirmDialog(contentPane,"The user" + selectedItem + "wants to add you", "The user" + selectedItem + "wants to add you",JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION)
					{
						answer = "accept";
						for (int i = 0; i < requestingFriends.size(); i++)
						{
							if (requestingFriends.get(i).equals(selectedItem))
							{
								requestingFriends.remove(i);
								break;
							}
						}
						offlineFriends.add(selectedItem);
						updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);		
					}
					else
					{
						answer = "decline";
						for (int i = 0; i < requestingFriends.size(); i++)
						{
							if (requestingFriends.get(i).equals(selectedItem))
							{
								requestingFriends.remove(i);
								break;
							}
						}
						updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);
					}
					
					sendObject = new SignUpClient();
					sendObject.setCode(answer);
					sendObject.setUsername(selectedItem);
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
					{
						@Override
						protected Void doInBackground() throws Exception 
						{
							client.output.writeObject(sendObject);
							return null;
						}	
					};
					worker.execute();
				}
				
				else if(e.getClickCount() == 3)
				{
					String selectedItem = (String) pendingList.getSelectedValue();
					String content = selectedItem.substring(9);
					int index = content.indexOf("<");
					String actualName = content.substring(0, index);
					selectedItem = actualName;
						
					String answer;
					int option = JOptionPane.showConfirmDialog(contentPane,"The user " + selectedItem + " wants to add you", "The user " + selectedItem + " wants to add you",JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION)
					{
						answer = "accept";
						for (int i = 0; i < requestingFriends.size(); i++)
						{
							if (requestingFriends.get(i).equals(selectedItem))
							{
								requestingFriends.remove(i);
								break;
							}
						}
						offlineFriends.add(selectedItem);
						updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);		
					}
					else
					{
						answer = "decline";
						for (int i = 0; i < requestingFriends.size(); i++)
						{
							if (requestingFriends.get(i).equals(selectedItem))
							{
								requestingFriends.remove(i);
								break;
							}
						}
						updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);
					}
					
					sendObject = new SignUpClient();
					sendObject.setCode(answer);
					sendObject.setUsername(selectedItem);
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
					{
						@Override
						protected Void doInBackground() throws Exception 
						{
							client.output.writeObject(sendObject);
							return null;
						}	
					};
					worker.execute();
				}
			}
		};
		
		pendingList.addMouseListener(pendingListener);
		
		MouseListener blockListener = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					String selectedItem = (String) blockedList.getSelectedValue();
					String content = selectedItem.substring(9);
					int index = content.indexOf("<");
					String actualName = content.substring(0, index);
					selectedItem = actualName;
						
					String answer;
					int option = JOptionPane.showConfirmDialog(contentPane,"Are you sure you want to unblock the user " + selectedItem + "?", "Are you sure you want to unblock the user " + selectedItem + "?" ,JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION)
					{
						answer = "removeFromBlock";
						for (int i = 0; i < blockedFriends.size(); i++)
						{
							if (blockedFriends.get(i).equals(selectedItem))
							{
								blockedFriends.remove(i);
								break;
							}
						}
						updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);
						
						sendObject = new SignUpClient();
						sendObject.setCode(answer);
						sendObject.setUsername(selectedItem);
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
						{
							@Override
							protected Void doInBackground() throws Exception 
							{
								client.output.writeObject(sendObject);
								return null;
							}	
						};
						worker.execute();
					}	
				}
			}
		};
		blockedList.addMouseListener(blockListener);
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
						JOptionPane.showMessageDialog(null, "Server connection failed. Please try again later!", "Connection failure", JOptionPane.ERROR_MESSAGE);
						Client.close();
						dispose();
						LoggedSystemTray.removeSystemTrayIcon();
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
						conversation.writeTheMessage(objectReceived.getMessage());
					}
					else
					{
						Conversation conversation = conversationMap.get(objectReceived.getUsername());
						conversation.writeTheMessage(objectReceived.getMessage());
					}
				}
				
				if(objectReceived.getCode().equals(NEWUSER))
				{
					onlineFriends.add(objectReceived.getUsername());
					Collections.sort(onlineFriends);
					for (int i = 0; i < offlineFriends.size(); i++)
						if(offlineFriends.get(i).equals(objectReceived.getUsername()))
							offlineFriends.remove(i);
					updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);
				}
				
				if(objectReceived.getCode().equals(USEROFFLINE))
				{
					offlineFriends.add(objectReceived.getUsername());
					Collections.sort(offlineFriends);
					for (int i = 0; i < onlineFriends.size(); i++)
						if(onlineFriends.get(i).equals(objectReceived.getUsername()))
							onlineFriends.remove(i);
					updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);
				}
				if(objectReceived.getCode().equals("requestSent"))
				{
					addFriend.dispose();
				}
				if(objectReceived.getCode().equals("alreadyFriends"))
				{
					addFriend.getLblWrongUsername().setText("You are already friends with this username.");
				}

				if(objectReceived.getCode().equals("addInvalidUsername"))
				{
					addFriend.getLblWrongUsername().setText("The username you have entered is invalid.");
				}
				
				if(objectReceived.getCode().equals("newPendingUser"))
				{
					requestingFriends.add(objectReceived.getUsername());
					Collections.sort(requestingFriends);
					updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);
				}
				
				if(objectReceived.getCode().equals("blockInvalidUsername"))
				{
					blockFriend.getLblWrongUsername().setText("The username you have entered is invalid.");
				}
				
				if(objectReceived.getCode().equals("blockedUser"))
				{
					blockFriend.dispose();
					String userToDelete = objectReceived.getUsername();
					blockedFriends.add(userToDelete);
					for (int i = 0; i < onlineFriends.size(); i++)
					{
						if(onlineFriends.get(i).equals(userToDelete))
						{
							onlineFriends.remove(i);
							break;
						}
					}
					
					for (int i = 0; i < offlineFriends.size(); i++)
					{
						if(offlineFriends.get(i).equals(userToDelete))
						{
							offlineFriends.remove(i);
							break;
						}
					}
					updateList(onlineFriends, offlineFriends, requestingFriends, blockedFriends);
				}

				if (objectReceived.getCode().equals("getMessages"))
				{
					Conversation convo = conversationMap.get(objectReceived.getUsername());
					System.out.println(convo.messages.getText());
					HTMLDocument doc = new HTMLDocument();
					doc = convo.getDoc();
					String paneMessage = "";
					
					try {
						paneMessage = doc.getText(0, doc.getLength());
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (paneMessage.isEmpty())
						convo.writeOlderMessage(objectReceived.getOldMessages().get(0), 0);
					else
						convo.writeOlderMessage(objectReceived.getOldMessages().get(0), 1);
					for (int i = 2; i < objectReceived.getOldMessages().size(); i++)
						convo.writeOlderMessage(objectReceived.getOldMessages().get(i), 1);
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
	
	public void addFriendHandler()
	{
		btnAddFriend.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				addFriend = new AddFriend(client);
				addFriend.setVisible(true);
				addFriend.start();
			}
		});
	}
	
	public void blockFriendHandler()
	{
		btnBlockFriend.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				// TODO Auto-generated method stub
				blockFriend = new BlockFriend(client);
				blockFriend.setVisible(true);
				blockFriend.start();
			}
			
		});
	}
		
}