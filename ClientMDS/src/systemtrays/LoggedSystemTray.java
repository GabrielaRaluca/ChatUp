package systemtrays;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import GUI.Conversation;
import GUI.FriendsFrame;
import GUI.LoginFrame;
import client.Client;
import server.SignUpClient;

public class LoggedSystemTray 
{
	Client client;
	static SystemTray systemTray;
	static TrayIcon trayIcon;
	private final String EXITID = "exit";
	
	public LoggedSystemTray(Client client)
	{
		this.client = client;
		URL url = LoginFrame.class.getResource("/resources/onlineicon.png");

        if (SystemTray.isSupported()) {
        	
        	PopupMenu menu = new PopupMenu();

            MenuItem friendsItem = new MenuItem("Friends");
            friendsItem.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                LoginFrame.getFriendsFrame().setVisible(true);
              }
            });
            menu.add(friendsItem);
            
            MenuItem logoutItem = new MenuItem("Logout");
            logoutItem.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "www.java2s.com");
              }
            });
            menu.add(logoutItem);
            
            MenuItem closeItem = new MenuItem("Exit ChatUp!");
            menu.add(closeItem);
            
            systemTray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(new ImageIcon(url).getImage(), "Java App", menu);
            trayIcon.setImageAutoSize(true);// Autosize icon based on space
            
            closeItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
              	SignUpClient sendObject = new SignUpClient();
              	sendObject.setCode(EXITID);
              	try {
  					Client.output.writeObject(sendObject);
  					Client.output.flush();
  				} catch (IOException e1) 
              	{
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				}
              	ArrayList<Conversation> removeConversations = new ArrayList<Conversation>();
              	removeConversations.addAll(FriendsFrame.conversationMap.values());
              	
              	for (int i = 0; i < removeConversations.size(); i++)
              	{
              		removeConversations.get(i).dispose();
              	}
              	Client.close();
              	LoginFrame.getFriendsFrame().dispose();
              	removeSystemTrayIcon();
                }
              });
            
            try {
                systemTray.add(trayIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }

            
        }
	}
	public static void removeSystemTrayIcon()
    {
		systemTray.remove(trayIcon);
    }
}
