package de.codelix.emsbridge.listener;

import de.codelix.emsbridge.Config;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.messages.Messages;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.service.EntityService;
import de.codelix.entitymanagementsystem.models.Entity;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final EntityService entityService;
    private final Map<UUID, Entity> tmpEntities = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        Entity entity;
        try {
            entity = this.entityService.getEntityByPlayerUuid(uuid);
        } catch (RuntimeException e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Messages.ERROR_LOAD_ENTITY);
            throw new RuntimeException(e);
        }
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
        this.tmpEntities.remove(uuid);
        if (entity != null) {
            this.entityService.renameEntityLocal(player, entity.getName());
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

}
