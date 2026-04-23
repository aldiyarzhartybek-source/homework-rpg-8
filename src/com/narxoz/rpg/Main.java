package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.CombatFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for Homework 8 — The Haunted Tower: Ascending the Floors.
 *
 * Build your heroes, floors, tower runner, and execute the climb here.
 */
public class Main {

    public static void main(String[] args) {
        List<Hero> party = new ArrayList<>();
        party.add(new Hero("Aria", 42, 11, 2, new PoisonedState(2)));
        party.add(new Hero("Brom", 55, 9, 3, new StunnedState()));

        List<TowerFloor> floors = List.of(
                new CombatFloor("Floor 1: Rotten Barracks", "Ghoul Scout", 26, 8),
                new TrapFloor("Floor 2: Venom Hall", 5),
                new RestFloor("Floor 3: Silent Chapel", 7),
                new BossFloor("Floor 4: Throne of Ashes", "Dread Knight", 48, 10)
        );

        System.out.println("===== Haunted Tower Run Started =====");
        TowerRunner runner = new TowerRunner(floors);
        TowerRunResult result = runner.run(party);

        System.out.println("\n===== Final TowerRunResult =====");
        System.out.println("Floors cleared: " + result.getFloorsCleared());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Reached top: " + result.isReachedTop());
    }
}
