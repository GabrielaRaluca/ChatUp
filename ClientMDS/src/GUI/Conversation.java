package GUI;
import client.*;
import server.SignUpClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.InputMap;
import javax.swing.JTextArea;
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
	public JScrollPane scrollPane;
	public JScrollPane scrollPane_1;
	public JTextArea writeMessage;
	public JTextArea messages;
	
	public String lineSent;
	public String lineReceived;
	public final String MESSAGEID = "message";
	
	public InputMap inputMap;
	public ActionMap actionMap;

	public Client client;
	private String destinationUsername;
	
	public Conversation(Client client, String destinationUsername)
	{
		this.client = client;
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
				messages.append(lineReceived + "\n");
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
		setBounds(100, 100, 613, 464);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.DARK_GRAY);
		setContentPane(contentPane);
		sendButton = new JButton("Send");
		sendButton.setBackground(Color.GRAY);
		scrollPane = new JScrollPane();
		scrollPane_1 = new JScrollPane();
		setVisible(true);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addGap(20))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
							.addGap(13))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
					.addGap(39)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(sendButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
					.addGap(26))
		);
		
		writeMessage = new JTextArea();
		writeMessage.setBackground(new Color(255, 255, 102));
		writeMessage.setWrapStyleWord(true);
		writeMessage.setLineWrap(true);
		
		scrollPane_1.setViewportView(writeMessage);
		
		messages = new JTextArea();
		messages.setBackground(new Color(255, 255, 102));
		messages.setWrapStyleWord(true);
		messages.setLineWrap(true);
		messages.setEditable(false);
		
		scrollPane.setViewportView(messages);
		contentPane.setLayout(gl_contentPane);
		
		inputMap = writeMessage.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = writeMessage.getActionMap();
		
		addWindowListener(new WindowAdapter()
	    {
	        @Override
	        public void windowClosing(WindowEvent e)
	        {
	            close();
	            e.getWindow().dispose();
	        }
	    });
	}
	
	public void setUpHandlers()
	{
		ctrlEnterHandler();
		buttonAction();
		enterAction();
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
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
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
						messages.append("Me: " + lineSent + "\n");
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
	
	public void showMessage(String message)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				messages.append(message + "\n");	
			}
		});
	}
	
	public void close()
	{
		FriendsFrame.removeMap(this);
	}
	
}