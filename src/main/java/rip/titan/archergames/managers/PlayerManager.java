package rip.titan.archergames.managers;

import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.titan.archergames.ArcherGamesPlugin;
import rip.titan.archergames.player.PlayerProfile;

import java.util.*;

@RequiredArgsConstructor
public class PlayerManager {

    private final ArcherGamesPlugin plugin;

    private final Map<UUID, PlayerProfile> profiles = new HashMap<>();
    private final Set<Player> spectators = new HashSet<>();

    private int contestants = 0;

    public void createProfile(UUID id, String name) {
        PlayerProfile profile = new PlayerProfile(plugin, id, name);
        profiles.put(id, profile);
    }

    public PlayerProfile getProfile(Player player) {
        return profiles.get(player.getUniqueId());
    }

    public void removeProfile(Player player) {
        profiles.remove(player.getUniqueId());
    }

    public void saveAllProfiles() {
        for (PlayerProfile profile : profiles.values()) {
            profile.save(false, plugin);
        }
    }

    public void removePlayer(Player player) {
        PlayerProfile profile = getProfile(player);
        if (profile.getLastAttacker() != null) {
            PlayerProfile killerProfile = plugin.getPlayerManager().getProfile(plugin.getServer().getPlayer(profile.getLastAttacker()));
            killerProfile.handleKill();
            plugin.getServer().broadcastMessage(killerProfile.getName() + " killed " + player);
        } else {
            plugin.getServer().broadcastMessage(player + " has been killed.");
        }
        addSpectator(player);
    }

    private void addSpectator(Player player) {
        if (spectators.contains(player)) {
            player.kickPlayer("Error: Already spectating");
            return;
        }
        spectators.add(player);
        spectators.forEach(spectator -> spectator.getScoreboard().getTeam("SeeInvis").addEntry(player.getName()));
        player.setGameMode(GameMode.CREATIVE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
        plugin.getServer().getOnlinePlayers().forEach(online -> {
            player.showPlayer(online);
            if (online.getGameMode() == GameMode.SURVIVAL) {
                online.hidePlayer(player);
            }
        });
        calculateContestants();
    }

    public void removeSpectator(Player player) {
            spectators.remove(player);
            spectators.forEach(spectator -> spectator.getScoreboard().getTeam("SeeInvis").removeEntry(player.getName()));
    }

    public boolean isSpectating(Player player) {
        return spectators.contains(player);
    }

    public void calculateContestants() {
        int contestants = (int) plugin.getServer().getOnlinePlayers().stream()
                .filter(online -> online.getGameMode() == GameMode.SURVIVAL).count();
        this.contestants = contestants;

    }

    public int getContestants() {
        return contestants;
    }
}
