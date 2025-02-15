package de.codelix.emsbridge;

import de.codelix.emsbridge.command.ExceptionRunnable;
import de.codelix.emsbridge.command.impl.AccountCommand;
import de.codelix.emsbridge.command.impl.RegisterCommand;
import de.codelix.emsbridge.command.impl.TeamCommand;
import de.codelix.emsbridge.listener.PlayerListener;
import de.codelix.emsbridge.listener.ZeromqListener;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.service.EntityService;
import de.codelix.emsbridge.storage.EntityPlayerRepository;
import de.codelix.entitymanagementsystem.EMS;
import de.codelix.entitymanagementsystem.http.HttpException;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public class EMSBridge extends JavaPlugin {

    public static EMSBridge INSTANCE;

    private Config cfg;

    private EntityService entityService;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.saveDefaultConfig();

        ConfigurationSection section = this.getConfig().getConfigurationSection("");
        if (section == null) {
            throw new IllegalStateException("Configuration could not be loaded");
        }
        this.cfg = new Config(section);

        EntityPlayerRepository entityPlayerRepository = new EntityPlayerRepository(this.cfg.getDb());
        this.entityService = new EntityService(entityPlayerRepository, new EMS());

        PlayerListener playerListener = new PlayerListener(this.entityService);
        this.getServer().getPluginManager().registerEvents(playerListener, this);

        new RegisterCommand(this, this.entityService).register();
        new AccountCommand(this, this.entityService).register();
        new TeamCommand(this).register();

        new Thread(() -> new ZeromqListener(this.entityService).listen()).start();
    }

    public void handleErrors(String action, Player p, ExceptionRunnable runnable) {
        try {
            runnable.run();
        } catch (HttpException ex) {
            p.sendMessage(Texts.text("<color:red>Could not "+action+":</color> <color:gold>" + ex.getError().detail() + "</color>"));
            EMSBridge.INSTANCE.getLogger().log(Level.SEVERE, "Could not "+action, ex);
        } catch (Exception ex) {
            p.sendMessage(Texts.text("<color:red>Could not "+action+":</color> <color:gold>" + ex.getMessage() + "</color>"));
            EMSBridge.INSTANCE.getLogger().log(Level.SEVERE, "Could not "+action, ex);
        }
    }
}