package Gym;

import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymServer;


public class Server extends UnicastRemoteObject implements GymServer{

/**
	 * 
	 */
	private static final long serialVersionUID = 8741232380634334159L;

/* 	KILL PORT WITH:	
 * 		lsof -i :10001
 *  		kill -9 PID
*/
	protected Server() throws RemoteException {
	}

	public static void main(String[] args) throws RemoteException {
		Registry registry = LocateRegistry.createRegistry(10001);
		Server server = new Server();

		try {
			Runtime.getRuntime().exec("rmiregistry 10001");
			registry.rebind("Server", server);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Server started.");

	}

	@Override
	public String networkToString() throws RemoteException {
		return null;
	}

	@Override
	public void printMessage(String message) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerClientA(String host, int port, String registryName) throws RemoteException {
		Registry registry = LocateRegistry.getRegistry(host, port);
        try {
			ClientA stub = (ClientA) registry.lookup(registryName);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
        System.out.println("CLient A registered.");
	}

	@Override
	public void registerClientB(String host, int port, String registryName) throws RemoteException {
		Registry registry = LocateRegistry.getRegistry(host, port);
        try {
			ClientB stub = (ClientB) registry.lookup(registryName);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
        System.out.println("CLient B registered.");
		
	}

	@Override
	public void setTeamA(Team arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTeamAReady(boolean arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTeamB(Team arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTeamBReady(boolean arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}