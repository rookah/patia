package tempeteMentale;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PlanServer {

	public static void main(String[] args) {
		PlanGenerator p = new PlanGenerator();
		String host = "192.168.1.33";
		System.setProperty("java.rmi.server.hostname", host);
		try {
			PlanGeneratorInterface p_stub = (PlanGeneratorInterface) UnicastRemoteObject.exportObject(p, 0);
			Registry registry = LocateRegistry.createRegistry(8004);
			registry.bind("PlanService", p_stub);
			System.out.println("Server ready");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
