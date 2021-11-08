package net.lihui.app.plugin.thoughtworkscodereviewtools.entity;


public class PerBoard {

    private String status;
    private int disableAt;
    private int warnAt;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDisableAt() {
        return this.disableAt;
    }

    public void setDisableAt(int disableAt) {
        this.disableAt = disableAt;
    }

    public int getWarnAt() {
        return this.warnAt;
    }

    public void setWarnAt(int warnAt) {
        this.warnAt = warnAt;
    }
}
