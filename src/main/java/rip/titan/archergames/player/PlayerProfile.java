package rip.titan.archergames.player;

import rip.titan.archergames.ArcherGamesPlugin;
import us.lemin.core.CorePlugin;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.storage.database.MongoRequest;

import java.util.UUID;

public class PlayerProfile {
    private final UUID id;
    private final String name;
    private boolean godKit = false;
    private UUID lastAttacker;
    private int kills;
    private int credits;


    public PlayerProfile(ArcherGamesPlugin plugin, UUID id, String name) {
        this.id = id;
        this.name = name;
        CoreProfile coreProfile = CorePlugin.getInstance().getProfileManager().getProfile(id);

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
                    .put("kills", getKills())
                    .put("credits", getCredits());

            request.run();
        };

        if (async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }

    public void setGodKit(boolean godKit) {
        this.godKit = godKit;
    }

    public boolean hasGodKit() {
        return this.godKit;
    }

    public void setLastAttacker(UUID lastAttacker) {
        this.lastAttacker = lastAttacker;
    }

    public UUID getLastAttacker() {
        return lastAttacker;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public int getCredits() {
        return credits;
    }

    public void addCredits(int increasedBy) {
        this.credits = this.credits + increasedBy;
    }

    public int getKills() {
        return kills;
    }

    public void handleKill() {
        this.credits++;
    }
}
