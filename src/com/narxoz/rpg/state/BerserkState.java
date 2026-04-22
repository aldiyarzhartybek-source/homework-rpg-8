package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class BerserkState implements HeroState {

    private int turnsRemaining;

    public BerserkState() {
        this(2);
    }

    public BerserkState(int turnsRemaining) {
        this.turnsRemaining = turnsRemaining;
    }

    @Override
    public String getName() {
        return "Berserk";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(1, (int) Math.round(basePower * 1.60));
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(1, (int) Math.round(rawDamage * 1.25));
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println(hero.getName() + " is consumed by rage.");
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
            System.out.println(hero.getName() + " stays berserk for "
                    + turnsRemaining + " more turn(s).");
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}