package Gym;

import java.rmi.registry.Registry;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;
import edu.uab.cs203.network.NetworkGym;

// TODO: server messages prints to server itself

public class Server extends UnicastRemoteObject implements Serializable, GymServer, NetworkGym {
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
		this.clients = new ArrayList<>();
	}
	
	@Override
	public void printMessage(String message) throws RemoteException {
		System.out.println("Recieved message from client: " + message);
		broadcastMessage(message);
	}


	public void broadcastMessage(String message) {
		for (GymClient c : this.clients) {
			try {
				c.printMessage(message);
			} catch (RemoteException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	

	@Override
	public String networkToString() throws RemoteException {
		return null;
	}

	

	@Override
	public void registerClientA(String host, int port, String registryName) throws RemoteException {
		System.out.println("Registering client: " + host + ":" + port + ":" + registryName);
		try {
			GymClient clientA;
			clientA  = (GymClient) LocateRegistry.getRegistry(host, port).lookup(registryName);
			this.clients.add(clientA);
			clientA.printMessage("Client A registered.");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void registerClientB(String host, int port, String registryName) throws RemoteException {
		System.out.println("Registering client: " + host + ":" + port + ":" + registryName);
		try {
			GymClient clientB;
			clientB = (GymClient) LocateRegistry.getRegistry(host, port).lookup(registryName);
			this.clients.add(clientB);
			clientB.printMessage("Client B registered.");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws RemoteException {
		try {
			Server server = new Server();
			Runtime.getRuntime().exec("rmiregistry 10001");
			Registry registry = LocateRegistry.createRegistry(10001);
			registry.bind("Server", server);
			System.out.println("Server started.");
		}

		catch (Exception e) {
			e.printStackTrace();

		}

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


	@Override
	public void executeTurn() {
		// TODO Auto-generated method stub

	}


	@Override
	public void fight(int arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public GymClient getClientA() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public GymClient getClientB() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Team getTeamA() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Team getTeamB() {
		// TODO Auto-generated method stub
		return null;
	}


}