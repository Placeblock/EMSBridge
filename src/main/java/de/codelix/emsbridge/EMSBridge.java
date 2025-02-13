package de.codelix.emsbridge;

import de.codelix.emsbridge.command.impl.RegisterCommand;
import de.codelix.emsbridge.listener.PlayerListener;
import de.codelix.emsbridge.storage.EntityPlayerRepository;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class EMSBridge extends JavaPlugin {

    public static EMSBridge INSTANCE;

    private final EntityMap entityMap = new EntityMap();
    private Config cfg;
    private EntityPlayerRepository repository;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.saveDefaultConfig();

        ConfigurationSection section = this.getConfig().getConfigurationSection("");
        if (section == null) {
            throw new IllegalStateException("Configuration could not be loaded");
        }
        this.cfg = new Config(section);

        this.repository = new EntityPlayerRepository(this.cfg.getDb());

        PlayerListener playerListener = new PlayerListener(this.repository, this.entityMap);
        this.getServer().getPluginManager().registerEvents(playerListener, this);

        new RegisterCommand(this).register();
    }
}