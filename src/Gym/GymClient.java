//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Gym;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.attacks.AbstractAttack;
import edu.uab.cs203.effects.StatusEffect;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Iterator;

public interface GymClient extends Remote {
    default AbstractAttack nextAttack(Objectmon objectmon) throws RemoteException {
        return objectmon.nextAttack();
    }

    default boolean addStatusEffectFromAttack(AbstractAttack attack, Objectmon affectedObjectmon) throws RemoteException {
        Objectmon local = null;
        Iterator var4 = this.getTeam().iterator();

        while(var4.hasNext()) {
            Objectmon o = (Objectmon)var4.next();
            if (o.equals(affectedObjectmon)) {
                local = o;
                break;
            }
        }

        StatusEffect effect = attack.getStatusEffect(affectedObjectmon);
        return effect != null ? local.addStatusEffect(effect) : false;
    }

    /** @deprecated */
    @Deprecated
    default void disconnect() throws RemoteException {
        System.out.println("Disconnecting!");
        System.exit(-1);
    }

    Objectmon networkApplyDamage(Objectmon var1, Objectmon var2, int var3) throws RemoteException;

    void networkTick() throws RemoteException;

    void printMessage(String var1) throws RemoteException;

    void setTeam(Team var1) throws RemoteException;

    Team<Objectmon> getTeam() throws RemoteException;

    Objectmon nextObjectmon() throws RemoteException;
}
