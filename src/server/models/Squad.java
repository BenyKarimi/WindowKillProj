package server.models;

import java.util.ArrayList;
import java.util.Objects;

public class Squad {
    private String name;
    private User leader;
    private ArrayList<User> members;
    private Squad enemySquad;
    private boolean palioxis;
    private boolean adonis;
    private boolean gefjon;
    private int squadXP;
    private int monomachiaWinCounter;
    private int gainedXp;
    private ArrayList<String> history;
    public Squad(String name) {
        this.name = name;
        members = new ArrayList<>();
        history = new ArrayList<>();
        palioxis = false;
        adonis = false;
        gefjon = false;
        squadXP = 0;
        gainedXp = 0;
        monomachiaWinCounter = 0;
    }

    public String getName() {
        return name;
    }

    public int getGainedXp() {
        return gainedXp;
    }

    public void setMonomachiaWinCounter(int monomachiaWinCounter) {
        this.monomachiaWinCounter = monomachiaWinCounter;
    }

    public int getMonomachiaWinCounter() {
        return monomachiaWinCounter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public Squad getEnemySquad() {
        return enemySquad;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setEnemySquad(Squad enemySquad) {
        this.enemySquad = enemySquad;
    }

    public boolean isPalioxis() {
        return palioxis;
    }

    public void setPalioxis(boolean palioxis) {
        this.palioxis = palioxis;
    }

    public boolean isAdonis() {
        return adonis;
    }

    public void setAdonis(boolean adonis) {
        this.adonis = adonis;
    }

    public boolean isGefjon() {
        return gefjon;
    }

    public void setGefjon(boolean gefjon) {
        this.gefjon = gefjon;
    }

    public int getSquadXP() {
        return squadXP;
    }

    public void setSquadXP(int squadXP) {
        this.squadXP = squadXP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Squad squad = (Squad) o;
        return Objects.equals(name, squad.name);
    }
}
