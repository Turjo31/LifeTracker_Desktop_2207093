package com.turjo2207093.lifetracker;

public class Habit {
    private String name;
    private String target;
    private int progress;
    private int targetValue;

    public Habit(String name, String target) {
        this.name = name;
        this.target = target;
        this.progress = 0;
        this.targetValue = 1; 
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }
}
