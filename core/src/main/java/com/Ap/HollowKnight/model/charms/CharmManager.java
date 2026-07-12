package com.Ap.HollowKnight.model.charms;

import com.Ap.HollowKnight.model.Knight.Knight;

import java.util.HashSet;
import java.util.Set;

public class CharmManager {
    private final Charm[] activeCharms = new Charm[3];
    private final Set<Charm> unlockedCharms = new HashSet<>();
    public CharmManager() {
        activeCharms[0] = null;
        activeCharms[1] = null;
        activeCharms[2] = null;
        for(Charm charm : Charm.values()) {
            if(charm.equals(Charm.VOID_HEART))
                continue;
            unlockedCharms.add(charm);
        }
    }


    public boolean equipCharm(Charm charm, Knight knight) {
        if (isEquipped(charm)) {
            return false;
        }

        for (int i = 0; i < activeCharms.length; i++) {
            if (activeCharms[i] == null) {
                activeCharms[i] = charm;
                charm.equipEffect(knight);
                return true;
            }
        }

        return false;
    }


    public boolean unequipCharm(Charm charm, Knight knight) {
        for (int i = 0; i < activeCharms.length; i++) {
            if (activeCharms[i] == charm) {
                charm.unequipEffect(knight);
                activeCharms[i] = null;
                recalculateStats(knight);
                return true;
            }
        }
        return false;
    }


    public boolean isEquipped(Charm charm) {
        for (Charm c : activeCharms) {
            if (c == charm) {
                return true;
            }
        }
        return false;
    }


    private void recalculateStats(Knight knight) {

        for (Charm c : activeCharms) {
            if (c != null) {
                c.equipEffect(knight);
            }
        }
    }

    public Charm[] getActiveCharms() {
        return activeCharms;
    }

    public void unlockCharm(Charm charm) {
        unlockedCharms.add(charm);
    }

    public boolean isUnlocked(Charm charm) {
        return unlockedCharms.contains(charm);
    }
}
