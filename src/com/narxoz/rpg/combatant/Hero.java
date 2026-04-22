package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.state.StunnedState;

public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this(name, hp, attackPower, defense, new NormalState());
    }

    public Hero(String name, int hp, int attackPower, int defense, HeroState initialState) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = (initialState != null) ? initialState : new NormalState();
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }
    public HeroState getState()    { return state; }

    public void setState(HeroState newState) {
        if (newState == null) {
            return;
        }

        String oldName = (state == null) ? "None" : state.getName();
        this.state = newState;

        if (!oldName.equals(newState.getName())) {
            System.out.println(name + " changes state: " + oldName + " -> " + newState.getName());
        }
    }

    public void restoreNormalState() {
        setState(new NormalState());
    }

    public void startTurn() {
        if (isAlive()) {
            state.onTurnStart(this);
        }
    }

    public void endTurn() {
        if (isAlive()) {
            state.onTurnEnd(this);
        }
    }

    public boolean canAct() {
        return isAlive() && state.canAct();
    }

    public int attack(Monster monster) {
        if (!isAlive() || monster == null || !monster.isAlive()) {
            return 0;
        }

        startTurn();

        if (!isAlive()) {
            return 0;
        }

        if (!canAct()) {
            System.out.println(name + " skips the turn because of " + state.getName() + ".");
            endTurn();
            return 0;
        }

        int damage = Math.max(1, state.modifyOutgoingDamage(attackPower));
        monster.takeDamage(damage);

        System.out.println(name + " attacks " + monster.getName()
                + " for " + damage + " damage [" + state.getName() + "]");

        endTurn();
        return damage;
    }

    public void takeDamage(int amount) {
        if (amount <= 0 || !isAlive()) {
            return;
        }

        int afterDefense = Math.max(1, amount - defense);
        int modified = Math.max(1, state.modifyIncomingDamage(afterDefense));

        hp = Math.max(0, hp - modified);

        System.out.println(name + " takes " + modified + " damage ["
                + state.getName() + "] -> " + hp + "/" + maxHp + " HP");

        triggerLowHpBerserk();
    }

    public void takeRawDamage(int amount) {
        if (amount <= 0 || !isAlive()) {
            return;
        }

        hp = Math.max(0, hp - amount);

        System.out.println(name + " suffers " + amount
                + " direct damage -> " + hp + "/" + maxHp + " HP");

        triggerLowHpBerserk();
    }

    public void heal(int amount) {
        if (amount <= 0 || !isAlive()) {
            return;
        }

        int before = hp;
        hp = Math.min(maxHp, hp + amount);
        int restored = hp - before;

        if (restored > 0) {
            System.out.println(name + " heals " + restored
                    + " HP -> " + hp + "/" + maxHp + " HP");
        }
    }

    private void triggerLowHpBerserk() {
        if (!isAlive()) {
            return;
        }

        int threshold = Math.max(1, maxHp / 3);

        if (hp <= threshold
                && !(state instanceof BerserkState)
                && !(state instanceof StunnedState)) {
            setState(new BerserkState(2));
        }
    }
}