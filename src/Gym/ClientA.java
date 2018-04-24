package Gym;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.ObjectmonNameGenerator;
import edu.uab.cs203.Team;
import edu.uab.cs203.lab05.BasicTeam;
import edu.uab.cs203.lab09.Hashmon;
import edu.uab.cs203.lab09.Lab09HashmonGym;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;
import edu.uab.cs203.network.NetworkGym;

public class ClientA extends UnicastRemoteObject implements GymClient, Serializable{

	static Team<Objectmon> teamA;

	private ClientA() throws RemoteException{
	}


    public static void main(String[] args) throws RemoteException {
		try {
			int clientPort = 9998;
			int serverPort = 10001;
			String serverHost = "localhost";
			GymClient clientA = new ClientA();

			Runtime.getRuntime().exec("rmiregistry " + clientPort);
			Registry registry = LocateRegistry.createRegistry(clientPort);
			registry.bind("Client A", clientA);

			Registry serverRegistry = LocateRegistry.getRegistry(serverHost, serverPort);
			GymServer server = (GymServer) serverRegistry.lookup("Server");

			server.registerClientA("localhost", clientPort, "Client A");
			Scanner s = new Scanner(System.in);

			System.out.println("List the hashmon you want on your team.. ^_^");
			String[] names = s.nextLine().split(", ");
			teamA = new BasicTeam<>("Team A", names.length);
			Hashmon.loadObjectdex("objectdex.txt");
			for (String name : names) {
				teamA.add(new Hashmon(name));
			}
			server.printMessage(teamA.toString());
			server.setTeamAReady(true);
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
    public Team<Objectmon> getTeam() {
//		this.teamA = new BasicTeam<>("Team A", 3);
//		try {
//			Hashmon.loadObjectdex("objectdex.txt");
//			for (int i = 0; i < this.teamA.getMaxSize(); ++i) {
//				Hashmon hmon = new Hashmon(ObjectmonNameGenerator.nextName());
//				this.teamA.add(hmon);
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
		return this.teamA;
    }
    @Override
    public Objectmon networkApplyDamage(Objectmon first, Objectmon second, int damage) throws RemoteException {
		second.setHp(second.getHp() - damage);
		return second;
    }
    @Override
    public void networkTick() throws RemoteException {
		this.getTeam().tick();
    }
    @Override
    public Objectmon nextObjectmon() throws RemoteException {
        return this.getTeam().nextObjectmon();
    }
    @Override
    public void printMessage(String message) throws RemoteException {
		System.out.println(message);
	}
    @Override
    public void setTeam(Team team) throws RemoteException {
        team = this.teamA;
    }
}

