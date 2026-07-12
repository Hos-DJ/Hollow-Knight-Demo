package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.charms.Charm;
import com.Ap.HollowKnight.model.charms.CharmManager;
import com.Ap.HollowKnight.model.Knight.Knight;

public class InventoryController {
    private final Knight knight;
    private final CharmManager charmManager;

    public InventoryController(){
        this.knight = LevelModel.getInstance().getKnight();
        this.charmManager = knight.getCharmManager();
    }

    public boolean equipCharm(Charm charm) {
        return charmManager.equipCharm(charm, knight);
    }

    public boolean unequipCharm(Charm charm) {
        return charmManager.unequipCharm(charm, knight);
    }

    public boolean isEquipped(Charm charm) {
        return charmManager.isEquipped(charm);
    }

    public Charm[] getActiveCharms() {
        return charmManager.getActiveCharms();
    }
    public boolean isUnlocked(Charm charm) {
        return charmManager.isUnlocked(charm);
    }

}
