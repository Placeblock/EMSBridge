package de.codelix.emsbridge.service;

import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.exceptions.*;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.storage.EntityPlayerRepository;
import de.codelix.entitymanagementsystem.EMS;
import de.codelix.entitymanagementsystem.models.Entity;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

@RequiredArgsConstructor
public class EntityService {
    private final EntityPlayerRepository repo;
    private final EMS ems;

    public Entity getEntityByPlayerUuid(UUID playerUuid) {
        Integer entityId;
        try {
            entityId = this.repo.getEntityIdByPlayerUuid(playerUuid);
        } catch (SQLException e) {
            throw new EntityLoadException(playerUuid, e);
        }
        if (entityId == null) return null;
        Entity entity;
        try {
            entity = this.ems.getEntity(entityId).join();
        } catch (RuntimeException e) {
            throw new EntityLoadException(playerUuid, e);
        }
        return entity;
    }

    public void renameEntity(UUID playerUuid, String newName) {
        int entityId = this.getEntityId(playerUuid);
        this.ems.renameEntity(entityId, newName).join();
    }

    public Entity createEntity(UUID playerUuid, String newName) {
        Integer existingEntityId;
        try {
            existingEntityId = this.repo.getEntityIdByPlayerUuid(playerUuid);
        } catch (SQLException e) {
            throw new EntityLoadException(playerUuid, e);
        }
        if (existingEntityId != null) {
            throw new PlayerAlreadyRegisteredException(playerUuid);
        }
        Entity entity = this.ems.createEntity(newName).join();
        try {
            this.repo.savePlayerEntityId(playerUuid, entity.getId());
        } catch (SQLException e) {
            throw new LinkNotCreatedException(playerUuid, entity.getId());
        }
        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            this.renameEntityLocal(player, newName);
        }
        return entity;
    }

    public void renameEntityLocal(int entityId, final String entityName) {
        UUID playerUuid;
        try {
            playerUuid = this.repo.getPlayerUuidByEntityId(entityId);
        } catch (SQLException e) {
            EMSBridge.INSTANCE.getLogger().log(Level.SEVERE, "Exception while loading player UUID by Entity ID "+entityId+" to rename", e);
            return;
        }
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) return;
        this.renameEntityLocal(player, entityName);
    }

    public void renameEntityLocal(final Player player, final String entityName) {
        player.displayName(Texts.text(entityName));
        player.playerListName(Texts.text(entityName));
    }

    public int getEntityId(UUID playerUuid) {
        Integer entityId;
        try {
            entityId = this.repo.getEntityIdByPlayerUuid(playerUuid);
        } catch (SQLException e) {
            throw new EntityLoadException(playerUuid, e);
        }
        if (entityId == null) {
            throw new PlayerNotRegisteredException(playerUuid);
        }
        return entityId;
    }
}
