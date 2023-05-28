package pl.itarea.time.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.itarea.time.TimePlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayTimeManager {

    private final TimePlugin plugin;
    private final Map<UUID, Long> playerPlayTimes;

    public PlayTimeManager(TimePlugin plugin) {
        this.plugin = plugin;
        this.playerPlayTimes = new HashMap<>();
    }

    public long getPlayerPlayTime(UUID playerId) {
        return this.playerPlayTimes.getOrDefault(playerId, 0L);
    }

    public void loadPlayerPlayTimes() {
        YamlConfiguration data = YamlConfiguration.loadConfiguration(this.getDataFile());

        for (String uuid : data.getKeys(false)) {
            playerPlayTimes.put(UUID.fromString(uuid), data.getLong(uuid));
        }
    }

    public void savePlayerPlayTimes() {
        YamlConfiguration data = new YamlConfiguration();

        for (Map.Entry<UUID, Long> entry : playerPlayTimes.entrySet()) {
            data.set(entry.getKey().toString(), entry.getValue());
        }

        try {
            data.save(this.getDataFile());

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private File getDataFile() {
        return new File(plugin.getDataFolder(), "player-play-times.yml");
    }

    public void startPlayTimeTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID playerId = player.getUniqueId();
                long playTime = this.getPlayerPlayTime(playerId);

                ++playTime;

                this.playerPlayTimes.put(playerId, playTime);
                this.plugin.getRewardManager().checkAndGiveRewards(player, playTime);
            }
        }, 20L, 20L);
    }
}