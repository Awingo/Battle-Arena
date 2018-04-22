//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Gym;

import edu.uab.cs203.Team;
import edu.uab.cs203.lab09.Lab09HashmonGym;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GymServer extends Remote {
    void setTeamA(Team var1) throws RemoteException;

    void setTeamB(Team var1) throws RemoteException;

    void setTeamAReady(boolean var1) throws RemoteException;

    void setTeamBReady(boolean var1) throws RemoteException;

    String networkToString() throws RemoteException;

    void registerClientA(String var1, int var2, String var3) throws RemoteException;

    void registerClientB(String var1, int var2, String var3) throws RemoteException;

    void printMessage(String var1) throws RemoteException;
}
