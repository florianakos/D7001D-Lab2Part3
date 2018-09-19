package lab.two.part.three;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TCPServerApp {
	public static Logger logger;
	
	public static void main(String[] args) throws Exception {
    	// Define logger component
    	Logger logger  = Logger.getLogger("log");
        	
    	// handle the getting of port number as first argument
    	if (args.length != 2){
    		logger.severe("Arguments needed: [PORT] [LOGGING_LEVEL]");
			throw new IllegalArgumentException("Arguments needed: [PORT] [LOGGING_LEVEL]");
		}
    	
    	for(int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    	
    	//set logging level
    	
    	switch (args[1]) {
		case "SEVERE":
			logger.setLevel(Level.SEVERE);
			System.out.println("Logging set to SEVERE");
			break;
		case "WARNING":
			logger.setLevel(Level.WARNING);
			System.out.println("Logging set to WARNING");
			break;
		case "INFO":
			logger.setLevel(Level.INFO);
			System.out.println("Logging set to INFO");
			break;
		case "CONFIG":
			logger.setLevel(Level.CONFIG);
			System.out.println("Logging set to CONFIG");
			break;
		case "FINE":
			logger.setLevel(Level.FINE);
			System.out.println("Logging set to FINE");
			break;
		case "FINER":
			logger.setLevel(Level.FINER);
			System.out.println("Logging set to FINER");
			break;
		case "FINEST":
			logger.setLevel(Level.FINEST);
			System.out.println("Logging set to FINEST");
			break;
		}
    	
    	// create threadpool executor service with 20 threads
    	ExecutorService threadPool = Executors.newFixedThreadPool(20); 
    	
    	
    	// create server listener socket
    	try (ServerSocket listener = new ServerSocket(Integer.parseInt(args[0]))){
			
    		// output some general info about server params
			System.out.println("\r\nRunning Server: " + 
	                "Host=" + listener.getInetAddress().getHostAddress() + 
	                " Port=" + listener.getLocalPort());
			
			
			for (;;) {
				// start accepting new clients in infinite loop
				Socket client = listener.accept();
				
				// output some info about newly connected clients
				String clientAddress = client.getInetAddress().getHostAddress();
	            System.out.println("\r\nNew connection from " + clientAddress);
				
	            // start new thread to handle client that just connected
	            ServerWorker obj_worker = new ServerWorker(client);
				threadPool.execute(obj_worker);
				
				// output some extra info about current state of threadpool
				logger.info(threadPool.toString());				
			}
		} catch (IOException e) {
			threadPool.shutdown();
			// SEVERE level log added to signify some issues with sockets
			logger.severe(e.getMessage());
			return;
		}
    }
}