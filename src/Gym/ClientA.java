package Gym;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;

public class ClientA implements GymClient{

    private ClientA() {}

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1354);
        ClientA clientA = new ClientA();
    	try {
    		Runtime.getRuntime().exec("rmiregistry 1354");

    		registry.rebind("Client A", clientA);
    	} 
    	catch (Exception e) {
    		e.printStackTrace();
    		System.exit(-1);
    	}
    	
    	
    	Registry ServerRegistry = LocateRegistry.getRegistry("localhost", 10001);;
        try {
			GymServer server = (GymServer) ServerRegistry.lookup("Server");
			server.registerClientA("localhost", 1354, "Client A");
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