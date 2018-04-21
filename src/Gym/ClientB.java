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
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;

public class ClientB extends UnicastRemoteObject implements GymClient, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3971860713529284609L;


	private ClientB() throws RemoteException{}
    

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
			GymServer server = (GymServer) serverRegistry.lookup("Server");
			
			server.registerClientB("localhost", clientPort, "Client B");
			
			Scanner s = new Scanner(System.in);
			while(true) {
				String message = s.nextLine();
				server.printMessage(message);

			}
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
        return this.getTeam();
    }
    @Override
    public Objectmon networkApplyDamage(Objectmon first, Objectmon second, int damage) throws RemoteException {
        int afterAttackHP = second.getHP()-damage;
        second.setHp(afterAttackHP);
        return second;
    }
    @Override
    public void networkTick() throws RemoteException {
    }
    @Override
    public Objectmon nextObjectmon() throws RemoteException {
        return getTeam().nextObjectmon();
    }
    @Override
    public void printMessage(String message) throws RemoteException {
    		System.out.println("Chat message: " + message);    
    	}
    @Override
    public void setTeam(Team arg0) throws RemoteException {
        arg0 = this.getTeam();
    }
}