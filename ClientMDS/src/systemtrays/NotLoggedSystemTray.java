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

public class NotLoggedSystemTray 
{
	Client client;
	static SystemTray systemOfflineTray;
	static TrayIcon trayIconOffline;
	private final String EXITID = "exit";
	
	public NotLoggedSystemTray(Client client)
	{
		this.client = client;
		URL url = LoginFrame.class.getResource("/resources/offlineicon.png");
		
		if (SystemTray.isSupported()) 
		{
        	
        	PopupMenu menu = new PopupMenu();

            MenuItem chatUpItem = new MenuItem("ChatUp!");
            chatUpItem.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
               FriendsFrame.getLoginFrame().setVisible(true);
              }
            });
            menu.add(chatUpItem);
                       
            MenuItem closeItem = new MenuItem("Exit ChatUp!");
            menu.add(closeItem);
            
            systemOfflineTray = SystemTray.getSystemTray();
            trayIconOffline = new TrayIcon(new ImageIcon(url).getImage(), "Java App", menu);
            trayIconOffline.setImageAutoSize(true);// Autosize icon based on space
            
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
              	FriendsFrame.getLoginFrame().dispose();
              	removeSystemTrayIcon();
                }
              });
            
            try {
                systemOfflineTray.add(trayIconOffline);
            } catch (Exception e) {
                e.printStackTrace();
            }

            
        }
	}
	public static void removeSystemTrayIcon()
    {
		systemOfflineTray.remove(trayIconOffline);
    }
}
