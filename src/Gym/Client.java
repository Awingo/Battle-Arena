package Gym;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.ObjectmonNameGenerator;
import edu.uab.cs203.Team;
import edu.uab.cs203.attacks.AbstractAttack;
import edu.uab.cs203.attacks.BasicAttack;
import edu.uab.cs203.lab05.BasicTeam;
import edu.uab.cs203.lab08.*;
import edu.uab.cs203.lab09.Hashmon;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements GymClient, Serializable {

    static Team<Objectmon> teamA;
    static Team<Objectmon> teamB;


    public Client() throws RemoteException {
    }

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry("localhost", 10001);
        System.out.println("Which would you like to be.. \n1. Team A \n2. Team B\n");
        Scanner sc = new Scanner(System.in);
        String ans = sc.nextLine();


        try {
            GymServer server = (GymServer) registry.lookup("Server");
            GymClient clientA = new Client();
            GymClient clientB = new Client();

            Hashmon.loadObjectdex("objectdex.txt");


            if (ans.equals("1")) {

                int clientPort = 9998;
                int serverPort = 10001;
                String serverHost = "localhost";

                Runtime.getRuntime().exec("rmiregistry " + clientPort);
                Registry clientAregistry = LocateRegistry.createRegistry(clientPort);
                clientAregistry.rebind("Client A", clientA);

                Registry serverRegistry = LocateRegistry.getRegistry(serverHost, serverPort);

                server.registerClientA("localhost", clientPort, "Client A");
                teamA = new BasicTeam<>("Team A", 3);
                for (int i = 0; i < teamA.getMaxSize(); ++i) {
                    Hashmon hmon = new Hashmon(ObjectmonNameGenerator.nextName());
                    teamA.add(hmon);
                }
                if (teamB != null){
                    server.printMessage(teamA.toString());
                }
                else
                    System.out.println("Waiting on the other client to choose their team...");

                server.setTeamAReady(true);

            } else if (ans.equals("2") ) {
                int clientPort = 9997;
                int serverPort = 10001;
                String serverHost = "localhost";

                Runtime.getRuntime().exec("rmiregistry " + clientPort);
                Registry clientBregistry = LocateRegistry.createRegistry(clientPort);
                clientBregistry.rebind("Client B", clientB);

                Registry serverRegistry = LocateRegistry.getRegistry(serverHost, serverPort);

                server.registerClientB("localhost", clientPort, "Client B");
                teamB = new BasicTeam<>("Team B", 3);
                for (int i = 0; i < teamB.getMaxSize(); ++i) {
                    Hashmon hmon = new Hashmon(ObjectmonNameGenerator.nextName());
                    teamB.add(hmon);
                }
                if (teamA != null){
                    server.printMessage(teamB.toString());
                }
                else
                    System.out.println("Waiting on the other client to choose their team...");

                server.setTeamBReady(true);
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (EOFException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Team<Objectmon> getTeam() {
        if (this.teamA == null){
            return this.teamB;
        }if (this.teamB == null){
            return this.teamA;
        }
        return null;
    }


    @Override
    public Objectmon networkApplyDamage(Objectmon first, Objectmon second, int damage) throws RemoteException {
        second.setHp((second.getHp()-damage));
        return second;
    }

    @Override
    public void networkTick() throws RemoteException {
        this.getTeam().nextObjectmon().tick();
    }

    @Override
    public AbstractAttack nextAttack(Objectmon objectmon) throws RemoteException{
        AbstractAttack a = null;
        Scanner s = new Scanner(System.in);
        System.out.println("\nWhich attack do you want use?");
        System.out.println("1. Basic\n" +
                            "2. Badly Poisoned\n" +
                            "3. Poisoned\n" +
                            "4. Frozen\n" +
                            "5. Paralyzed\n" +
                            "6. Sleep\n");
        String attack = s.nextLine();
        if (attack.equals("1")) {
            a = new BasicAttack(objectmon, "Basic Attack");
        }
        else if (attack.equals("2")) {
            a = new BadPoisonAttack(objectmon, "Badly Poisoned Attack");
        }
        else if (attack.equals("3")) {
            a = new PoisonAttack(objectmon, "Poisoned Attack");
        }
        else if (attack.equals("4")) {
            a = new FreezingAttack(objectmon, "Frozen Attack");
        }
        else if (attack.equals("5")) {
            a = new ParalyzingAttack(objectmon, "Paralyzed Attack");
        }
        else if (attack.equals("6")) {
            a = new SleepAttack(objectmon, "Sleep Attack");
        }
        objectmon.getAttacks().add(a);
        return a;
    }

    @Override
    public Objectmon nextObjectmon() throws RemoteException {
        Scanner s = new Scanner(System.in);
        System.out.println("Would you like to choose which objectmon fights this turn? [Y/N]");
        if (s.next().equals("Y")) {
            System.out.println("Which objectmon do you want to fight this turn?");
            for (Objectmon ob : getObjectmons()) {
                System.out.println(ob.getName());
            }

            String objectmon = s.nextLine();

            Iterator it = this.getTeam().listIterator();
            while (it.hasNext()) {
                Objectmon o = (Objectmon) it.next();
                if (o.getName().equals(objectmon)) {
                    return o;
                }
            }
            System.out.println("That objectmon is not in the team, pick again.");
            return nextObjectmon();
        }
        else {
            return this.getTeam().nextObjectmon();
        }
    }

    @Override
    public void printMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public void setTeam(Team team) throws RemoteException {
        team = this.teamA;
    }

    public ArrayList<Objectmon> getObjectmons() {
        ArrayList<Objectmon> objs = new ArrayList<>();
        Iterator it = this.getTeam().listIterator();
        while(it.hasNext()) {
            Objectmon o = (Objectmon) it.next();
            objs.add(o);
        }
        return objs;
    }

}


