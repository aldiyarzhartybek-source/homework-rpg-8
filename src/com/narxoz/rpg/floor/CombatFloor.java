package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;

import java.util.List;

public class CombatFloor extends TowerFloor {

    private final String floorName;
    private final String monsterName;
    private final int monsterHp;
    private final int monsterAttack;
    private Monster monster;

    public CombatFloor(String floorName, String monsterName, int monsterHp, int monsterAttack) {
        this.floorName = floorName;
        this.monsterName = monsterName;
        this.monsterHp = monsterHp;
        this.monsterAttack = monsterAttack;
    }

    @Override
    protected void setup(List<Hero> party) {
        monster = new Monster(monsterName, monsterHp, monsterAttack);
        System.out.println("[SETUP] A wild " + monsterName + " appears with " + monsterHp + " HP.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[CHALLENGE] Combat starts.");
        int totalDamage = 0;

        while (monster.isAlive() && hasAliveHeroes(party)) {
            for (Hero hero : party) {
                if (!hero.isAlive() || !monster.isAlive()) {
                    continue;
                }
                hero.attack(monster);
                if (!monster.isAlive()) {
                    break;
                }
            }

            if (monster.isAlive()) {
                Hero target = firstAliveHero(party);
                if (target != null) {
                    System.out.println(monster.getName() + " strikes back at " + target.getName() + ".");
                    int before = target.getHp();
                    monster.attack(target);
                    totalDamage += Math.max(0, before - target.getHp());
                }
            }
        }

        boolean cleared = !monster.isAlive();
        String summary = cleared ? monster.getName() + " is defeated." : "The party was wiped out.";
        return new FloorResult(cleared, totalDamage, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[LOOT] Battle rewards are distributed.");
        if (!result.isCleared()) {
            System.out.println("[LOOT] No rewards due to defeat.");
            return;
        }

        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(3);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[CLEANUP] Combat floor is cleaned up.");
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

    private Hero firstAliveHero(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return hero;
            }
        }
        return null;
    }
}
