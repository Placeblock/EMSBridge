package de.codelix.emsbridge.listener;

import de.codelix.emsbridge.EntityMap;
import de.codelix.emsbridge.storage.EntityPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    EntityPlayerRepository repo;
    EntityMap map;

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
        // TODO: OPEN GUI
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        this.map.removePlayer(uuid);
    }

}
