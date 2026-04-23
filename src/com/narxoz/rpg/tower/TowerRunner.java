package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;

import java.util.ArrayList;
import java.util.List;

public class TowerRunner {

    private final List<TowerFloor> floors;

    public TowerRunner(List<TowerFloor> floors) {
        this.floors = new ArrayList<>(floors);
    }

    public TowerRunResult run(List<Hero> party) {
        int cleared = 0;

        for (TowerFloor floor : floors) {
            if (!hasAliveHeroes(party)) {
                System.out.println("The party can no longer continue.");
                break;
            }

            FloorResult result = floor.explore(party);
            System.out.println("[RESULT] " + result.getSummary());

            if (result.isCleared()) {
                cleared++;
            } else {
                break;
            }
        }

        int survivors = countAliveHeroes(party);
        boolean reachedTop = cleared == floors.size() && survivors > 0;
        return new TowerRunResult(cleared, survivors, reachedTop);
    }

    private boolean hasAliveHeroes(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private int countAliveHeroes(List<Hero> party) {
        int alive = 0;
        for (Hero hero : party) {
            if (hero.isAlive()) {
                alive++;
            }
        }
        return alive;
    }
}
