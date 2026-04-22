package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private int turnsRemaining;

    public PoisonedState() {
        this(3);
    }

    public PoisonedState(int turnsRemaining) {
        this.turnsRemaining = turnsRemaining;
    }

    @Override
    public String getName() {
        return "Poisoned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(1, (int) Math.round(basePower * 0.80));
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(1, (int) Math.round(rawDamage * 1.20));
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println(hero.getName() + " suffers from poison.");
        hero.takeRawDamage(4);
    }

    @Override
    public void onTurnEnd(Hero hero) {
        if (!hero.isAlive()) {
            return;
        }

        turnsRemaining--;

        if (turnsRemaining <= 0) {
            hero.setState(new NormalState());
        } else {
            System.out.println(hero.getName() + " remains poisoned for "
                    + turnsRemaining + " more turn(s).");
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}