package dev.bestzige.model;

public class Redeem {
    private String code;
    private int credit;

    public Redeem(String code, int credit) {
        this.code = code;
        this.credit = credit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
