package dev.bestzige.model;

public class User {
    private int id;
    private String username;
    private int credit;

    public User(int id, String username, int credit) {
        this.id = id;
        this.username = username;
        this.credit = credit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
