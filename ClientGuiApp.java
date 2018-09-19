package lab.two.part.three;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientGuiApp {
	
	// global variables for GUI elements (not strictly necessary, but convenient)
	public static JTextArea ta;
	public static JTextField urlField;
	public static JTextField portField;
	public static JFrame mainWindow;
	public static JPanel panelOne;
	public static JPanel panelTwo;
	public static JLabel urlLabel;
	public static JLabel cmdLabel;
	public static JTextField cmdField;
	public static JButton send;
	public static JButton looping;
	public static JButton clear;
	public static JScrollPane sp;

	
	public static void main(String args[]) {
		// create the pool of threads
		ExecutorService threadPool = Executors.newFixedThreadPool(20);
		
		// create main window frame
		mainWindow = new JFrame("Threaded TCP Client");
	    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainWindow.setSize(800, 600);

	    //Creating the panel at bottom and adding components
	    panelOne = new JPanel(); // the panel is not visible in output
	    panelTwo = new JPanel();
	    urlLabel = new JLabel("Enter Server URL and port");
	    cmdLabel = new JLabel("Enter command to execute");
	    urlField = new JTextField("localhost", 10); // accepts upto 10 characters
	    portField = new JTextField("7777", 10); // accepts upto 10 characters
	    cmdField = new JTextField("ping 8.8.8.8 -c5", 10); // accepts upto 10 characters
	     
	    // Text Area at the Center
	    ta = new JTextArea();
	    DefaultCaret caret = (DefaultCaret)ta.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    ta.setEditable(false);
	    
	    // Create ScrollingPane
	    sp = new JScrollPane(ta); 
	    sp.setBounds(5,5,400,400);
	    sp.setPreferredSize(new Dimension(200, 250));
	    sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    // Assign function to Button CLEAR
	    clear = new JButton("Clear Text");
	    clear.addActionListener(new ActionListener() {
	    	@Override
	           public void actionPerformed(ActionEvent e) {
	               ta.setText("");
	           }
	    });
	    
	    // Assign function to Button SEND
	    send = new JButton("Send Command");
	    send.addActionListener(new ActionListener() {
	           @Override
	           public void actionPerformed(ActionEvent e) {
		           	try {
		           		// gather info of destination and command
		           		String address = urlField.getText();
		           		String port = portField.getText();
		           		String command = cmdField.getText();
		           		
		           		// if gathered info is ok, create thread for handling it in TCHClientApp.class
		           		if (!address.equals("") && !port.equals("") && !command.equals("")) {
		           			TCPClientApp obj_worker = new TCPClientApp(address, Integer.parseInt(port), command, ta);
			           		threadPool.execute(obj_worker);
		           		}
		           			           		
					} catch (Exception e1) {
						e1.printStackTrace();
					}
	           }
	    });     
	       
	    // Assign function to button to start looping
	    looping = new JButton("Start Looping");
	    looping.addActionListener(new ActionListener() {
	           @Override
	           public void actionPerformed(ActionEvent e) {
	               try {
	            	   // Create object worker to execute the looping in new thread
	            	   LoopingApp obj_worker = new LoopingApp(30, ta);
	            	   threadPool.execute(obj_worker);
	               } catch (Exception e1) {
	            	   System.out.println(e1.getMessage());
	            	   return;
	               }
	           }
	    });
	     
	    // Adding GUI Components to the frame
	    panelOne.add(urlLabel);
	    panelOne.add(urlField);
	    panelOne.add(portField);
	    panelTwo.add(cmdLabel);
	    panelTwo.add(cmdField);
	    panelTwo.add(send);
	    panelTwo.add(looping);
	    panelTwo.add(clear);
	    mainWindow.getContentPane().add(BorderLayout.CENTER, sp);
	    mainWindow.getContentPane().add(BorderLayout.NORTH, panelOne);
	    mainWindow.getContentPane().add(BorderLayout.SOUTH, panelTwo);
	    mainWindow.setVisible(true);
	    sp.setVisible(true);

	}
}
