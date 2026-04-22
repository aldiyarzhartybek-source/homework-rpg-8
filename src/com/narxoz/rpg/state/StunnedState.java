package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState {

    private int turnsRemaining;

    public StunnedState() {
        this(1);
    }

    public StunnedState(int turnsRemaining) {
        this.turnsRemaining = turnsRemaining;
    }

    @Override
    public String getName() {
        return "Stunned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return basePower;
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(1, (int) Math.round(rawDamage * 1.10));
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println(hero.getName() + " is stunned and cannot move.");
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
            System.out.println(hero.getName() + " is still stunned for "
                    + turnsRemaining + " more turn(s).");
        }
    }

    @Override
    public boolean canAct() {
        return false;
    }
}