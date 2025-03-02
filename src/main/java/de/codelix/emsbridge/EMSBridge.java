package de.codelix.emsbridge;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import de.codelix.emsbridge.command.impl.AccountCommand;
import de.codelix.emsbridge.command.impl.RegisterCommand;
import de.codelix.emsbridge.command.impl.TeamCommand;
import de.codelix.emsbridge.command.impl.TeamMsgCommand;
import de.codelix.emsbridge.listener.MotdListener;
import de.codelix.emsbridge.listener.PlayerListener;
import de.codelix.emsbridge.listener.ZeromqListener;
import de.codelix.emsbridge.listener.MotdListener;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.metrics.impl.*;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.service.EntityService;
import de.codelix.emsbridge.service.TeamService;
import de.codelix.emsbridge.storage.EntityPlayerMap;
import de.codelix.emsbridge.storage.EntityPlayerRepository;
import de.codelix.entitymanagementsystem.EMS;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class EMSBridge extends JavaPlugin {

    public static EMSBridge INSTANCE;
    public static InfluxDBClient INFLUX_CLIENT;
    public static WriteApi INFLUX;
    public static ScheduledExecutorService SCHEDULER;

    private Config cfg;

    private EntityService entityService;
    private TeamService teamService;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.saveDefaultConfig();

        ConfigurationSection section = this.getConfig().getConfigurationSection("");
        if (section == null) {
            throw new IllegalStateException("Configuration could not be loaded");
        }
        this.cfg = new Config(section);

        EntityPlayerRepository entityPlayerRepository = new EntityPlayerRepository(this.cfg.getSqlDb());
        EMS ems = new EMS();
        this.entityService = new EntityService(entityPlayerRepository, ems, new EntityPlayerMap());
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.teamService = new TeamService(ems, this.entityService, mainScoreboard);

        PlayerListener playerListener = new PlayerListener(this.entityService, this.teamService);
        this.getServer().getPluginManager().registerEvents(playerListener, this);

        new RegisterCommand(this, this.entityService).register();
        new AccountCommand(this, this.entityService).register();
        new TeamCommand(this, this.entityService, this.teamService).register();
        new TeamMsgCommand(this, this.teamService).register();

        new Thread(() -> new ZeromqListener(this.entityService, this.teamService).listen()).start();

        this.connectInflux();
        this.enableMetrics();

        this.getServer().getPluginManager().registerEvents(new MotdListener(), this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::checkProjectStartTitles, 0L, 20L);
    }

    private void connectInflux() {
        Config.InfluxDB db = this.cfg.getInfluxDb();
        final String serverURL = "http://"+ db.getHost()+":"+db.getPort();
        final char[] token = db.getToken().toCharArray();
        INFLUX_CLIENT = InfluxDBClientFactory.create(serverURL, token, db.getOrganization(), db.getBucket());
        if (!INFLUX_CLIENT.ping()) {
            throw new IllegalStateException("InfluxDB connection failed");
        }
        INFLUX = INFLUX_CLIENT.makeWriteApi();
        this.getLogger().info("Connected to InfluxDB");
        SCHEDULER = Executors.newScheduledThreadPool(5);
    }

    private void enableMetrics() {
        new TPSMetric(this.getServer()).enable();
        new OnlinePlayersMetric(this.getServer()).enable();
        new ChunksMetric(this.getServer()).enable();
        new BlocksMetric().enable();
        new PlayerHealthMetric().enable();
        new DistanceTraveledMetric().enable();
        new EntityDeathsMetric().enable();
        new PlayerDeathsMetric().enable();
        new EntitiesMetric(this.getServer()).enable();
    }

    private void checkProjectStartTitles() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime projectStart = this.cfg.getProjectStart();
        Duration timeUntilProjectStart = Duration.between(now, projectStart);
        long seconds = timeUntilProjectStart.getSeconds();
        if (seconds < 6 && seconds > 0) {
            this.getServer().showTitle(Title.title(Texts.text("<color:blue>Nostalgicraft"),
                    Texts.text(String.format("<color:green>%d", seconds))));
        }
        if (seconds == 0) {
            this.getServer().showTitle(Title.title(Component.empty(), Texts.text("<color:green>Have fun!")));
        }
    }
}