package tempeteMentale;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlanGeneratorInterface extends Remote{
	public String getNextOperation() throws RemoteException;
	public void newPlan() throws RemoteException;
	public void newPlan(String starting_node) throws RemoteException;
	public Point getGoalNode() throws RemoteException;
	public Point getPositionsFromPuck(String destination) throws RemoteException;
	public Point getPositionsFromNode(String nodeLabel) throws RemoteException;
}
