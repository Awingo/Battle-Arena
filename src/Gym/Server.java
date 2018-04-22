package Gym;

import java.rmi.registry.Registry;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.attacks.AbstractAttack;
import edu.uab.cs203.effects.StatusEffect;
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
		broadcastMessage(message);
	}

	@Override
	public String networkToString() throws RemoteException {
        String stats = "";
        if(this.getTeamA().size() != 0 || this.getTeamB().size() != 0) {
            for(int i = 0; i < this.getTeamA().size(); i++) {
                stats += this.getTeamA().get(i).toString() + "\n";
            }
            stats += "\n\n";
            for(int i = 0; i < getTeamB().size(); i++) {
                stats += getTeamB().get(i).toString();
            }
        }
        return stats;
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
		this.TeamA = team;
	}

	@Override
	public void setTeamAReady(boolean ready) throws RemoteException {
		this.teamAReady = ready;
	}

	@Override
	public void setTeamB(Team team) throws RemoteException {
		this.TeamB = team;
	}

	@Override
	public void setTeamBReady(boolean ready) throws RemoteException {
		this.teamBReady = ready;
	}


	@Override
	public void executeTurn() {
	    try {
            this.getClientA().networkTick();
            this.getClientB().networkTick();
            Objectmon omanA = this.getClientA().nextObjectmon();
            Objectmon omanB = this.getClientB().nextObjectmon();
            if (omanA != null && omanB != null) {
                this.printMessage(omanA.toString());
                this.printMessage(omanB.toString());
                AbstractAttack nextAttack = omanA.nextAttack();
                int damageDone;
                StatusEffect effect;
                if (nextAttack != null) {
                    damageDone = nextAttack.getDamage(omanB);
                    this.getClientA().networkApplyDamage(omanA, omanB, damageDone);
                    this.getClientB().addStatusEffectFromAttack(nextAttack, omanB);
                }

                if (omanB.isFainted()) {
                    this.printMessage(omanB + " fainted! Moving to next turn!");
                } else {
                    nextAttack = omanB.nextAttack();
                    if (nextAttack != null) {
                        damageDone = nextAttack.getDamage(omanA);
                        this.getClientB().networkApplyDamage(omanB, omanA, damageDone);
                        this.getClientA().addStatusEffectFromAttack(nextAttack, omanA);
                    }

                    if (omanA.isFainted()) {
                        this.printMessage(omanA + " fainted! Moving to next turn!");
                    }
                }
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void fight(int rounds) {
	    try {
            this.printMessage("Today's fight will be");
            this.printMessage(this.getTeamA().toString());
            this.printMessage("vs.");
            this.printMessage(this.getTeamB().toString());
            int roundCount = 0;

            while((this.getTeamA().canFight() || this.getTeamB().canFight()) && roundCount < rounds) {
                ++roundCount;
                this.printMessage("+++++++++++++");
                this.printMessage("Round " + roundCount + ": FIGHT!");
                this.executeTurn();
                this.printMessage("At the end of round " + roundCount + ":");
                this.printMessage(networkToString());
                this.printMessage("------------");
            }

            if (this.getTeamA().canFight()) {
                this.printMessage("Team A won!");
            } else if (this.getTeamB().canFight()) {
                this.printMessage("Team B won!");
            } else {
                this.printMessage("It was a draw!");
            }

            this.printMessage("Team A: " + this.getTeamA().toString());
            this.printMessage("Team B: " + this.getTeamB().toString());
        }
        catch (RemoteException e){
	        e.printStackTrace();
        }
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