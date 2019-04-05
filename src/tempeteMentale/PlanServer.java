package tempeteMentale;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PlanServer {

	public static void main(String[] args) {
		PlanGenerator p = new PlanGenerator();
		try {
			PlanGeneratorInterface p_stub = (PlanGeneratorInterface) UnicastRemoteObject.exportObject(p, 0);
			
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("PlanService", p_stub);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
