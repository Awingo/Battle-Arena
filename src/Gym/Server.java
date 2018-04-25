package Gym;

import java.rmi.registry.Registry;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.attacks.AbstractAttack;
import edu.uab.cs203.effects.AbstractStatusEffect;
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
        	stats += teamToString(this.getTeamA()) + "\n";
        	stats += teamToString(this.getTeamB());
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
		if (this.teamAReady && this.teamBReady) {
		    fight(100);
        }
	}

	@Override
	public void setTeamB(Team team) throws RemoteException {
		this.TeamB = team;
	}

	@Override
	public void setTeamBReady(boolean ready) throws RemoteException {
		this.teamBReady = ready;
        if (this.teamAReady && this.teamBReady) {
            fight(100);
        }
	}


	@Override
	public void executeTurn() {
	    try {
            this.getClientA().networkTick();
            this.getClientB().networkTick();
            Objectmon omanA = this.getClientA().nextObjectmon();
            Objectmon omanB = this.getClientB().nextObjectmon();
            if (omanA != null && omanB != null) {
				AbstractAttack nextAttack = this.getClientA().nextAttack(omanA);
				this.printMessage(objectmonToString(omanA));
                this.printMessage(objectmonToString(omanB));
                int damageDone;
                StatusEffect effect;
                if (nextAttack != null) {
                    damageDone = nextAttack.getDamage(omanB);
                    this.getClientA().networkApplyDamage(omanA, omanB, damageDone);
                    this.getClientB().addStatusEffectFromAttack(nextAttack, omanB);
                }

                if (omanB.isFainted()) {
                    this.printMessage(omanB.getName() + " fainted! Moving to next turn!");
                } else {
                    nextAttack = this.getClientB().nextAttack(omanB);
                    if (nextAttack != null) {
                        damageDone = nextAttack.getDamage(omanA);
                        this.getClientB().networkApplyDamage(omanB, omanA, damageDone);
                        this.getClientA().addStatusEffectFromAttack(nextAttack, omanA);
                    }

                    if (omanA.isFainted()) {
                        this.printMessage(omanA.getName() + " fainted! Moving to next turn!");
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
            this.printMessage(teamToString(this.getTeamA()));
            this.printMessage("vs.");
            this.printMessage(teamToString(this.getTeamB()));
            int roundCount = 0;

            while((this.getTeamA().canFight() || this.getTeamB().canFight()) && roundCount < rounds) {
                ++roundCount;
                this.printMessage("+++++++++++++");
                this.printMessage("Round " + roundCount + ": FIGHT!\n");
                this.executeTurn();
                this.printMessage("\nAt the end of round " + roundCount + ":\n");
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

            this.printMessage("Team A: " + teamToString(this.getTeamA()));
            this.printMessage("Team B: " + teamToString(this.getTeamB()));
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

	public String teamToString(Team t) {
		String s = t.getName();
		Iterator it = t.listIterator();
		while(it.hasNext()) {
			Objectmon o = (Objectmon) it.next();
			s += "\n" + objectmonToString(o);
		}
		return s;
	}

	public String objectmonToString(Objectmon o) {
		String s = "";
		String attacks = "";
		String effects = "";
		for (AbstractAttack a : o.getAttacks()) {
			attacks += "\t\n" + a.getName();
		}
		for (StatusEffect e : o.getStatusEffects()) {
			effects += "\t\n" + "Effect Type" + e.getClass().getName() +
					"\t\n" + "Ticks Remaining: " + e.getTicksRemaining() +
					"\t\n" + "Prevent attack: " + e.preventAttack();
		}
		s += "\n" + "Name: " + o.getName() + "\n"
				+ "Hp: " + o.getHp() + "\n"
				+ "Stamina: " + o.getStamina() + "\n"
				+ "Weight: " + o.getWeight() + "\n"
				+ "Attacks: " + attacks + "\n"
				+ "Status Effects: " + effects;

		return s;
	}

}