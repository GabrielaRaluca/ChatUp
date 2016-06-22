package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

import client.Client;
import server.SignUpClient;

public class BlockFriend extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPanel buttonPane;
	private JButton blockButton;
	private JButton cancelButton;
	
	private JLabel lblEnterUsername;
	
	private final String BLOCKFRIEND = "blockfriend";
	private JLabel lblWrongUsername;
	
	private SignUpClient objectReceived; 
	Client client;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			BlockFriend dialog = new BlockFriend(client);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	public JLabel getLblWrongUsername() 
	{
		return lblWrongUsername;
	}

	/**
	 * Create the dialog.
	 */
	public BlockFriend(Client client) 
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
				blockButton = new JButton("Block Friend");
				blockButton.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
				blockButton.setActionCommand("");
				getRootPane().setDefaultButton(blockButton);
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
						.addComponent(blockButton)
						.addGap(31)
						.addComponent(cancelButton)
						.addGap(115))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(blockButton)
							.addComponent(cancelButton)))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
	}
	
	public void start()
	{
		blockButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				// TODO Auto-generated method stub
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
				{
					@Override
					protected Void doInBackground() throws Exception 
					{
						// TODO Auto-generated method stub
						String userToSend = textField.getText();
						SignUpClient sendObject = new SignUpClient();
						sendObject.setCode(BLOCKFRIEND);
						sendObject.setUsername(userToSend);
						client.output.writeObject(sendObject);
						return null;
					}	
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
