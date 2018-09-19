package lab.two.part.three;

import javax.swing.JTextArea;

public class LoopingApp implements Runnable {
	// need to know how many times to loop
	public int numberOfLoops;
	public JTextArea ta;
		
	// create an instance with the number of loops that needs to be done
	LoopingApp (int numberOfLoops, JTextArea ta) {
		this.numberOfLoops = numberOfLoops;
		this.ta = ta;
	}
	
	// a method needed for the threading mechanism to work 
	public void run() {
		for (int i= 0; i < this.numberOfLoops; i++) {
			try {
			    Thread.sleep(100);
			    ta.append("."+i+"\n");
			} catch(InterruptedException e) {
			    System.out.println("got interrupted!");
			}
		}
	}
}