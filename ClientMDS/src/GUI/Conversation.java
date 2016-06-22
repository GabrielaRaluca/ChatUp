package GUI;
import client.*;
import emoji.EmojiMap;
import server.SignUpClient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;

import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;


public class Conversation extends JFrame {

	public JPanel contentPane;
	public JButton sendButton;
	private JButton btnShowOlderMessages;
	public JScrollPane scrollPane;
	public JScrollPane scrollPane_1;
	private JTextArea writeMessage;
	public JTextPane messages;
	
	public String lineSent;
	public String lineReceived;
	public final String MESSAGEID = "message";
	
	public InputMap inputMap;
	public ActionMap actionMap;

	public Client client;
	private HTMLEditorKit kit;
	private HTMLDocument document;
	
	public HTMLDocument getDoc() {
		return document;
	}

	private String destinationUsername;
	private String username;
	
	public Conversation(Client client, String destinationUsername)
	{
		this.client = client;
		this.username = LoginFrame.getUsername();
		this.destinationUsername = destinationUsername;
	}
	
	public String getDestinationUsername()
	{
		return destinationUsername;
	}
	
	public void start() 
	{
		initialize();
		setUpHandlers();
		//startReading();
	}
	
	public void startReading()
	{
		SwingWorker<Void, String> worker = new SwingWorker<Void, String>()
		{
			@Override
			protected Void doInBackground() throws Exception 
			{
				// TODO Auto-generated method stub
				while(true)
				{
					try
					{
						synchronized(Client.input)
						{
							lineReceived = (String) Client.input.readObject();
						}
						publish(lineReceived);
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
			
			protected void process(List<String> list)
			{
				writeTheMessage(lineReceived);
			}
			
		};
		worker.execute();
	}
	
	public void initialize()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(Conversation.class.getResource("/resources/ChatUp!.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(500,400));
		setTitle(destinationUsername);
		setBounds(100, 100, 650, 450);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{10, 0, 0, 0, 10, 0};
		gbl_contentPane.rowHeights = new int[]{10, 0, 0, 0, 10, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 50.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		gbc_scrollPane.gridwidth = 3;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		messages = new JTextPane();
		kit = new HTMLEditorKit()
		{
			 @Override 
		        public ViewFactory getViewFactory(){ 

		            return new HTMLFactory(){ 
		                public View create(Element e){ 
		                   View v = super.create(e); 
		                   if(v instanceof InlineView){ 
		                       return new InlineView(e){ 
		                           public int getBreakWeight(int axis, float pos, float len) { 
		                               return GoodBreakWeight; 
		                           } 
		                           public View breakView(int axis, int p0, float pos, float len) { 
		                               if(axis == View.X_AXIS) { 
		                                   checkPainter(); 
		                                   int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len); 
		                                   if(p0 == getStartOffset() && p1 == getEndOffset()) { 
		                                       return this; 
		                                   } 
		                                   return createFragment(p0, p1); 
		                               } 
		                               return this; 
		                             } 
		                         }; 
		                   } 
		                   else if (v instanceof ParagraphView) { 
		                       return new ParagraphView(e) { 
		                           protected javax.swing.SizeRequirements calculateMinorAxisRequirements(int axis, javax.swing.SizeRequirements r) { 
		                               if (r == null) { 
		                                     r = new javax.swing.SizeRequirements(); 
		                               } 
		                               float pref = layoutPool.getPreferredSpan(axis); 
		                               float min = layoutPool.getMinimumSpan(axis); 
		                               // Don't include insets, Box.getXXXSpan will include them. 
		                                 r.minimum = (int)min; 
		                                 r.preferred = Math.max(r.minimum, (int) pref); 
		                                 r.maximum = Integer.MAX_VALUE; 
		                                 r.alignment = 0.5f; 
		                               return r; 
		                             } 

		                         }; 
		                     } 
		                   return v; 
		                 } 
		             }; 
		         } 
		};
		messages.setEditorKit(kit);
		messages.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		messages.setBackground(new Color(255,255, 102));
		messages.setEditable(false);
		scrollPane.setViewportView(messages);
		
		document = new HTMLDocument();
		messages.setDocument(document);
		
		btnShowOlderMessages = new JButton("Show Older Messages");
		btnShowOlderMessages.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		btnShowOlderMessages.setBackground(new Color(255, 255, 102));
		GridBagConstraints gbc_btnShowOlderMessages = new GridBagConstraints();
		gbc_btnShowOlderMessages.anchor = GridBagConstraints.WEST;
		gbc_btnShowOlderMessages.insets = new Insets(0, 0, 5, 5);
		gbc_btnShowOlderMessages.gridx = 1;
		gbc_btnShowOlderMessages.gridy = 2;
		contentPane.add(btnShowOlderMessages, gbc_btnShowOlderMessages);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 3;
		gbc_scrollPane_1.gridwidth = 2;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
		writeMessage = new JTextArea();
		writeMessage.setLineWrap(true);
		writeMessage.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		writeMessage.setBackground(new Color(255, 255, 102));
		scrollPane_1.setViewportView(writeMessage);
		
		sendButton = new JButton("");
		sendButton.setFont(new Font("MS UI Gothic", Font.PLAIN, 15));
		sendButton.setBackground(new Color(255, 255, 102));
		ImageIcon imageIcon = new ImageIcon(Conversation.class.getResource("/resources/sendb.png"));
		sendButton.setOpaque(false);
		sendButton.setContentAreaFilled(false);
		sendButton.setBorderPainted(false);
		sendButton.setFocusPainted(false);
		sendButton.setIcon(imageIcon);
		
		GridBagConstraints gbc_sendButton = new GridBagConstraints();
		gbc_sendButton.fill = GridBagConstraints.BOTH;
		gbc_sendButton.insets = new Insets(0, 0, 5, 5);
		gbc_sendButton.gridx = 3;
		gbc_sendButton.gridy = 3;
		
		contentPane.add(sendButton, gbc_sendButton);
		
		inputMap = messages.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = messages.getActionMap();
		
		addWindowListener(new WindowAdapter()
	    {
	        @Override
	        public void windowClosing(WindowEvent e)
	        {
	            close();
	            e.getWindow().dispose();
	        }
	    });
		setVisible(true);
	}
	
	
	public void setUpHandlers()
	{
		ctrlEnterHandler();
		buttonAction();
		enterAction();
		btnShowOlderMessagesHandler();
	}
	
	public void btnShowOlderMessagesHandler()
	{
		btnShowOlderMessages.addActionListener(new ActionListener()
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
					SignUpClient sendObject = new SignUpClient();
					sendObject.setCode("getMessages");
					sendObject.setUsername(destinationUsername);
					client.output.writeObject(sendObject);
					return null;
				}
			};
			worker.execute();
			}
		});
	}
	public void ctrlEnterHandler()
	{
		Action newLine = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				writeMessage.append("\n");
			}
		};
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "newLine");
		actionMap.put("newLine", newLine);
	}
	
	public void buttonAction()
	{
		sendButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent event) 
			{
				SwingWorker<Void, String> worker = new SwingWorker<Void, String>()
				{
					@Override
					protected Void doInBackground() throws Exception 
					{
						SignUpClient sendObject = new SignUpClient();
						sendObject.setDestinationUsername(destinationUsername);
						lineSent = writeMessage.getText();
						writeMessage.setText("");
						sendObject.setCode(MESSAGEID);
						sendObject.setMessage(lineSent);
						Client.output.writeObject(sendObject);
						Client.output.flush();
						return null;
					}
					
					protected void done()
					{
						writeTheMessage(username + ": " + lineSent);
					}
				};
				worker.execute();
			}
		});
	}
	
	public void enterAction()
	{
		Action wrapper = new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	           sendButton.doClick();
	        }
	    };
		
	    KeyStroke keyStroke = KeyStroke.getKeyStroke("ENTER");
        Object actionKey = writeMessage.getInputMap(
                JComponent.WHEN_FOCUSED).get(keyStroke);
        writeMessage.getActionMap().put(actionKey, wrapper);
	}
	
	
	public void writeTheMessage(String message)
	{
		for (int i = 0; i < EmojiMap.emojis.size(); i++)
			if(message.contains(EmojiMap.emojis.get(i).getCode()))
			{
				String pic = "<img width=\"25\" height=\"25\" src =\"" + Conversation.class.getResource(EmojiMap.emojis.get(i).getPath()) + "\"/>";
				message = message.replace(EmojiMap.emojis.get(i).getCode(), pic);
			}
		
		String actualMessage = "<html><p style=\"font-size:15\">" + message + "</p></html>\n";
		try {
			kit.insertHTML(document, document.getLength(), actualMessage, 0, 0, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeOlderMessage(String message,int offset)
	{
		for (int i = 0; i < EmojiMap.emojis.size(); i++)
			if(message.contains(EmojiMap.emojis.get(i).getCode()))
			{
				String pic = "<img width=\"25\" height=\"25\" src =\"" + Conversation.class.getResource(EmojiMap.emojis.get(i).getPath()) + "\"/>";
				message = message.replace(EmojiMap.emojis.get(i).getCode(), pic);
			}
		
		String actualMessage = "<html><p style=\"font-size:15\">" + message + "</p></html>\n";
		try {
			kit.insertHTML(document, offset, actualMessage, 0, 0, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		FriendsFrame.removeMap(this);
	}
	
}