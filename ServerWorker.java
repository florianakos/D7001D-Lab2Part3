package lab.two.part.three;

import java.net.*;
import java.io.*;

public class ServerWorker implements Runnable {
	protected Socket socket = null;
	static int BUFSIZE=1024;
	
	// Constructor
	ServerWorker(Socket ss) {
		this.socket = ss;
	}
	
	// execution part of the thread
	public void run() {
		try {
			
			// read the command from the client (1st line)
			String clientCommand = null;
			BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));        
			clientCommand = in.readLine();
            
			// invoke internal function to execute this command
            String result = executeCommand(clientCommand);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            if (result != null) {
            	// if the execution gave valid result/output send it to the socket
            	out.println(result);
            	out.close();
            }
            
            // otherwise signal that something went wrong
            out.println("ERROR in remote command execution");
            out.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}
	}
	
	public static String executeCommand(String command) {
		// if the client sent an empty command return nothing...
		if (command.equals("")) {
			return null;
		}
		
		// create a small script to properly handle commands that have a pipe 
		String[] cmdTemplate = {
				"/bin/sh",
				"-c",
				command};

		// execution
		StringBuilder commandResult = new StringBuilder();
		String txt;
		try {
			// invoke the Runtime to execute the command
			Process process = Runtime.getRuntime().exec(cmdTemplate);
			
			// FOR WINDOWS ONLY: the .exec() String has to be prefixed with "cmd /c"
			// Process process = Runtime.getRuntime().exec("cmd /c" + command);
	        
			// Read the result of the execution
			BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
	        while ((txt = reader.readLine()) != null){
	        	commandResult.append(txt + "\n");
	        } 
	        // return the result to the calling function
	        return commandResult.toString();
	        
		} catch (IOException e) {	
			System.out.println(e.getMessage());
			return null;
		}
	}
}
