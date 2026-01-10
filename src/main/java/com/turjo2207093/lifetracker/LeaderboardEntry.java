package com.turjo2207093.lifetracker;

public class LeaderboardEntry {
    private int rank;
    private String username;
    private int level;
    private int xp;

    public LeaderboardEntry(int rank, String username, int level, int xp) {
        this.rank = rank;
        this.username = username;
        this.level = level;
        this.xp = xp;
    }

    public int getRank() { return rank; }
    public String getUsername() { return username; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
}
