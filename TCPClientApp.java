package lab.two.part.three;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextArea;

// has to implement Runnable so that it can be run on ThreadPool
public class TCPClientApp implements Runnable {
	
	//reference to main thread GUI element, so it can be updated from this thread
	public JTextArea ta;
	
	// needs a socket and a command to execute on the remote end
    protected Socket socket = null;
    protected String command = null;
    static int BUFSIZE=1024;  
    
    // Constructor for instantiating inside a new thread
    public TCPClientApp(String address, int serverPort, String command, JTextArea ta) throws Exception {
        this.socket = new Socket(address, serverPort);
        this.command = command;
        this.ta = ta;
    }

    // will be executed in the thread
	@Override
	public void run()  {
		// try-catch to handle exceptions
        try {
        	// ========= First part: Sends the command ============================
        	PrintWriter outBytes = new PrintWriter(socket.getOutputStream(), true);
            outBytes.println(this.command);
            outBytes.flush();
            //=====================================================================
            
            // ========= Second part: Reads the reply ==================================================
            String dataFeed = null;
            StringBuilder sb = new StringBuilder();
            
            // read the bytes from the input stream of the socket line by line
            BufferedReader inBytes = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            while ( (dataFeed = inBytes.readLine()) != null ) {
                sb.append(new String(dataFeed));
                sb.append("\n");
            }
            ta.append(sb.toString());
            return;
            //==========================================================================================
            
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        	return;
        }
		
	}
}