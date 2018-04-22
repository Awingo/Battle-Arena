package Gym;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.lab05.BasicTeam;
import edu.uab.cs203.lab09.Lab09HashmonGym;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;

public class ClientB extends UnicastRemoteObject implements GymClient, Serializable{
	Team<Objectmon> team;

	private ClientB() throws RemoteException{
		this.team = new BasicTeam<>();

	}
    

    public static void main(String[] args) throws RemoteException {
		try {			
			int clientPort = 9997;
			int serverPort = 10001;
			String serverHost = "localhost";
			GymClient clientB = new ClientB();

			Runtime.getRuntime().exec("rmiregistry " + clientPort);
			Registry registry = LocateRegistry.createRegistry(clientPort);
			registry.bind("Client B", clientB);

			Registry serverRegistry = LocateRegistry.getRegistry(serverHost, serverPort);
			Server server = (Server) serverRegistry.lookup("Server");
			
			server.registerClientB("localhost", clientPort, "Client B");

		} 
		catch (NotBoundException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

}

	@Override
    public Team<Objectmon> getTeam() throws RemoteException {
        return this.team;
    }
    @Override
    public Objectmon networkApplyDamage(Objectmon first, Objectmon second, int damage) throws RemoteException {
		if (!first.isFainted()) {
			second.setHp(second.getHp() - damage);
		}
		return second;
    }
    @Override
    public void networkTick() throws RemoteException {
		this.team.tick();
    }
    @Override
    public Objectmon nextObjectmon() throws RemoteException {
        return this.team.nextObjectmon();
    }
    @Override
    public void printMessage(String message) throws RemoteException {
    		System.out.println("Chat message: " + message);    
    	}
    @Override
    public void setTeam(Team team) throws RemoteException {
        team = this.team;
    }
}