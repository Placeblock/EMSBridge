package de.codelix.emsbridge.listener;

import de.codelix.emsbridge.Config;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.EntityMap;
import de.codelix.emsbridge.minimessage.Texts;
import de.codelix.emsbridge.storage.EntityPlayerRepository;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final EntityPlayerRepository repo;
    private final EntityMap map;

    @EventHandler
    public void on(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        Integer entityId;
        try {
            entityId = this.repo.getEntityIdByPlayerUuid(uuid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.map.addPlayer(uuid, entityId);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Integer entityId = this.map.getEntityId(uuid);
        if (entityId != null) return;

        Config cfg = EMSBridge.INSTANCE.getCfg();
        Config.Book introductionBook = cfg.getBooks().getBook("introduction");
        List<Component> pages = introductionBook.getPages().stream().map(p -> (Component) Texts.text(p)).toList();
        player.openBook(Book.builder()
                .title(Texts.text(introductionBook.getTitle()))
                .author(Texts.text(introductionBook.getAuthor()))
                .pages(pages));
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        this.map.removePlayer(uuid);
    }

}
