package GUI;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.Client;
import server.SignUpClient;

import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class AddFriend extends JDialog 
{
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JButton addButton;
	private JButton cancelButton;
	
	private JLabel lblEnterUsername;
	
	private final String ADDFRIEND = "addfriend";
	private JLabel lblWrongUsername;
		
	Client client;

	public JLabel getLblWrongUsername() 
	{
		return lblWrongUsername;
	}
	
	public AddFriend(Client client) 
	{
		this.client = client;
		setBounds(100, 100, 350, 150);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{15, 0, 15, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			lblEnterUsername = new JLabel("Enter Username");
			lblEnterUsername.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
			GridBagConstraints gbc_lblEnterUsername = new GridBagConstraints();
			gbc_lblEnterUsername.insets = new Insets(0, 0, 5, 5);
			gbc_lblEnterUsername.gridx = 1;
			gbc_lblEnterUsername.gridy = 1;
			contentPanel.add(lblEnterUsername, gbc_lblEnterUsername);
		}
		{
			textField = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 3;
			gbc_textField.gridy = 1;
			contentPanel.add(textField, gbc_textField);
			textField.setColumns(10);
		}
		{
			lblWrongUsername = new JLabel("");
			lblWrongUsername.setForeground(Color.RED);
			lblWrongUsername.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
			GridBagConstraints gbc_lblWrongUsername = new GridBagConstraints();
			gbc_lblWrongUsername.insets = new Insets(0, 0, 5, 5);
			gbc_lblWrongUsername.gridx = 1;
			gbc_lblWrongUsername.gridy = 2;
			gbc_lblWrongUsername.gridwidth = 3;
			contentPanel.add(lblWrongUsername, gbc_lblWrongUsername);
		}
		{
			JPanel buttonPane = new JPanel();
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.insets = new Insets(0, 0, 0, 5);
			gbc_buttonPane.gridx = 1;
			gbc_buttonPane.gridy = 3;
			gbc_buttonPane.gridwidth = 3;
			contentPanel.add(buttonPane, gbc_buttonPane);
			{
				addButton = new JButton("Add Friend");
				addButton.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
				addButton.setActionCommand("");
				getRootPane().setDefaultButton(addButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
				cancelButton.setActionCommand("Cancel");
			}
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(31)
						.addComponent(addButton)
						.addGap(31)
						.addComponent(cancelButton)
						.addGap(115))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(addButton)
							.addComponent(cancelButton)))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
	}
	
	public void start()
	{
		addButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
				{
					protected Void doInBackground() throws Exception 
					{
						String userToSend = textField.getText();
						SignUpClient sendObject = new SignUpClient();
						sendObject.setCode(ADDFRIEND);
						sendObject.setUsername(userToSend);
						client.output.writeObject(sendObject);
						return null;	
					}
					
					/*protected void done()
					{
						System.out.println("intra aici");
		
						
						if (FriendsFrame.code.equals("requestSent"))
							dispose();
						else if (FriendsFrame.code.equals("alreadyFriends"))
						{
							System.out.println(SwingUtilities.isEventDispatchThread());
							lblWrongUsername.setText("You are already friends with this username.");
							System.out.println("deja preteni");
						}
						else if (FriendsFrame.code.equals("invalidUsername"))
						{
							System.out.println(SwingUtilities.isEventDispatchThread());
							lblWrongUsername.setText("The username you have entered is invalid.");
							System.out.println("nu e bine");
						}
					}*/
				};
				worker.execute();
			}
		});
		
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dispose();			
			}
			
		});
	}	
}
