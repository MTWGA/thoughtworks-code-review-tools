package net.lihui.app.plugin.thoughtworkscodereviewtools.entity;

public class TrelloList {

    private String id;
    private String name;
    private boolean closed;
    private int pos;
    private String softLimit;
    private String idBoard;
    private boolean subscribed;
    private Limits limits;

    public TrelloList() {
    }

    public TrelloList(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getSoftLimit() {
        return this.softLimit;
    }

    public void setSoftLimit(String softLimit) {
        this.softLimit = softLimit;
    }

    public String getIdBoard() {
        return this.idBoard;
    }

    public void setIdBoard(String idBoard) {
        this.idBoard = idBoard;
    }

    public boolean getSubscribed() {
        return this.subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Limits getLimits() {
        return this.limits;
    }

    public void setLimits(Limits limits) {
        this.limits = limits;
    }
}
