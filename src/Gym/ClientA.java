package Gym;


import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;

public class ClientA implements GymClient, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -530516955783405853L;

	private ClientA() {}
    

    public static void main(String[] args) throws RemoteException {
		try {			
			Registry registry = LocateRegistry.createRegistry(9998);
			Runtime.getRuntime().exec("rmiregistry 9998");
			
			ClientA clientA = new ClientA();
			registry.rebind("Client A", clientA);


			Registry ServerRegistry = LocateRegistry.getRegistry("localhost", 10001);;

			GymServer stub = (GymServer) ServerRegistry.lookup("Server");
			stub.registerClientA("localhost", 9998, "Client A");
			
			Scanner s = new Scanner(System.in);
			while(true) {
				String message = s.nextLine();
				stub.printMessage(message);
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