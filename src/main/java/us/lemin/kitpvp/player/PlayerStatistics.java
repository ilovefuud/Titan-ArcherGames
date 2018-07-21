package us.lemin.kitpvp.player;

import lombok.Getter;

public class PlayerStatistics {
    @Getter
    private int kills, deaths, killStreak, highestKillStreak, credits, eventWins;

    public double getKillDeathRatio() {
        return deaths == 0 ? kills : (double) kills / deaths;
    }

    public boolean handleDeath() {
        boolean newRecordStreak = killStreak > highestKillStreak;

        if (newRecordStreak) {
            highestKillStreak = killStreak;
        }

        killStreak = 0;
        deaths++;

        return newRecordStreak;
    }

    public void handleKill() {
        kills++;
        killStreak++;
    }
}
