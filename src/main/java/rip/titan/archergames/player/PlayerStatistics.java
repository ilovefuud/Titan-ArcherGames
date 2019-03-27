package rip.titan.archergames.player;

public class PlayerStatistics {


    private int kills;

    public void handleKill() {
        kills++;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

}
