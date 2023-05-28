package pl.itarea.time;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import pl.itarea.time.commands.TimeCommand;
import pl.itarea.time.managers.PlayTimeManager;
import pl.itarea.time.managers.RewardManager;

public class TimePlugin extends JavaPlugin {

    private TimeConfig config;
    private PlayTimeManager playTimeManager;
    private RewardManager rewardManager;

    @Override
    public void onEnable() {
        this.config = new TimeConfig();
        this.config.load(this);

        this.rewardManager = new RewardManager(this);

        this.playTimeManager = new PlayTimeManager(this);
        this.playTimeManager.loadPlayerPlayTimes();
        this.playTimeManager.startPlayTimeTask();

        final TimeCommand command = new TimeCommand(this);
        final PluginCommand internalCommand = this.getCommand("playtime");
        internalCommand.setExecutor(command);
        internalCommand.setTabCompleter(command);
    }

    @Override
    public void onDisable() {
        if (this.playTimeManager != null) {
            this.playTimeManager.savePlayerPlayTimes();
        }
    }

    public TimeConfig config() {
        return config;
    }

    public PlayTimeManager getPlayTimeManager() {
        return this.playTimeManager;
    }

    public RewardManager getRewardManager() {
        return this.rewardManager;
    }
}
