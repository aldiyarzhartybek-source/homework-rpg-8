package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;

import java.util.List;

public class TrapFloor extends TowerFloor {

    private final String floorName;
    private final int spikeDamage;

    public TrapFloor(String floorName, int spikeDamage) {
        this.floorName = floorName;
        this.spikeDamage = spikeDamage;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[SETUP] Hidden pressure plates begin to click.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[CHALLENGE] Trap sequence hits the whole party.");
        int totalDamage = 0;
        int aliveIndex = 0;

        for (Hero hero : party) {
            if (!hero.isAlive()) {
                continue;
            }

            int before = hero.getHp();
            hero.takeRawDamage(spikeDamage);
            totalDamage += Math.max(0, before - hero.getHp());

            if (!hero.isAlive()) {
                continue;
            }

            if (aliveIndex % 2 == 0) {
                hero.setState(new PoisonedState(2));
            } else {
                hero.setState(new StunnedState());
            }
            aliveIndex++;
        }

        boolean cleared = hasAliveHeroes(party);
        String summary = cleared
                ? "The party survives the trap corridor."
                : "No one survived the trap corridor.";

        return new FloorResult(cleared, totalDamage, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[LOOT] Emergency kits found near trap controls.");
        if (!result.isCleared()) {
            System.out.println("[LOOT] Nobody is left to claim loot.");
            return;
        }
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(2);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[CLEANUP] Trap mechanisms reset.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    private boolean hasAliveHeroes(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }
}
