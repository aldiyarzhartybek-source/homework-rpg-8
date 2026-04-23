package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;

import java.util.List;

public class RestFloor extends TowerFloor {

    private final String floorName;
    private final int healAmount;

    public RestFloor(String floorName, int healAmount) {
        this.floorName = floorName;
        this.healAmount = healAmount;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[SETUP] A quiet sanctuary with warm light.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[CHALLENGE] No enemies here, only recovery.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(healAmount);
            }
        }
        return new FloorResult(true, 0, "The party catches its breath.");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[LOOT] Rest floor gives no loot.");
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[CLEANUP] Campfire embers fade.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }
}
