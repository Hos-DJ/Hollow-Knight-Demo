package com.Ap.HollowKnight.model.player;

public class CharmManager {
    private final Charm[] activeCharms = new Charm[3];

    public CharmManager() {
        activeCharms[0] = null;
        activeCharms[1] = null;
        activeCharms[2] = null;
    }


    public boolean equipCharm(Charm charm, Knight knight) {
        if (isEquipped(charm)) {
            System.out.println("This charm is already equipped!");
            return false;
        }

        for (int i = 0; i < activeCharms.length; i++) {
            if (activeCharms[i] == null) {
                activeCharms[i] = charm;
                charm.equipEffect(knight);
                System.out.println(charm.name() + " equipped at slot " + i);
                return true;
            }
        }

        System.out.println("Charm slots are full!");
        return false;
    }


    public boolean unequipCharm(Charm charm, Knight knight) {
        for (int i = 0; i < activeCharms.length; i++) {
            if (activeCharms[i] == charm) {
                charm.unequipEffect(knight);
                activeCharms[i] = null;
                System.out.println(charm.name() + " unequipped from slot " + i);

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
}
