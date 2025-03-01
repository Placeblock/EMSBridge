package de.codelix.emsbridge.listener;

import de.codelix.emsbridge.Config;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.messages.Messages;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.service.EntityService;
import de.codelix.emsbridge.service.TeamService;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.models.Team;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final EntityService entityService;
    private final TeamService teamService;
    private final Map<UUID, Entity> tmpEntities = new HashMap<>();
    private final Map<UUID, Team> tmpTeams = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        Entity entity;
        Team team;
        try {
            entity = this.entityService.getEntityByPlayerUuid(uuid);
            team = this.teamService.getTeam(uuid);
        } catch (RuntimeException e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Messages.ERROR_LOAD_ENTITY);
            throw new RuntimeException(e);
        }
        EMSBridge.INSTANCE.getLogger().info("Loaded Player " + uuid + " with team " + team + " and entity " + entity);
        this.tmpTeams.put(uuid, team);
        this.tmpEntities.put(uuid, entity);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!this.tmpEntities.containsKey(uuid)) {
            player.kick(Messages.ERROR_LOAD_ENTITY);
            return;
        }
        Entity entity = this.tmpEntities.get(uuid);
        Team team = this.tmpTeams.get(uuid);
        this.tmpEntities.remove(uuid);
        this.tmpTeams.remove(uuid);
        if (entity != null) {
            this.entityService.addOnlinePlayer(player.getUniqueId(), entity.getId());
            this.entityService.renameEntityLocal(player, Texts.text(entity.getName()), team);
            if (team != null) {
                this.teamService.addPlayerToTeamLocal(entity.getId(), team);
            } else {
                this.teamService.removePlayerFromTeamLocal(entity.getId());
            }
            player.showTitle(Messages.TITLE);
            return;
        }

        Config cfg = EMSBridge.INSTANCE.getCfg();
        Config.Book introductionBook = cfg.getBooks().getBook("introduction");
        List<Component> pages = introductionBook.getPages().stream().map(p -> (Component) Texts.text(p)).toList();
        player.openBook(Book.builder()
                .title(Texts.text(introductionBook.getTitle()))
                .author(Texts.text(introductionBook.getAuthor()))
                .pages(pages));
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player p = event.getEntity();
        event.deathMessage(Texts.text("<color:red>\uD83D\uDD46 ")
                .append(p.displayName().color(NamedTextColor.GRAY)));
    }

    @EventHandler
    public void on(AsyncChatEvent event) {
        event.renderer((source, sourceDisplayName, message, _) -> {
            Component result = sourceDisplayName.color(NamedTextColor.GRAY)
                    .append(Texts.text("<color:gray> > "))
                    .append(message);
            Team team = this.teamService.getTeamByLocalPlayerUuid(source.getUniqueId());
            if (team == null) return result;
            Component prefix = TeamService.getTeamPrefix(team.getName(), TeamService.getTeamColor(team.getHue()));
            return prefix.append(result);
        });
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        this.entityService.removeOnlinePlayer(uuid);
    }
}
