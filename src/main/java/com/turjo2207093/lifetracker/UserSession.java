package com.turjo2207093.lifetracker;

public class UserSession {
    private static UserSession instance;
    
    private int id;
    private String username;
    private String email;
    private int level;
    private int xp;
    private boolean isAdmin;
    private String fullName;
    private String age;
    private String gender;
    private String diaryEntry;

    private UserSession(int id, String username, String email, int level, int xp, boolean isAdmin, String fullName, String age, String gender, String diaryEntry) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.level = level;
        this.xp = xp;
        this.isAdmin = isAdmin;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.diaryEntry = diaryEntry;
    }

    public static void setSession(int id, String username, String email, int level, int xp, boolean isAdmin, String fullName, String age, String gender, String diaryEntry) {
        instance = new UserSession(id, username, email, level, xp, isAdmin, fullName, age, gender, diaryEntry);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void cleanSession() {
        instance = null;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    public boolean isAdmin() { return isAdmin; }
    public String getFullName() { return fullName; }
    public String getAge() { return age; }
    public String getGender() { return gender; }
    public String getDiaryEntry() { return diaryEntry; }

    public void setLevel(int level) { this.level = level; }
    public void setXp(int xp) { this.xp = xp; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAge(String age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDiaryEntry(String diaryEntry) { this.diaryEntry = diaryEntry; }
}
