package rip.titan.archergames.player;

import lombok.Getter;
import lombok.Setter;
import rip.titan.archergames.ArcherGamesPlugin;
import us.lemin.core.CorePlugin;
import us.lemin.core.storage.database.MongoRequest;

import java.util.UUID;
@Getter
@Setter
public class PlayerProfile {
    private final UUID id;
    private final String name;
    private boolean godKit = false;
    private UUID lastAttacker;
    private int kills;
    private int credits;


    public PlayerProfile(UUID id, String name) {
        this.id = id;
        this.name = name;

        CorePlugin.getInstance().getMongoStorage().getOrCreateDocument("archergames", id, (document, found) -> {
            if (found) {
                godKit = document.getBoolean("god_kit", false);
                kills = document.getInteger("kills", 0);
                credits = document.getInteger("credits", 0);
            }
        });
    }

    public void save(ArcherGamesPlugin plugin) {
        save(true, plugin);
    }

    public void save(boolean async, ArcherGamesPlugin plugin) {
        Runnable runnable = () -> {
            MongoRequest request = MongoRequest.newRequest("archergames", id)
                    .put("credits", getCredits());
            request.run();
        };

        if (async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }

    public void handleKill() {
        this.kills++;
    }

    public void removeCredits(int removedAmount) {this.credits = this.credits - removedAmount;}

    public void addCredits(int addedAmount) {this.credits = this.credits + addedAmount;}
}
