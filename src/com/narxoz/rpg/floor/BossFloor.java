package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.StunnedState;

import java.util.List;

public class BossFloor extends TowerFloor {

    private final String floorName;
    private final String bossName;
    private final int bossHp;
    private final int bossAttack;
    private Monster boss;

    public BossFloor(String floorName, String bossName, int bossHp, int bossAttack) {
        this.floorName = floorName;
        this.bossName = bossName;
        this.bossHp = bossHp;
        this.bossAttack = bossAttack;
    }

    @Override
    protected void announce() {
        System.out.println("\n=== BOSS FLOOR: " + getFloorName() + " ===");
    }

    @Override
    protected void setup(List<Hero> party) {
        boss = new Monster(bossName, bossHp, bossAttack);
        System.out.println("[SETUP] " + bossName + " descends with " + bossHp + " HP.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[CHALLENGE] Boss battle begins.");
        int totalDamage = 0;
        int round = 1;

        while (boss.isAlive() && hasAliveHeroes(party)) {
            System.out.println("-- Round " + round + " --");
            for (Hero hero : party) {
                if (!hero.isAlive() || !boss.isAlive()) {
                    continue;
                }
                hero.attack(boss);
            }

            if (!boss.isAlive()) {
                break;
            }

            for (Hero hero : party) {
                if (!hero.isAlive()) {
                    continue;
                }
                int before = hero.getHp();
                boss.attack(hero);
                totalDamage += Math.max(0, before - hero.getHp());
            }

            Hero firstAlive = firstAliveHero(party);
            if (firstAlive != null && round % 2 == 0) {
                System.out.println(boss.getName() + " uses shockwave on " + firstAlive.getName() + "!");
                firstAlive.setState(new StunnedState());
            }
            round++;
        }

        boolean cleared = !boss.isAlive();
        String summary = cleared ? boss.getName() + " has fallen." : "The boss overwhelms the party.";
        return new FloorResult(cleared, totalDamage, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[LOOT] Boss chest is opened.");
        if (!result.isCleared()) {
            System.out.println("[LOOT] The chest remains sealed.");
            return;
        }
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(6);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[CLEANUP] The throne room grows silent.");
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
