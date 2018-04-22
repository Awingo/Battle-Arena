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
import edu.uab.cs203.lab09.Lab09HashmonGym;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;
import edu.uab.cs203.network.NetworkGym;


public class Server extends UnicastRemoteObject implements Serializable, GymServer, NetworkGym{
	private ArrayList<GymClient> clients;
	private GymClient ClientA;
	private GymClient ClientB;
	private Team TeamA;
	private Team TeamB;
	private boolean teamAReady;
	private boolean teamBReady;


	public Server() throws RemoteException {
		this.clients = new ArrayList<>();
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
	public void printMessage(String message) throws RemoteException {
		System.out.println("Recieved message from client: " + message);
		broadcastMessage(message);
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
			this.ClientA  = (GymClient) LocateRegistry.getRegistry(host, port).lookup(registryName);
			this.clients.add(this.ClientA);
			this.ClientA.printMessage("Client A registered.");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void registerClientB(String host, int port, String registryName) throws RemoteException {
		System.out.println("Registering client: " + host + ":" + port + ":" + registryName);
		try {
			GymClient clientB;
			this.ClientB = (GymClient) LocateRegistry.getRegistry(host, port).lookup(registryName);
			this.clients.add(this.ClientB);
			this.ClientB.printMessage("Client B registered.");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setTeamA(Team team) throws RemoteException {
		team = this.TeamA;
	}

	@Override
	public void setTeamAReady(boolean ready) throws RemoteException {
		this.teamAReady = ready;
	}

	@Override
	public void setTeamB(Team team) throws RemoteException {
		team = this.TeamB;
	}

	@Override
	public void setTeamBReady(boolean ready) throws RemoteException {
		this.teamBReady = ready;
	}


	@Override
	public void executeTurn() {

	}

	@Override
	public void fight(int arg0) {

	}

	@Override
	public GymClient getClientA() {
		return this.ClientA;
	}

	@Override
	public GymClient getClientB() {
		return this.ClientB;
	}

	@Override
	public Team getTeamA() {
		try {
			return this.ClientA.getTeam();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Team getTeamB() {
		try {
			return this.ClientB.getTeam();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
}