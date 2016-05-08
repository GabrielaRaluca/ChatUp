package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.JTextPane;
import javax.swing.JTextArea;

public class FriendsFrame extends JFrame {

	private JPanel contentPane;
	private JTextPane txtpnAsda;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FriendsFrame frame = new FriendsFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FriendsFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		txtpnAsda = new JTextPane();
		txtpnAsda.setText("asda");
		
		URL url = LoginFrame.class.getResource("/resources/ChatUp!.png");
		ImageIcon imageIcon = new ImageIcon(url);
		Image scaledImage = imageIcon.getImage().getScaledInstance(10, 10, java.awt.Image.SCALE_SMOOTH);
		ImageIcon logo = new ImageIcon(scaledImage);
		
		txtpnAsda.insertIcon(logo);
		try {
			appendString("fakakaklalala\n");
			appendString("ok new line");
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		contentPane.add(txtpnAsda, BorderLayout.CENTER);
	}
	
	public void appendString(String str) throws BadLocationException
	{
	     StyledDocument document = (StyledDocument) txtpnAsda.getDocument();
	     document.insertString(document.getLength(), str, null);
	                                                    // ^ or your style attribute  
	 }

}
