package Gym;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;

public class ClientB implements GymClient{

    private ClientB() {}

    public static void main(String[] args) throws RemoteException {
    	String host = "localhost";
        int port = 9997;
        Registry registry = LocateRegistry.createRegistry(9997);

    	try {
    		Runtime.getRuntime().exec("rmiregistry 9997");

    		registry.rebind("Client B", new ClientB());
    	} 
    	catch (Exception e) {
    		e.printStackTrace();
    		System.exit(-1);
    	}
    	
    	Registry ServerRegistry = LocateRegistry.getRegistry("localhost", 10001);;
        try {
			GymServer stub = (GymServer) ServerRegistry.lookup("Server");
			stub.registerClientB("localhost", 9997, "Client B");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
        
    }

	@Override
	public Team<Objectmon> getTeam() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Objectmon networkApplyDamage(Objectmon arg0, Objectmon arg1, int arg2) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void networkTick() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Objectmon nextObjectmon() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printMessage(String arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTeam(Team arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}