package GUI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

public class AddFriendDialog extends JDialog implements ActionListener
{
	private JPanel contentPane;
	private JTextField textField;
	
	public AddFriendDialog()
	{
		setSize(300 , 150);
        setResizable(false);
        
        JPanel pane = contentPanel();
        add(pane);

	}
	
	public JPanel contentPanel()
	{
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblEnterUsername = new JLabel("Enter username: ");
		lblEnterUsername.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		
		textField = new JTextField();
		textField.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		textField.setColumns(10);
		
		JButton btnAddFriend = new JButton("Add Friend");
		btnAddFriend.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(71)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblEnterUsername)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnAddFriend)
							.addGap(50)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(79, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(63)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEnterUsername)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAddFriend)
						.addComponent(btnCancel))
					.addContainerGap(123, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		return contentPane;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
