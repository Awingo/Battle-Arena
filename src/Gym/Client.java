package Gym;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;

public class Client implements GymClient, Serializable{

	private ArrayList<Server> server;
	/**
	 * 
	 */
	private static final long serialVersionUID = -7921006151087392344L;

	private Client() {
		this.server = new ArrayList<Server>();
	}


	public static void main(String[] args) throws RemoteException {
		Client client = new Client();
		try {
			Runtime.getRuntime().exec("rmiregistry 1354");
			Registry registry = LocateRegistry.createRegistry(1354);
			registry.bind("Client", client);
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		try {
			Registry serverRegistry = LocateRegistry.getRegistry("localhost", 10001);
			GymServer server = (GymServer) serverRegistry.lookup("Server");
			server.registerClientA("localhost", 1354, "Client");
			server.registerClientB("localhost", 1354, "Client");
		} 
		catch (NotBoundException e) {
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
	public void printMessage(String message) throws RemoteException {
		System.out.println(message);
	}

	@Override
	public void setTeam(Team arg0) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
