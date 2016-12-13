package de.uniba.wiai.lspi.chord.service.impl;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.com.CommunicationException;
import de.uniba.wiai.lspi.chord.com.Node;

public class AsyncBroadcast extends Thread {
	
	
	Node node = null;
	Broadcast broadcast = null;
	
	public AsyncBroadcast(Node node, Broadcast broadcast) {
		this.node = node;
		this.broadcast = broadcast;
	}
	
	public void run() {
		
        
            try {
            	node.broadcast(broadcast);
//                System.out.println("Broadcast sent from "+ getNodeURL() + " with transaction ID: " + info.getTransaction());
            } catch (CommunicationException e) {
                e.printStackTrace();
            }
	}
	

}
