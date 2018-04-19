package Gym;

import java.rmi.registry.Registry;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;


public class Server extends UnicastRemoteObject implements GymServer, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 808166874398061280L;
	
	private ArrayList<GymClient> clients;

/* 	KILL PORT WITH:	
 * 	lsof -i :10001
 *  	kill -9 PID
*/
	public Server() throws RemoteException {
		this.clients = new ArrayList<GymClient>();
	}
	

	public static void main(String[] args) throws RemoteException {
		try {
			Server server = new Server();
			Runtime.getRuntime().exec("rmiregistry 10001");
			Registry registry = LocateRegistry.createRegistry(10001);
			registry.bind("Server", server);
		}
		catch (Exception e) {
			e.printStackTrace();
		
		}
		System.out.println("Server started.");

	}
	
	public void clearClients() {
		this.clients.clear();
	}
	
	@Override
	public String networkToString() throws RemoteException {
		return null;
	}

	@Override
	public void printMessage(String message) throws RemoteException {
		for (GymClient chat : this.clients) {
			try {
				chat.printMessage(message);
			}
			catch(RemoteException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
		}
		
	}

	@Override
	public void registerClientA(String host, int port, String registryName) throws RemoteException {
        try {
        		Client client = (Client) LocateRegistry.getRegistry(host, port).lookup(registryName);
            this.clients.add(client);

		} catch (NotBoundException e) {
			e.printStackTrace();
		}
        System.out.println("CLient A registered.");
	}

	@Override
	public void registerClientB(String host, int port, String registryName) throws RemoteException {
        try {
    			Client client = (Client) LocateRegistry.getRegistry(host, port).lookup(registryName);
             this.clients.add(client);
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