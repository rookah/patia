package tempeteMentale;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class EV3Client {
	
	private static DatagramSocket dsocket = null;
	
  public static int[] getPalets() 
  {
    try 
    {
      int port = 8888;
      
      
      // Create a socket to listen on the port.
      if (dsocket == null) {
    	  dsocket = new DatagramSocket(port);
      }
      // Create a buffer to read datagrams into. If a
      // packet is larger than this buffer, the
      // excess will simply be discarded!
      byte[] buffer = new byte[2048];

      // Create a packet to receive data into the buffer
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

      // Now loop forever, waiting to receive packets and printing them.
      while (true) 
      {
        // Wait to receive a datagram
        dsocket.receive(packet);

        // Convert the contents to a string, and display them
        String msg = new String(buffer, 0, packet.getLength());
        //System.out.println(packet.getAddress().getHostName() + ": "
        //    + msg);
        
        String[] palets = msg.split("\n");
        int[] posPalets = new int[palets.length * 2];
        
        for (int i = 0; i < palets.length; i++) 
        {
        	String[] coord = palets[i].split(";");
        	int x = Integer.parseInt(coord[1]);
        	int y = Integer.parseInt(coord[2]);
        	posPalets[i*2] = x;
        	posPalets[i*2+1] = y;
        }
        

        // Reset the length of the packet before reusing it.
        packet.setLength(buffer.length);
        return posPalets;
      }
     
    } 
    catch (Exception e) 
    {
      System.err.println(e);
    }
	return null;
  }
 
}
